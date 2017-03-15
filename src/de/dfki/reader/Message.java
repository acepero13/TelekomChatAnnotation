package de.dfki.reader;

/**
 * Created by alvaro on 3/8/17.
 */

public class Message implements Textable {
    private final int counter;
    private String text;
    private Speaker speaker;
    private int value;
    private int topic;
    private int defenseStrategy;
    public Message(int counter, String line) {
        this.counter = counter;
        this.text = line;
        this.topic = -1;
        this.value = -1;
        this.defenseStrategy = -1;
        parseTopicAndValue();
    }

    private void parseTopicAndValue() {
        if(countMatches(text, "|") == 3){
            int indexTopic = text.indexOf("|");
            int indexValue = text.indexOf("|", indexTopic + 1);
            int indexLast = text.indexOf("|", indexValue + 1);
            topic = Integer.parseInt(text.substring(indexTopic + 1, indexValue));
            value = Integer.parseInt(text.substring(indexValue + 1, indexLast));
            text = text.substring(0, indexTopic);
        }else if(countMatches(text, "|") == 4){
            int indexTopic = text.indexOf("|");
            int indexValue = text.indexOf("|", indexTopic + 1);
            int indexDefenseStrategy = text.indexOf("|", indexValue + 1);
            int indexLast = text.indexOf("|", indexDefenseStrategy + 1);
            topic = Integer.parseInt(text.substring(indexTopic + 1, indexValue));
            value = Integer.parseInt(text.substring(indexValue + 1, indexDefenseStrategy));
            defenseStrategy = Integer.parseInt(text.substring(indexDefenseStrategy +1, indexLast));
            text = text.substring(0, indexTopic);
        }
    }

    public static int countMatches(String text, String findStr){
        int lastIndex = 0;
        int count = 0;

        while(lastIndex != -1){

            lastIndex = text.indexOf(findStr,lastIndex);

            if(lastIndex != -1){
                count ++;
                lastIndex += findStr.length();
            }
        }
        return count;
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

    public int getDefenseStrategy() {
        return defenseStrategy;
    }

    @Override
    public void setDefenseStrategy(int value) {
        defenseStrategy = value;
    }

    public enum Speaker {
        USER, AGENT, INFO
    }
}
