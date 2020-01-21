package com.arekmaz.mafia.enums;

import com.arekmaz.mafia.R;

public enum Role {
    MAFIA("MAFIA"),
    CITIZEN("CITIZEN");

    public final String value;
    public String displayValue;

    Role(String val) {
        this(val, val);
    }

    Role(String val, String displayVal) {
        value = val;
        displayValue = displayVal;
    }

    public static Role fromString(String role) {
        switch (role) {
            case "MAFIA":
                return MAFIA;
            case "CITIZEN":
            default:
                return CITIZEN;
        }
    }

    public String getRole() {
        return value;
    }

    public int getDisplayStringId() {
        switch (this) {
            case MAFIA:
                return R.string.mafia_role_label;
            case CITIZEN:
            default:
                return R.string.citizen_role_label;
        }
    }
}
