package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientThread extends Thread {

    public static final int SIGNUP = 100;
    public static final int LOGIN = 101;
    public static final int SEND_MESSAGE = 102;
    public static final int PULL_MESSAGES = 103;
    public static final int PULL_ALLMESSAGES = 104;
    public static final int OKAY = 200;
    public static final int FAILURE = 201;

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Map<String, User> users;

    private static final Object lock = new Object();


    public ClientThread(Socket socket, Map<String, User> users) {
        this.socket = socket;
        this.users = users;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int action = inputStream.read();
            switch (action){
                case SIGNUP:
                    signup();
                    break;
                case LOGIN:
                    login();
                    break;
                case SEND_MESSAGE:
                    send();
                    break;
                case PULL_MESSAGES:
                    pull();
                    break;
                case PULL_ALLMESSAGES:
                    pullAll();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void signup() throws IOException {
        String userName = readString();
        String password = readString();
        if(userName.length() == 0 || password.length() == 0)
            return;
        boolean success = false;
        synchronized (lock) {
            if (!users.containsKey(userName)) {
                users.put(userName, new User(userName,password));
                success = true;
            }
        }
        outputStream.write(success ? OKAY : FAILURE);
    }


    private String validateUser() throws IOException{
        String userName = readString();
        String password = readString();
        if(userName.length() == 0 || password.length() == 0)
            return null;
        boolean valid = false;
        if(users.containsKey(userName)){
            String existingPassword = users.get(userName).getPassword();
            valid = existingPassword.equals(password);
        }
        return valid ? userName : null;
    }
    private void login() throws IOException {
        outputStream.write(validateUser() != null ? OKAY : FAILURE);
    }

    private void send() throws IOException {
        String userName = validateUser();
        if(userName == null)
            return;
        String userTo = readString();
        String content = readString();
        if(content.length() == 0)
            return;
        if(users.containsKey(userTo)){
            users.get(userTo).addMessageIn(new Message(userName,content));
            users.get(userName).addMessageOut(new Message(userTo,content));
            outputStream.write(OKAY);
        }
        else
            outputStream.write(FAILURE);
    }


    private void pull() throws IOException {
        String userName = validateUser();
        if(userName == null)
            return;
        String userFrom = readString();
        if(users.containsKey(userFrom)){
            outputStream.write(OKAY);
            List<Message> messages = users.get(userName).pullMessages(userFrom);
            writePulls(messages);
        }
        else
            outputStream.write(FAILURE);
    }

    private void pullAll() throws IOException {
        String userName = validateUser();
        if(userName == null)
            return;
        String userFrom = readString();
        System.out.println("pull All conversation: "+ userName+" & "+userFrom);
        if(users.containsKey(userFrom)){
            outputStream.write(OKAY);
            List<Message> messages = users.get(userName).pullAllMessages(userFrom);
            writePulls(messages);
        }
        else
            outputStream.write(FAILURE);
    }

    private void writePulls(List<Message> messages) throws IOException {
        if (messages == null)
            return;
        outputStream.write(messages.size());
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            writeString(message.getFrom());
            writeString(message.getContent());
            System.out.println(message);
        }
    }


    private String readString() throws IOException{
        int length = inputStream.read();
        if(length == -1)
            throw new IOException("expected string length");
        byte[] buffer = new byte[length];
        int actuallyRead = inputStream.read(buffer);
        if(actuallyRead != length)
            throw new IOException("expected " + length + " bytes.");
        return new String(buffer);
    }

    private int readInt() throws IOException{
        byte[] intBytes = new byte[4];
        int actuallyRead = inputStream.read(intBytes);
        if(actuallyRead != 4)
            throw new IOException("expected four bytes");
        return ByteBuffer.wrap(intBytes).getInt();
    }
    private void writeString(String s) throws IOException {
        byte[] bytes = s.getBytes();
        outputStream.write(bytes.length);
        outputStream.write(bytes);
    }


}
