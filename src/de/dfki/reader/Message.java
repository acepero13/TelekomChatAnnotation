package de.dfki.reader;

/**
 * Created by alvaro on 3/8/17.
 */

public class Message {
    public enum Speaker {
        USER, AGENT
    }
    private final int counter;
    private final String text;
    private Speaker speaker;

    public Message(int counter, String line) {
        this.counter = counter;
        this.text = line;
    }

    public int getCounter() {
        return counter;
    }

    public String getText() {
        return text;
    }

    public void setSpeaker(Speaker speaker){
        this.speaker = speaker;
    }

    public Speaker getSpeaker(){
        return this.speaker;
    }
}
