package com.company;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

public class User {
    private String userName;
    private String password;
    private HashMap<String,Chat> chats;


    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        chats = new HashMap<>();
//        messagesIn = new ArrayList<>();
//        messagesOut = new ArrayList<>();
    }

    public List<Message> pullMessages(String userFrom){
        if (chats.containsKey(userFrom))
            return chats.get(userFrom).pullUnreadMessages();
        return null;
    }

    public List<Message> pullAllMessages(String userFrom){
        if (chats.containsKey(userFrom))
            return chats.get(userFrom).pullAllMessages();
        return null;
    }

    public void addChat(String userName){
        chats.put(userName, new Chat(userName));
    }

    public void addMessageIn(Message message){
        String userFrom = message.getFrom();
        if(!chats.containsKey(userFrom))
            addChat(userFrom);
        chats.get(userFrom).inMessage(message);
    }

    public void addMessageOut(Message message){
        String userTo = message.getFrom();
        if(!chats.containsKey(userTo))
            addChat(userTo);
        chats.get(userTo).outMessage(message.getContent());
    }

    public HashMap<String, Chat> getChats() {
        return chats;
    }

    public void setChats(HashMap<String, Chat> chats) {
        this.chats = chats;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public ArrayList<Message> getMessagesIn() {
//        return messagesIn;
//    }
//
//    public void setMessagesIn(ArrayList<Message> messagesIn) {
//        this.messagesIn = messagesIn;
//    }

//    public ArrayList<Message> getMessagesOut() {
//        return messagesOut;
//    }
//
//    public void setMessagesOut(ArrayList<Message> messagesOut) {
//        this.messagesOut = messagesOut;
//    }
//
//    public void putMessageIn(Message message){
//        messagesIn.add(message);
//    }
//
//    public void putMessageOut(Message message){
//        messagesOut.add(message);
//    }


//    public List<Message> unreadMessages(){
//        if (unReadMessages == messagesIn.size())
//            return null;
//        List<Message> messages = messagesIn.subList(unReadMessages,messagesIn.size());
//        unReadMessages = messagesIn.size();
//        return messages;
//    }

}
