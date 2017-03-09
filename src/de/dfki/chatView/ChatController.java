package de.dfki.chatView;

import de.dfki.reader.Conversation;
import de.dfki.reader.Message;
import de.dfki.reader.TelekomChat;
import de.dfki.reader.TextReader;
import java.io.File;
import java.net.URL;
import java.util.*;

//import de.dfki.csv.CSVReader;
//import de.dfki.csv.Conversation;
//import de.dfki.csv.ConversationLine;
//import de.dfki.csv.metadata.MetaDataObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Robbie
 */
public class ChatController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane screolPane;
    @FXML
    private Button openFileButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private ComboBox<String> sessionList;
    @FXML
    private TextField sessionName;

    private int current_position = 0;

    private GridPane chatGridPane;

    private TelekomChat telecomChat;

    private ObservableList<String> sessionobservableList;

    TextReader reader;

    LinkedList<Conversation> conversations;
    
    HashMap<String, String> annotation = new HashMap<>();

//    private CSVReader csvReader;
//    private HashMap<String, Conversation> conversations;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        // TODO
//        sessionList.setOnAction((event) -> {
//            String s = sessionList.getSelectionModel().getSelectedItem();
//            if (s != null) {
//                // set the setValue of combobox
////                setCurrentPosition();
//                chatGridPane.getChildren().clear();
//                String sessionId = sessionList.getSelectionModel().getSelectedItem();
//                String[] sessionId1 = sessionId.substring(0, sessionId.length() - 1).split("\\s++");
//                String sessionId0 = sessionId1[0];
//                if (!sessionId.equals("")) {
////                    Conversation conversation = conversations.get(sessionId0);
//                    int i = 0;
////                    for (Object objectLine : conversation.getMessages()) {
////                        ConversationLine line = (ConversationLine) objectLine;
////                        addUserDialog(i, line.getUserQuestion(), line);
////                        if (!line.getUserQuestion().equals("")) {
////                            i++;
////                        }
////                        addSystemDialog(i, line.getSystemResponse(), line);
////                        if (!line.getSystemResponse().equals("")) {
////                            i++;
////                        }
////                    }
////
////                    int countMessage = conversation.getMessages().size();
////                    sessionName.setText(s + "      " + countMessage + " Message(s)");
//
//                    sessionName.setText(s);
//                }
//
//            }
//        });
//
//        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                sessionName.setText("openFile");
//                handleOpen();
//            }
//        });
//
//        saveFileButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                FileChooser fileChooser = new FileChooser();
//                // Set extension filter
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
//                        "TXT files (*.txt)", "*.txt");
//                fileChooser.getExtensionFilters().add(extFilter);
//                File file = fileChooser.showSaveDialog(reader.getPrimaryStage());
//                if (file != null) {
//                    // Make sure it has the correct extension
//                    if (!file.getPath().endsWith(".txt")) {
//                        file = new File(file.getPath() + ".txt");
//                    }
//                }
//                
////                TextField tf = (TextField) chatGridPane.lookup("#"+"userTopic0");
////                String s = tf.getText();
////                System.out.println("****************   " + s);
//            }
//        });
//
//        nextButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////                moveNext();
//            }
//        });
//
//        previousButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////                movePrevious();
//            }
//        });
//
////        showChatOverview();

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleOpen();
            }
        });
        showChatOverview();
    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(telecomChat.getPrimaryStage());
        if (file != null) {
            String filename = file.getAbsolutePath();
            reader = new TextReader(filename);
            reader.read();
            conversations = reader.getConversations();
            addFirstConversationIntoChatFrame(conversations);
            fillCombobox(conversations);
        }
    }
    
    private void addFirstConversationIntoChatFrame(LinkedList<Conversation> conversations) {
        
        int messageCounter = 0;
        Conversation con = conversations.getFirst();

        LinkedList<String> info = con.getInfo();
        for (String s : info) {
            addInfo(messageCounter, s);
            messageCounter++;
        }
        LinkedList<Message> messages = con.getConversation();
        for (Message m : messages) {
            if (m.getSpeaker() == Message.Speaker.AGENT) {
                String text = m.getText();
                addSystemDialog(messageCounter, text);
                messageCounter++;
            } else {
                String text = m.getText();
                addUserDialog(messageCounter, text);
                messageCounter++;
            }
        }
    }
    
    private void addConversationIntoChatFrame(LinkedList<Conversation> conversations, int conNummer) {
        int messageCounter = 0;
        Conversation nextCon = conversations.get(current_position);
        LinkedList<String> info = nextCon.getInfo();
        for (String s : info) {
            addInfo(messageCounter, s);
            messageCounter++;
        }
        LinkedList<Message> messages = nextCon.getConversation();
        for (Message m : messages) {
            if (m.getSpeaker() == Message.Speaker.AGENT) {
                String text = m.getText();
                addSystemDialog(messageCounter, text);
                messageCounter++;
            } else {
                String text = m.getText();
                addUserDialog(messageCounter, text);
                messageCounter++;
            }
        }
    }

    public void setMainApp(TelekomChat reader) {
        this.telecomChat = reader;
    }

    public void fillCombobox(LinkedList<Conversation> conversations) {
        int conversationCounter = 0;
        List sessionaryLst = new ArrayList();
        for (Conversation c : conversations) {
            int messagesInConversation = c.getConversation().size();
            sessionaryLst.add(conversationCounter + "  " + "( " + messagesInConversation + " Message(s) " + ")");
            conversationCounter++;
        }
        sessionobservableList = FXCollections.observableArrayList(sessionaryLst);

        sessionList.getItems().clear();
        sessionList.getItems().addAll(sessionobservableList);
        sessionList.setValue(sessionobservableList.get(0));

        nextButton.setOnMouseClicked((event) -> {
            chatGridPane.getChildren().clear();
            moveNext(conversations);

        });
        previousButton.setOnMouseClicked((event) -> {
            chatGridPane.getChildren().clear();
            movePrevious();
        });
    }
    
    private void moveNext(LinkedList<Conversation> conversations) {
        previousButton.setDisable(false);
        if (current_position < sessionobservableList.size() - 1) {
            current_position++;
            sessionList.getSelectionModel().select(current_position);
            if (current_position == sessionobservableList.size() - 1) {
                nextButton.setDisable(true);
            }

            addConversationIntoChatFrame(conversations, current_position);

        } else {
            nextButton.setDisable(true);
        }
    }
    
    private void movePrevious() {
        nextButton.setDisable(false);
        if (current_position > 0) {
            current_position--;
            sessionList.getSelectionModel().select(current_position);
            if (current_position == 0) {
                previousButton.setDisable(true);
            }
            addConversationIntoChatFrame(conversations, current_position);
        } else {
            previousButton.setDisable(true);
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    private void showChatOverview() {

        chatGridPane = new GridPane();
//        chatGridPane.setGridLinesVisible(true);
//        chatGridPane.getStyleClass().add("grid");

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(100);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(20);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(20);
        chatGridPane.getColumnConstraints().addAll(c0, c1, c2);
        chatGridPane.setVgap(10);
        chatGridPane.setHgap(2);
        screolPane.setContent(chatGridPane);
    }

    private void addUserDialog(int i, String dialog) {
        if (!dialog.equals("")) {
            String userDialog = "User:" + "\n" + dialog;
            Label chatMessage = new Label(userDialog);
            chatMessage.setStyle("-fx-background-color: lightskyblue; -fx-alignment: left;");

            chatMessage.setAlignment(Pos.TOP_LEFT);

            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(1000);

            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.setPadding(new Insets(0, 10, 0, 20));

            TextField userTopic = new TextField();
            userTopic.setPrefWidth(100);
            userTopic.setPadding(new Insets(8, 0, 8, 0));
            Conversation c = conversations.get(current_position);
            int messagePositionWithoutInfo = i - c.getInfo().size();
            if(messagePositionWithoutInfo >=0)
                userTopic.setText(""+c.getConversation().get(messagePositionWithoutInfo).getTopic());
            userTopic.setId("userTopic" + i);

            TextField userValue = new TextField();
            userValue.setPrefWidth(100);
            userValue.setPadding(new Insets(8, 0, 8, 0));
            if(messagePositionWithoutInfo >=0)
                userValue.setText(""+c.getConversation().get(messagePositionWithoutInfo).getValue());
            userValue.setId("userValue" + i);

            Pane p1 = new Pane();
            p1.setStyle("-fx-background-color: lightskyblue; -fx-alignment: left;");

            Pane p2 = new Pane();
            p2.setStyle("-fx-background-color: lightskyblue; -fx-alignment: left;");

            p1.getChildren().add(userTopic);
            p1.setPadding(new Insets(0, 50, 0, 50));
            p2.getChildren().add(userValue);
            p2.setPadding(new Insets(0, 50, 0, 50));

            chatGridPane.add(chatMessage, 0, i);
            chatGridPane.add(p1, 1, i);
            chatGridPane.add(p2, 2, i);
            
            userTopic.textProperty().addListener((observable, oldValue, newValue) -> {
                    String message = "Sie: " + dialog + "|" + userTopic.getText() + "|" + userValue.getText();
                    annotation.put(userTopic.getId(), message);
                    if(messagePositionWithoutInfo >=0)
                        c.getConversation().get(messagePositionWithoutInfo).setTopic(Integer.parseInt(newValue));
            });
            
            userValue.textProperty().addListener((observable, oldValue, newValue) -> {
                    String message = "Sie: " + dialog + "|" + userTopic.getText() + "|" + userValue.getText();
                    annotation.put(userTopic.getId(), message);
                    if(messagePositionWithoutInfo >=0)
                        c.getConversation().get(messagePositionWithoutInfo).setValue(Integer.parseInt(newValue));
            });
        }
    }

    private void addSystemDialog(int i, String dialog ) {
        if (!dialog.equals("")) {
            String systemDialog = "System:" + "\n" + dialog;
            Label chatMessage = new Label(systemDialog);
            chatMessage.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            chatMessage.setAlignment(Pos.TOP_LEFT);
            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(1000);
            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.setText(chatMessage.getText());

            chatMessage.setPadding(new Insets(0, 10, 0, 20));

            Pane p1 = new Pane();
            p1.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            Pane p2 = new Pane();
            p2.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            TextField systemTopic = new TextField();
            systemTopic.setPrefWidth(100);
            systemTopic.setPadding(new Insets(8, 0, 8, 0));
            systemTopic.setId("systemTopic" + i);

            TextField systemValue = new TextField();
            systemValue.setPrefWidth(100);
            systemValue.setPadding(new Insets(8, 0, 8, 0));
            systemValue.setId("systemValue" + i);

            p1.getChildren().add(systemTopic);
            p1.setPadding(new Insets(0, 50, 0, 50));
            p2.getChildren().add(systemValue);
            p2.setPadding(new Insets(0, 50, 0, 50));
            chatGridPane.add(chatMessage, 0, i);
            chatGridPane.add(p1, 1, i);
            chatGridPane.add(p2, 2, i);
            
            systemTopic.textProperty().addListener((observable, oldValue, newValue) -> {
                    String message = "{Name}: " + dialog + "|" + systemTopic.getText() + "|" + systemValue.getText();
                    annotation.put(systemTopic.getId(), message);
                    System.out.println(annotation);
            });
            
            systemValue.textProperty().addListener((observable, oldValue, newValue) -> {
                    String message = "{Name}: " + dialog + "|" + systemTopic.getText() + "|" + systemValue.getText();
                    annotation.put(systemTopic.getId(), message);
                    System.out.println(annotation);
            });
        }
    }
    
    private void addInfo(int i, String info) {
        if (!info.equals("")) {
            info = "Info:" + "\n" + info;
            Label chatInfo = new Label(info);

            chatInfo.setStyle("-fx-background-color: azure; -fx-alignment: left;");

            chatInfo.setAlignment(Pos.TOP_LEFT);

            chatInfo.setWrapText(true);
            chatInfo.setPrefWidth(1000);

            GridPane.setHalignment(chatInfo, HPos.LEFT);

//            chatMessage.getStyleClass().add("message-bubble-left");
//            chatMessage.setPadding(new Insets(0, 0, 0, 40));
//            chatGridPane.addRow(i, chatMessage);
//            TextField userTopic = new TextField();
//            userTopic.setId("userTopic" + i);
//            TextField userValue = new TextField();
//            userValue.setId("userValue" + i);
            chatGridPane.add(chatInfo, 0, i);
//            chatGridPane.add(userTopic, 1, i);
//            chatGridPane.add(userValue, 2, i);
        }
    }
}
