package de.dfki.reader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alvaro on 3/8/17.
 */
public class TextReader {
    public static final String INFO_LINE = "info:";
    public static final String USER_NAME = "Sie";
    private final String filename;
    private String systemName = "{Name}";
    private LinkedList<Conversation> conversations = new LinkedList<>();

    public TextReader(String filename) {
        this.filename = filename;
    }

    public void read() {
        String line;
        int counter = 0;
        Conversation conversation = new Conversation();
        try (
                InputStream fis = new FileInputStream(this.filename);
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                if(isNewConversation(line, "------")){
                    counter = 0;
                    conversation = new Conversation();
                    conversations.add(conversation);
                }
                if (line.startsWith(INFO_LINE) && isNameSet(line)){
                    NameRegexFinder regexFinder = new NameRegexFinder();
                    String name = regexFinder.parse(line);
                    conversation.setSystemName(name);
                    conversation.addInfoLine(line);
                }else if(line.startsWith(INFO_LINE)){
                    conversation.addInfoLine(line);
                }else if(line.startsWith(USER_NAME)){
                    String text = getLineText(USER_NAME, line);
                    Message m = new Message(counter, text);
                    conversation.addUserLine(m);
                    counter++;
                }else if(line.startsWith(conversation.getSystemName())){
                    String text = getLineText(conversation.getSystemName(), line);
                    Message m = new Message(counter, text);
                    conversation.addSystemLine(m);
                    counter++;
                } else if(line.startsWith("#")){
                    parseDefenseAndPin(conversation, line);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int a = 0;

    }

    private void parseDefenseAndPin(Conversation conversation, String text) {
        if(Message.countMatches(text,"#") == 2){
            int indexDefense = text.indexOf("#");
            int indexPin = text.indexOf("#", indexDefense + 1);
            int defenseStrategy = Integer.parseInt(text.substring(indexDefense + 1, indexPin));
            int pined = Integer.parseInt(text.substring(indexPin + 1));
            conversation.setDefenseStrategy(defenseStrategy);
            if(pined == 1){
                conversation.setPinned(true);
            }else {
                conversation.setPinned(false);
            }
        }
        else if(Message.countMatches(text,"#") == 3){
            int indexDefense = text.indexOf("#");
            int indexPin = text.indexOf("#", indexDefense + 1);
            int indexAssesment = text.indexOf("#", indexPin +1);
            int defenseStrategy = Integer.parseInt(text.substring(indexDefense + 1, indexPin));
            if(indexAssesment == -1){
                indexAssesment = text.length();
            }
            int pined = Integer.parseInt(text.substring(indexPin + 1, indexAssesment));
            conversation.setDefenseStrategy(defenseStrategy);
            if(pined == 1){
                conversation.setPinned(true);
            }else {
                conversation.setPinned(false);
            }


            int assesment = -1;
            if(indexAssesment != -1){
                assesment = Integer.parseInt(text.substring(indexAssesment +1));

            }
            conversation.setOveralAssesment(assesment);
        }

    }


    public int getNextUnAnnotatedConversation(int current_position){
        int pos = -1;
        int i = current_position + 1;
        while (i < conversations.size() && pos < 0){
            Conversation c = conversations.get(i);
            if(!c.isAnnotated()){
                pos = i;
            }
            i++;
        }
        return pos;
    }

    public int getPreviousUnAnnotatedConversation(int current_position){
        int pos = -1;
        int i = current_position -1;
        while (i > 0 && pos < 0){
            Conversation c = conversations.get(i);
            if(!c.isAnnotated()){
                pos = i;
            }
            i--;
        }
        return pos;
    }

    public LinkedList<Conversation> getConversations(){
        return conversations;
    }

    private boolean isNewConversation(String line, String s) {
        return line.contains(s);
    }

    private String getLineText(String name, String line) {
        int i = line.indexOf(name);
        int index = 0;
        if(i >= 0){
            index = index + name.length();
        }
        if(index > 0 && index < line.length() &&  line.charAt(index ) == ':'){
            return line.substring(index +1);
        }else if(index > 0){
            return line.substring(index );
        }
        return line;
    }

    private boolean isNameSet(String line) {
        return line.contains("mein Name ist");
    }


    private class NameRegexFinder
    {
        public String parse(String txt)
        {

            String re1=".*?";	// Non-greedy match on filler
            String re2="(\\{)";	// Any Single Character 1
            String re3="((?:[a-z][a-z]+))";	// Word 1
            String re4="(-)*";	// Any Single Character 2
            String re5="((?:[a-z][a-z]+))";	// Word 2
            String re6="(\\})";	// Any Single Character 3

            Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(txt);
            String name = "";
            if (m.find())
            {
                String c1=m.group(1);
                String word1=m.group(2);
                String c2=m.group(3);
                String word2=m.group(4);
                String c3=m.group(5);
                if(c2 != null){
                    name = c1 + word1 + c2 + word2 + c3;
                }else{
                    name = c1 + word1 + word2 + c3;
                }
                return name;
            }
            return "";
        }
    }


}


