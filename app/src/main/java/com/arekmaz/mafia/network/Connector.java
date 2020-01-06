package com.arekmaz.mafia.network;

import android.util.Log;

import com.arekmaz.mafia.activities.LiveRoomActivity;
import com.arekmaz.mafia.activities.RoomSetupActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Connector {
    private final static String TAG = "Connector";
    private final static String HOST_ADDRESS = "192.168.43.1";
    private final static int PORT = 13378;

    public static void gatherClients(int maxNoClients, LiveRoomActivity.NetworkObserver observer){
        Log.i(TAG, "Server starting...");
        new Thread(() -> {
        int clientsCounter = 0;
            try(ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(HOST_ADDRESS))){
                Log.i(TAG, "Server started.");
                while(clientsCounter <= maxNoClients){
                    Socket client = server.accept();
                    if(client != null){
                        clientsCounter++;
                        InetAddress clientAddress = client.getInetAddress();
                        Log.i(TAG, clientAddress.toString() + " connected.");
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String message = in.readLine();
                        Log.i(TAG, "Client:" + clientAddress.toString() + " :" + message);
                        observer.update(clientAddress, message);
                        in.close();
                        client.close();
                    }
                }
                Log.i(TAG, "Queue full.");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
