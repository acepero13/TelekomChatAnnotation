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
    private LinkedList<Message> conversation = new LinkedList<>();
    private int defenseStrategy;


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
        info.add(line);
    }

    public String getSystemName(){
        return systemName;
    }

    public LinkedList<Message> getConversation() {
        return conversation;
    }

    public int getDefenseStrategy() {
        return defenseStrategy;
    }

    public void setDefenseStrategy(int defenseStrategy) {
        this.defenseStrategy = defenseStrategy;
    }
}
