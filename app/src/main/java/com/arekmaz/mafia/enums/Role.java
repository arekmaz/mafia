package com.arekmaz.mafia.enums;

public enum Role {
    MAFIA("MAFIA"),
    CITIZEN("CITIZEN");

    public final String value;

    Role(String val) {
        value = val;
    }
}
