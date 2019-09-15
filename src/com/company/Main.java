package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {


    public static final int PORT = 3000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Map<String, User> users = new HashMap<>();
        try {
            serverSocket = new ServerSocket(PORT);
            while (true){
                Socket socket = serverSocket.accept();
                new ClientThread(socket, users).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
