package com.d2fn.jester.plugin.sensu;

import com.d2fn.jester.bot.JesterBot;
import com.d2fn.jester.plugin.Message;
import com.d2fn.jester.plugin.Plugin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.yammer.dropwizard.json.ObjectMapperFactory;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.List;

public class SensuPlugin implements Plugin {
    private final Client jersey;
    private final URI rootUri;
    private final ObjectMapper json = new ObjectMapperFactory().build();

    public static class Payload {
        public long timestamp;
        public Payload() {
            this.timestamp = System.currentTimeMillis();
        }
    }

    public SensuPlugin(Client jersey, URI rootUri) {
        this.jersey = jersey;
        this.rootUri = rootUri;
    }

    @Override
    public String getName() {
        return "Sensu";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        if (msg.isCommand("sensu") && msg.hasArguments()) {
            // events
            final List<String> arguments = msg.getArguments();
            if (arguments.size() >= 1) {
                final String firstArgument = arguments.get(0);

                if ("events".equals(firstArgument)) {
                    // status: client/check - output
                    final List<Event> events = getEvents();
                    for (Event event : events) {
                        bot.sendMessage(msg.getChannel(), event.toString());
                    }
                } else if ("silenced".equals(firstArgument)) {
                    final List<String> silenced = getSilenced();
                    for (String thing : silenced) {
                        bot.sendMessage(msg.getChannel(), thing);
                    }
                } else if ("silence".equals(firstArgument)) {
                    if (arguments.size() <= 1) {
                        bot.sendMessage(msg.getChannel(), "USAGE: silence <client>");
                        bot.sendMessage(msg.getChannel(), "USAGE: silence <client>/<check>");
                    } else {
                        final String target = arguments.get(1);
                        try {
                            silence(target);
                            bot.sendMessage(msg.getChannel(), "Silenced " + target);
                        } catch (Exception e) {
                            bot.sendMessage(msg.getChannel(), "Could not silence " + target + ": " + e.getMessage());
                        }
                    }
                } else if ("unsilence".equals(firstArgument)) {
                    if (arguments.size() <= 1) {
                        bot.sendMessage(msg.getChannel(), "USAGE: unsilence <client>");
                        bot.sendMessage(msg.getChannel(), "USAGE: unsilence <client>/<check>");
                    } else {
                        final String target = arguments.get(1);
                        try {
                            unsilence(target);
                            bot.sendMessage(msg.getChannel(), "Unsilenced " + target);
                        } catch (Exception e) {
                            bot.sendMessage(msg.getChannel(), "Could not unsilence " + target + ": " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private List<String> getSilenced() {
        final List<String> result = Lists.newArrayList();
        for (String stashed : jersey.resource(rootUri).path("stashes").get(new GenericType<List<String>>() {
        })) {
            if (stashed.startsWith("silence/")) {
                result.add(stashed.substring("silence/".length()));
            }
        };
        return result;
    }

    private void unsilence(String target) {
        jersey.resource(rootUri).path("stashes/silence").path(target)
                .delete();
    }

    private void silence(String target) throws JsonProcessingException {
        final String payload = json.writeValueAsString(new Payload());
        jersey.resource(rootUri).path("stashes/silence").path(target)
                .entity(payload.getBytes(Charsets.UTF_8), MediaType.APPLICATION_JSON_TYPE)
                .post(String.class);
    }

    public List<Event> getEvents() {
        return jersey.resource(rootUri).path("events").get(new GenericType<List<Event>>(){});
    }
}
