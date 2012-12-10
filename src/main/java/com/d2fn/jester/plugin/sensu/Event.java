package com.d2fn.jester.plugin.sensu;

public class Event {
    private String client;
    private String check;
    private int occurrences;
    private String output;
    private int status;
    private boolean flapping;


    @Override
    public String toString() {
        return String.format("%s: %s/%s - %s", getStatusAsString(), getClient(), getCheck(), getOutput());
    }

    public String getClient() {
        return client;
    }

    public String getCheck() {
        return check;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public String getOutput() {
        return output;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusAsString() {
        switch (status) {
            case 0:
                return "OK";
            case 1:
                return "WARNING";
            case 2:
                return "CRITICAL";
            default:
                return "UNKNOWN";
        }
    }

    public boolean isFlapping() {
        return flapping;
    }
}
