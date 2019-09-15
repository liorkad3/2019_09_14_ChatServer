package com.company;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String username;
    private ArrayList<Message> messages;
    private int unReadIndex;

    public Chat(String username) {
        this.username = username;
        messages = new ArrayList<>();
        unReadIndex = 0;
    }

    public List<Message> pullMessages(int index){
        if (messages.size() == 0 || index == messages.size())
            return null;
        ArrayList<Message> pulls = new ArrayList<>();
        for (int i = index; i <messages.size() ; i++) {
            pulls.add(messages.get(i));
        }
        unReadIndex = messages.size();
        return pulls;
    }

    public List<Message> pullUnreadMessages(){
        return pullMessages(unReadIndex);
    }

    public List<Message> pullAllMessages(){
        return pullMessages(0);
    }


    public void inMessage(Message message){
        messages.add(message);
    }

    public void outMessage(String content){
        messages.add(new Message("me",content));
        unReadIndex = messages.size();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public int getUnReadIndex() {
        return unReadIndex;
    }

    public void setUnReadIndex(int unReadIndex) {
        this.unReadIndex = unReadIndex;
    }
}
