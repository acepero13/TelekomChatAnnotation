package de.dfki.reader;

import java.util.LinkedList;

/**
 * Created by alvaro on 3/8/17.
 */
public class Conversation {
    private  String systemName = "Not a line to read";
    private LinkedList<Message> user = new LinkedList<>();
    private LinkedList<Message> system = new LinkedList<>();
    private LinkedList<String> info = new LinkedList<>();
    private LinkedList<Textable> conversation = new LinkedList<>();
    private boolean pinned = false;

    private int defenseStrategy;

    public boolean isAnnotated(){
        boolean isAnnotated = true;
        int i = 0;
        while(i< conversation.size() && isAnnotated){
            Textable conversationMessages = conversation.get(i);
            if(conversationMessages.getValue() < 0 || conversationMessages.getTopic() < 0){
                isAnnotated = false;
            }
            i++;
        }
        return isAnnotated;
    }


    public Conversation(String systemName){
        this.systemName = systemName;
    }

    public Conversation(){

    }

    public void setSystemName(String name){
        this.systemName  = name;
    }

    public void addUserLine(Message line){
        line.setSpeaker(Message.Speaker.USER);
        user.add(line);
        getConversation().add(line);
    }

    public void addSystemLine(Message line){
        line.setSpeaker(Message.Speaker.AGENT);
        system.add(line);
        getConversation().add(line);
    }

    public void addInfoLine(String line){
        Textable infoLine = new InfoLine(line);
        info.add(line);
        conversation.add(infoLine);
    }

    public String getSystemName(){
        return systemName;
    }

    public LinkedList<Textable> getConversation() {
        return conversation;
    }

    public int getDefenseStrategy() {
        return defenseStrategy;
    }

    public void setDefenseStrategy(int defenseStrategy) {
        this.defenseStrategy = defenseStrategy;
    }

    public LinkedList<String> getInfo() {
        return info; //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
