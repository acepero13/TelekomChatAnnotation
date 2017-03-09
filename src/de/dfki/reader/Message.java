package de.dfki.reader;

/**
 * Created by alvaro on 3/8/17.
 */

public class Message {
    private final int counter;
    private final String text;
    private Speaker speaker;
    private int value;
    private int topic;
    public Message(int counter, String line) {
        this.counter = counter;
        this.text = line;
        this.topic = -1;
        this.value = -1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public int getCounter() {
        return counter;
    }

    public String getText() {
        return text;
    }

    public Speaker getSpeaker(){
        return this.speaker;
    }

    public void setSpeaker(Speaker speaker){
        this.speaker = speaker;
    }

    public enum Speaker {
        USER, AGENT
    }
}
