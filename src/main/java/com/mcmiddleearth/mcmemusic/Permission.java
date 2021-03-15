package com.mcmiddleearth.mcmemusic;

public enum Permission {
    MANAGE ("mcmemusic.manage"),
    LISTEN ("mcmemusic.listen");

    private final String node;

    Permission(String s) {
        node = s;
    }

    public String getNode() {
        return node;
    }
}
