package com.example.mafia.enums;

public enum AppMode {
    HOST("HOST"),
    PLAYER("PLAYER");

    public final String value;

    AppMode(String val) {
        value = val;
    }
}
