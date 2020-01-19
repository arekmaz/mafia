package com.arekmaz.mafia.network;

import android.util.Log;

import com.arekmaz.mafia.activities.RoomControlActivity;
import com.arekmaz.mafia.entity.Player;
import com.arekmaz.mafia.enums.Role;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;


public class Connector {
    private final static String HOST_ADDRESS = "192.168.43.1";
    private final static int PORT = 13378;

    public static Thread initServerThread(RoomControlActivity.PlayerActionsCallbacks callbacks) {
        Thread serverThread = createThread(callbacks);
        return serverThread;
    }

    private static Thread createThread(RoomControlActivity.PlayerActionsCallbacks callbacks) {
        return new Thread(() -> {
            try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(HOST_ADDRESS))) {
                while (true) {
                    Socket client = server.accept();
                    if (client != null) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String newPlayerNick = in.readLine();
                        javax.management.relation.Role newPlayerRole = callbacks.getNextPlayerRole();
                        callbacks.onPlayerAdded(new Player(newPlayerNick, newPlayerRole));
                        in.close();
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        out.write(newPlayerRole.getRole());
                        out.close();
                        client.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
