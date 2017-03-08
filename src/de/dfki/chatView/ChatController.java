package de.dfki.chatView;

import de.dfki.reader.Reader;
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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
//import telecomchat.TelecomChat;

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

    private Reader telecomChat;

    private ObservableList<String> sessionobservableList;

//    private CSVReader csvReader;
//    private HashMap<String, Conversation> conversations;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
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
////                    int i = 0;
////                    for (Object objectLine : conversation.getMessages()) {
////                        ConversationLine line = (ConversationLine) objectLine;
////                        addUserDialog(i, line.getUserQuestion(), line);
////                        if (!line.getUserQuestion().equals("")) {
////                            i++;
////                        }
////                        addSystemDialog(i, line.getSystemResponse(), line);
////                        if (!line.getSystemResponse().equals("")) {
////                            i++;
//                        }
//                    }
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
////        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent event) {
////                sessionName.setText("openFileButton");
////                handleOpen();
////            }
////        });
//
////        nextButton.setOnAction(new EventHandler<ActionEvent>() {
////            @Override
////            public void handle(ActionEvent event) {
////                moveNext();
//////                setCurrentConversation();
//////                conversations.keySet();
////            }
//        });
//
//        previousButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                movePrevious();
//            }
//        });
//
//        showChatOverview();
//    }
//
//    private void movePrevious() {
//        if (current_position > 0) {
//            current_position -= 1;
//            sessionList.getSelectionModel().select(current_position);
//            if (current_position == 0) {
//                previousButton.setDisable(true);
//            }
//        } else {
//            previousButton.setDisable(true);
//        }
//    }

//    private void setCurrentPosition() {
//        current_position = sessionList.getSelectionModel().getSelectedIndex();
//        if (nextButton.isDisable() && current_position < sessionobservableList.size() - 1) {
//            nextButton.setDisable(false);
//        }
//        if (previousButton.isDisable() && current_position > 0) {
//            previousButton.setDisable(false);
//        }
//    }

//    private void setCurrentConversation() {
//    }

//    private void moveNext() {
//        if (current_position < sessionobservableList.size() - 1) {
//            current_position += 1;
//            sessionList.getSelectionModel().select(current_position);
//            if (current_position == sessionobservableList.size() - 1) {
//                nextButton.setDisable(true);
//            }
//        } else {
//            nextButton.setDisable(true);
//        }
    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(telecomChat.getPrimaryStage());
        if (file != null) {
            String filename = file.getAbsolutePath();
//            csvReader = new CSVReader(filename, ';');
//            csvReader.readFile();
//            conversations = csvReader.parse();
////            fillCombobox(conversations.keySet());
//            fillCombobox(conversations);
        }
    }

    public void setMainApp(Reader telecomChat) {
        this.telecomChat = telecomChat;
    }

//    public void fillCombobox(Collection<String> s) {
//    public void fillCombobox(HashMap<String, Conversation> conversations) {
//        List sessionaryLst = new ArrayList();
//        for (String sessionId : conversations.keySet()) {
//            int number = conversations.get(sessionId).getMessages().size();
//            sessionaryLst.add(sessionId + "  " + "( " + number + " Message(s) " + ")");
//        }
//        sessionobservableList = FXCollections.observableArrayList(sessionaryLst);
//
//        sessionList.getItems().clear();
//        sessionList.getItems().addAll(sessionobservableList);
//
//        sessionList.setValue(sessionobservableList.get(0));
//    }

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
        chatGridPane.getColumnConstraints().addAll(c0,c1,c2);
        chatGridPane.setVgap(10);
        screolPane.setContent(chatGridPane);
    }

    private void addUserDialog(int i, String dialog){
        if (!dialog.equals("")) {
            dialog = "User:" + "\n" + dialog;
            Label chatMessage = new Label(dialog);
//            chatMessage.setStyle("-fx-background-color: rgb(59, 89, 152);");
            chatMessage.setStyle("-fx-background-color: lightskyblue; -fx-alignment: left;");

            
            chatMessage.setAlignment(Pos.TOP_LEFT);

            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(1000);

            GridPane.setHalignment(chatMessage, HPos.LEFT);

//            chatMessage.getStyleClass().add("message-bubble-left");
//            chatMessage.setPadding(new Insets(0, 0, 0, 40));

//            chatGridPane.addRow(i, chatMessage);
            TextField userTopic = new TextField();
            userTopic.setId("userTopic" + i);
            TextField userValue = new TextField();
            userValue.setId("userValue" + i);
            
            chatGridPane.add(chatMessage, 0, i);
            chatGridPane.add(userTopic, 1, i);
            chatGridPane.add(userValue, 2, i);
        }
    }

//    private void renderMetadata(ConversationLine line, Label chatMessage) {
//        LinkedList<MetaDataObject> metadataObjects = line.getMetadataObjects();
//        for (MetaDataObject object : metadataObjects) {
//            object.render(chatMessage);
//        }
//    }

    private void addSystemDialog(int i, String dialog) {
        if (!dialog.equals("")) {
             dialog = "System:" + "\n" + dialog;
            Label chatMessage = new Label(dialog);
//            Pane test = new Pane();
//            test.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            
        chatMessage.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
//            
//            Separator s = new Separator();
//            s.setOrientation(Orientation.HORIZONTAL);
//            test.getChildren().add(chatMessage);
//            test.getChildren().add(s);
            chatMessage.setAlignment(Pos.TOP_LEFT);
            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(1000);
            GridPane.setHalignment(chatMessage, HPos.LEFT);
//            chatMessage.getStyleClass().add("message-bubble-right");
//            renderMetadata(line, chatMessage);
//            String text = chatMessage.getText() + "\n" + line.getTimestamp().toString();
            chatMessage.setText(chatMessage.getText());
            
            TextField systemTopic = new TextField();
            systemTopic.setId("systemTopic" + i);
            TextField systemValue = new TextField();
            systemValue.setId("systemValue" + i);
            
            chatGridPane.add(chatMessage,0,i);
            chatGridPane.add(systemTopic, 1, i);
            chatGridPane.add(systemValue, 2, i);
        }
    }
}
