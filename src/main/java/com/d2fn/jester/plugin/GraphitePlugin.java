package com.d2fn.jester.plugin;

import com.d2fn.jester.bot.JesterBot;
import com.google.common.io.ByteStreams;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GraphitePlugin implements Plugin {
    private final Client jersey;
    private final String urlPrefix;
    private final File storageLocation;
    private final String resultUrlPrefix;
    private final int numImagesToKeep;

    public GraphitePlugin(Client jersey, String urlPrefix, File storageLocation, String resultUrlPrefix, int numImagesToKeep) {
        this.jersey = jersey;
        this.urlPrefix = urlPrefix;
        this.storageLocation = storageLocation;
        this.resultUrlPrefix = resultUrlPrefix;
        this.numImagesToKeep = numImagesToKeep;
    }

    @Override
    public String getName() {
        return "Graphite";
    }

    @Override
    public void call(JesterBot bot, Message msg) throws Exception {
        final String command = msg.getCommand();
        if (command.startsWith(urlPrefix)) {
            final String resultUrl = download(command);
            bot.sendMessage(msg.getChannel(), resultUrl);
        }
    }

    private String download(String command) throws Exception {
        final ClientResponse response = jersey.resource(command).get(ClientResponse.class);
        final InputStream entityInputStream = response.getEntityInputStream();
        try {
            final File file =  write(entityInputStream);
            return getResultUrl(file);
        } finally {
            if (entityInputStream != null) {
                entityInputStream.close();
            }
        }
    }

    private String getResultUrl(File file) {
        return this.resultUrlPrefix + "/" + file.getName();
    }

    private File write(InputStream entityInputStream) throws Exception  {
        final long time = System.currentTimeMillis();
        final File file = new File(this.storageLocation, String.valueOf(time) + ".png");
        final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            ByteStreams.copy(entityInputStream, out);
        } finally {
            out.close();
        }
        return file;
    }
}
