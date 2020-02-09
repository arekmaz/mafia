package com.arekmaz.mafia.entity;

import com.arekmaz.mafia.enums.Role;

public class Player {

    private final String mIp;
    private String mNick;
    private Role mRole;

    public Player(String nick, Role role, String ip) {
        mNick = nick;
        mRole = role;
        mIp = ip;
    }


    public String getNick() {
        return mNick;
    }

    public Role getRole() {
        return mRole;
    }

    public String getIp() {
        return mIp;
    }
}
