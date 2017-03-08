package de.dfki.reader;

import de.dfki.chatView.ChatController;
import de.dfki.chatView.TelecomChat;
import de.dfki.reader.Message.Speaker;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by alvaro on 3/8/17.
 */
public class Reader extends Application{

    
//    public static void main(String[] args)
//    {
////        TextReader reader = new TextReader("C:\\Users\\EmpaT\\Desktop\\Telekom-Chatt\\Archiv\\sessions_01-150316_output.txt_proc4.txt");
////        File f = new File("C:\\Users\\EmpaT\\Desktop\\a.txt");
//        TextReader reader = new TextReader("/Users/Robbie/Documents/eLIZA/Archiv/sessions_01-150116_output_final.txt-dictentries.txt");
//        File f = new File("/Users/Robbie/Documents/test.txt");
//        reader.read();
//        LinkedList<Conversation> conversations = reader.getConversations();
//        for (Conversation c:conversations) {
//            Writer.write("--------------------------\n", f);
//            LinkedList<String> info = c.getInfo();
//            for(String s : info)
//            {
//               Writer.write(s + "\n", f);
//            }
//            LinkedList<Message> messages = c.getConversation();
//            for (Message m:messages  ) {
//              
//                String name = "";
//                if(m.getSpeaker()== Speaker.AGENT)
//                {
//                    name = c.getSystemName();
//                }else
//                {
//                    name = TextReader.USER_NAME;
//                }
//                
//                Writer.write(name + ": " + m.getText() + "|" + m.getTopic() + "|" + m.getValue() + "\n", f);
////                Writer.write(name + ": " + m.getText() + "\n", f);
//                  String text = m.getText();
//                  String speaker = m.getSpeaker().toString();
//                  System.out.println(speaker + ": " + text + "\n");
//
//            }
//        }
//    }
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chat");

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TelecomChat.class.getResource("Chat.fxml"));
            rootLayout = (AnchorPane) loader.load();
            
            // Give the controller access to the main app.
            ChatController controller = loader.getController();
            controller.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
