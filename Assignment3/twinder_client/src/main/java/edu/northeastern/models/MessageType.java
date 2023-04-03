package edu.northeastern.models;

public enum MessageType {
    PILL("Pill"),
    SWIPE("Swipe"),
    STATS("Stats"),
    MATCHES("Matches");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
