package de.dfki.reader;

import de.dfki.reader.Message.Speaker;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by alvaro on 3/8/17.
 */
public class Reader {
    public static void main(String[] args)
    {
        TextReader reader = new TextReader("C:\\Users\\EmpaT\\Desktop\\Telekom-Chatt\\Archiv\\sessions_01-150316_output.txt_proc4.txt");
        File f = new File("C:\\Users\\EmpaT\\Desktop\\a.txt");
        
        reader.read();
        LinkedList<Conversation> conversations = reader.getConversations();
        for (Conversation c:conversations) {
            Writer.write("--------------------------\n", f);
            LinkedList<String> info = c.getInfo();
            for(String s : info)
            {
               Writer.write(s + "\n", f);
            }
            LinkedList<Message> messages = c.getConversation();
            for (Message m:messages  ) {
              
                String name = "";
                if(m.getSpeaker()== Speaker.AGENT)
                {
                    name = c.getSystemName();
                }else
                {
                    name = TextReader.USER_NAME;
                }
                
                Writer.write(name + ": " + m.getText() + "|" + m.getTopic() + "|" + m.getValue() + "\n", f);
//                Writer.write(name + ": " + m.getText() + "\n", f);
                  String text = m.getText();
                  String speaker = m.getSpeaker().toString();
                  System.out.println(speaker + ": " + text + "\n");

            }
        }
    }
}
