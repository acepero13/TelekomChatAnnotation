package de.dfki.reader;

/**
 * Created by alvaro on 3/9/17.
 */
public interface Textable {
    int getValue() ;
    void setValue(int value);
    int getTopic();
    void setTopic(int topic);
    int getCounter();
    String getText();
    Message.Speaker getSpeaker();
    int getDefenseStrategy();
    void setDefenseStrategy(int value);

}
