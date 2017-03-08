package de.dfki.reader;

import java.util.LinkedList;

/**
 * Created by alvaro on 3/8/17.
 */
public class Reader {
    public static void main(String[] args)
    {
        TextReader reader = new TextReader("/home/alvaro/Documents/WorkHiwi/eLIZA/Archiv/sessions_01-150316_output.txt_proc4.txt");
        reader.read();
        LinkedList<Conversation> conversations = reader.getConversations();
        for (Conversation c:conversations) {
            LinkedList<Message> messages = c.getConversation();
            for (Message m:messages  ) {
                  String text = m.getText();
                  String speaker = m.getSpeaker().toString();
                  System.out.println(speaker + ": " + text + "\n");

            }
        }
    }
}
