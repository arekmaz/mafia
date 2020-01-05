package com.arekmaz.mafia.entity;

import com.arekmaz.mafia.enums.Role;

public class Player {

    private String mNick;
    private Role mRole;

    public Player(String nick, Role role) {
        mNick = nick;
        mRole = role;
    }


    public String getNick() {
        return mNick;
    }

    public Role getRole() {
        return mRole;
    }
}
