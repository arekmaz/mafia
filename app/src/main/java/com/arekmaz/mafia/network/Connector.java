package com.arekmaz.mafia.network;

import android.nfc.Tag;
import android.util.Log;

import com.arekmaz.mafia.activities.RoomControlActivity;
import com.arekmaz.mafia.activities.ShowGameCharacter;
import com.arekmaz.mafia.entity.Player;
import com.arekmaz.mafia.enums.Role;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Connector {
    public static final String TAG = Connector.class.getSimpleName();

    private final static String HOST_ADDRESS = "192.168.43.1";
    private final static int PORT = 13378;

    public static Thread initServerThread(RoomControlActivity.PlayerActionsCallbacks callbacks) {
        Thread serverThread = createServerThread(callbacks);
        return serverThread;
    }

    private static Thread createServerThread(RoomControlActivity.PlayerActionsCallbacks callbacks) {
        return new Thread(() -> {
            Log.d(TAG, "starting server thread");
            try (ServerSocket server = new ServerSocket(PORT)) {
                while (true) {
                    Log.d(TAG, "loop cycle");
                    Socket client = server.accept();
                    Log.d(TAG, "connection accepted");
                    if (client != null) {
                        Log.d(TAG, "new client connection from " + client.getInetAddress());
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                        Log.d(TAG, "waiting for client message");
                        String newPlayerNick = in.readLine();
                        Log.d(TAG, "message from client: " + newPlayerNick);
                        Role newPlayerRole = callbacks.getNextPlayerRole();
                        Log.d(TAG, "writing to client: " + newPlayerRole.getRole());
                        out.println(newPlayerRole.getRole());
                        in.close();
                        out.close();
                        client.close();
                        String clientIp = client.getInetAddress().toString();
                        callbacks.onPlayerAdded(new Player(newPlayerNick, newPlayerRole, clientIp));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static Thread initClientThread(ShowGameCharacter.GameCharacterRequestCallbacks callback) {
        Thread clientThread = createClientThread(callback);
        return clientThread;
    }

    private static Thread createClientThread(ShowGameCharacter.GameCharacterRequestCallbacks callback) {
        return new Thread(() -> {
            Log.d(TAG, "starting client thread");
            try (Socket clientSocket = new Socket()) {
                clientSocket.connect(new InetSocketAddress(HOST_ADDRESS, PORT));
                Log.d(TAG, "connected to server");
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                String playerNick = callback.getPlayerNick();
                Log.d(TAG, "writing to server: " + playerNick);
                out.println(playerNick);
                out.flush();
                Log.d(TAG, "waiting for server response");
                String playerRole = in.readLine();
                Log.d(TAG, "server response: " + playerRole);
                callback.onRawCharacterRoleResult(playerRole);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
