package de.dfki.reader;

/**
 * Created by alvaro on 3/9/17.
 */
public class InfoLine implements Textable{
    private  int counter;
    private String text = "";
    public InfoLine(String line) {
        text = line;
    }

    public InfoLine(int counter, String line) {
        text = line;
        this.counter = counter;
    }



    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public void setValue(int value) {

    }

    @Override
    public int getTopic() {
        return 0;
    }

    @Override
    public void setTopic(int topic) {

    }

    @Override
    public int getCounter() {
        return this.counter;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Message.Speaker getSpeaker() {
        return Message.Speaker.INFO;
    }

    @Override
    public int getDefenseStrategy() {
        return 0;
    }

    @Override
    public void setDefenseStrategy(int value) {

    }
}
