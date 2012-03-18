package com.d2fn.jester.plugin;

import com.yammer.dropwizard.logging.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Message
 * @author Dietrich Featherston
 */
public class Message {
    
    private String channel;
    private String sender;
    private String login;
    private String hostname;
    private String message;
    
    private List<String> words;
    
    // holds the first word of the message
    private String command;
    // holds an array of the remaining words in the message, space delimited
    private List<String> arguments;
    
    public Message(String channel, String sender, String login, String hostname, String message) {
        this.channel = channel;
        this.sender = sender;
        this.login = login;
        this.hostname = hostname;
        this.message = message;
        
        String[] words = message.split("\\s");
        if(words.length > 0) {
            this.words = Arrays.asList(words);
            command = words[0];
            arguments = new ArrayList<String>(words.length - 1);
            if(words.length > 1) {
                arguments.addAll(Arrays.asList(words).subList(1, words.length));
            }
        }
    }
    
    public String getChannel() {
        return channel;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getHostname() {
        return hostname;
    }
    
    public String getMessage() {
        return message;
    }
    
    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }
    
    public boolean isCommand(String cmd) {
        if(command != null && command.equalsIgnoreCase(cmd)) {
            return true;
        }
        return false;
    }

    public boolean hasArguments() {
        return arguments.size() > 0;
    }
    
    public String getCommand() {
        return command;
    } 
    
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }
    
    public boolean mentions(String word) {
        for(String w : words) {
            if(w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
    
    private static final Log log = Log.forClass(Message.class);
}
