package de.dfki.chatView;

import de.dfki.reader.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    private Button saveAs;
    @FXML
    private Button saveFile;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button pinButton;
    @FXML
    private Button nextAnot;
    @FXML
    private Button prevAnot;
    @FXML
    private Button goTo;
    @FXML
    private ComboBox<String> sessionList;
    @FXML
    private ComboBox<String> sessionPinList;
    @FXML
    private TextField strategyField;
    @FXML
    private TextField goToField;

    private int current_position = 0;

    private GridPane chatGridPane;

    private TelekomChat telecomChat;

    private ObservableList<String> sessionobservableList;

    TextReader reader;

    LinkedList<Conversation> conversations;

    HashMap<String, String> annotation = new HashMap<>();
    private HashMap<String, Integer> pineList = new HashMap<>();
    private File file;
    String filename = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        sessionList.setOnAction((event) -> {
            current_position = Math.max(sessionList.getSelectionModel().getSelectedIndex(), 0);
            chatGridPane.getChildren().clear();
            addConversationIntoChatFrame(conversations, current_position);
        });

        sessionPinList.setOnAction((event) -> {
            if (sessionPinList.getValue() != null) {
                current_position = pineList.get(sessionPinList.getValue());
                sessionList.getSelectionModel().select(current_position);
                chatGridPane.getChildren().clear();
                addConversationIntoChatFrame(conversations, current_position);
            }
        });

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleOpen();
            }
        });

        pinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (conversations != null) {
                    Conversation conver = conversations.get(current_position);
                    if (!conver.isPinned()) {
                        conver.setPinned(true);
                        pinButton.setText("UnPin");
                        if (!pineList.containsKey(sessionobservableList.get(current_position))) {
                            pineList.put(sessionobservableList.get(current_position), current_position);
                            sessionPinList.getItems().add(sessionobservableList.get(current_position));
                            sessionPinList.setValue(sessionobservableList.get(current_position));
                        }
                    } else {
                        conver.setPinned(false);
                        pinButton.setText("Pin");
                        if (pineList.containsKey(sessionobservableList.get(current_position))) {
                            pineList.remove(sessionobservableList.get(current_position), current_position);
                            sessionPinList.setValue(null);
                            sessionPinList.getItems().remove(sessionobservableList.get(current_position));
                        }
                    }
                }
            }
        });

        saveAs.setOnAction((event) -> {
            saveAsAction();
        });


        saveFile.setOnAction(event -> {
            genericSave();
        });





        nextAnot.setOnAction((event) -> {
            if (conversations != null) {
                int next = reader.getNextUnAnnotatedConversation(current_position);
                if (next != -1 && next < conversations.size()) {
                    current_position = next;
                    sessionList.getSelectionModel().select(current_position);
                    if (conversations.get(current_position).getDefenseStrategy() == -1) {
                        strategyField.setText("");
                    }
                    addConversationIntoChatFrame(conversations, current_position);
                }
            }
        });

        prevAnot.setOnAction((event) -> {
            if (conversations != null) {
                int prev = reader.getPreviousUnAnnotatedConversation(current_position);
                if (prev != -1 && prev >= 0) {
                    current_position = prev;
                    sessionList.getSelectionModel().select(current_position);
                    if (conversations.get(current_position).getDefenseStrategy() == -1) {
                        strategyField.setText("");
                    }
                    addConversationIntoChatFrame(conversations, current_position);
                }
            }
        });

        strategyField.textProperty().addListener((observable, oldValue, newValue) -> {
            Conversation con = conversations.get(current_position);
            if (!newValue.isEmpty()) {
                con.setDefenseStrategy(Integer.parseInt(newValue));
            }
        });
        
        goTo.setOnAction((event) -> {
            goToConversation();

        });

        goToField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    goToConversation();
                }
            }
        });
        

        
        showChatOverview();



    }

    private void genericSave() {
        if(file == null){
            saveAsAction();
        }else{
            handleSave();
        }
    }

    private void saveAsAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TCA files (*.tca)", "*.tca");
        fileChooser.getExtensionFilters().add(extFilter);
        file = fileChooser.showSaveDialog(telecomChat.getPrimaryStage());
        handleSave();  
    }

    private void goToConversation() {
        String value = goToField.getText();
        if(value.equals("")){
            return;
        }

        try {
            int conversationNumber = Integer.valueOf(value) - 1;
            if(conversationNumber >= 0 && conversationNumber < conversations.size()){
                current_position = conversationNumber;
                moveToConversation();
            }
        }catch (NumberFormatException e){

        }

    }

    private void handleSave() {

        try {
            if (file != null) {
            Writer writer = new Writer(file);
                for (Conversation c : conversations) {
                    writer.write("--------------------------\n");
                    for (Textable t : c.getConversation()) {
                        if (t.getSpeaker() == Message.Speaker.INFO) {
                            String message = TextReader.INFO_LINE + " " + t.getText() + "\n";
                            writer.write(message);
                        } else if (t.getSpeaker() == Message.Speaker.USER) {
                            String message = TextReader.USER_NAME + ": " + t.getText() + "|" + t.getTopic() + "|" + t.getValue() + "|" + "\n";
                            writer.write(message);
                        } else {
                            String message = c.getSystemName() + ": " + t.getText() + "|" + t.getTopic() + "|" + t.getValue() + "|" + "\n";
                            writer.write(message);
                        }
                    }
                    if (c.isPinned()) {
                        writer.write("#" + c.getDefenseStrategy() + "#" + 1 + "\n");
                    } else {
                        writer.write("#" + c.getDefenseStrategy() + "#" + 0 + "\n");
                    }
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handleOpen() {
        current_position = 0;
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter tcaFilter = new FileChooser.ExtensionFilter("TCA files (*.tca)", "*.tca");
        fileChooser.getExtensionFilters().add(txtFilter);
        fileChooser.getExtensionFilters().add(tcaFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(telecomChat.getPrimaryStage());
        if (file != null) {
            filename = file.getAbsolutePath();
            reader = new TextReader(filename);
            reader.read();
            conversations = reader.getConversations();
            addFirstConversationIntoChatFrame(conversations);
            fillCombobox(conversations);
            if(filename.contains(".tca")){
                this.file = file;
            }
        }

        saveFile.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
            @Override
            public void run() {
                genericSave();
            }
        });
    }

    private void addFirstConversationIntoChatFrame(LinkedList<Conversation> conversations) {

        int messageCounter = 0;
        Conversation con = conversations.getFirst();
        if (con.getDefenseStrategy() >= 0) {
            strategyField.setText("" + con.getDefenseStrategy());
        }
        LinkedList<Textable> messages = con.getConversation();
        addConversationDialog(messageCounter, messages);
    }

    private void addConversationIntoChatFrame(LinkedList<Conversation> conversations, int conNummer) {
        int messageCounter = 0;
        Conversation nextCon = null;
        try {
            nextCon = conversations.get(current_position);
        } catch (IndexOutOfBoundsException e) {
            int a = 0;
        }

        if (!nextCon.isPinned()) {
            pinButton.setText("Pin");
        } else {
            pinButton.setText("UnPin");
        }

        if (nextCon.getDefenseStrategy() >= 0) {
            strategyField.setText("" + nextCon.getDefenseStrategy());
        }

        LinkedList<Textable> messages = nextCon.getConversation();
        addConversationDialog(messageCounter, messages);
    }

    private void addConversationDialog(int messageCounter, LinkedList<Textable> messages) {
        for (Textable m : messages) {
            if (m.getSpeaker() == Message.Speaker.AGENT) {
                String text = m.getText();
                addSystemDialog(messageCounter, text);
                messageCounter++;
            } else if (m.getSpeaker() == Message.Speaker.USER) {
                String text = m.getText();
                addUserDialog(messageCounter, text);
                messageCounter++;
            } else if (m.getSpeaker() == Message.Speaker.INFO) {
                addInfo(messageCounter, m.getText());
                messageCounter++;
            }

        }
    }

    public void setMainApp(TelekomChat reader) {
        this.telecomChat = reader;
    }

    public void fillCombobox(LinkedList<Conversation> conversations) {
        int conversationCounter = 1;

        pineList.clear();
        sessionPinList.setValue(null);
        sessionPinList.getItems().clear();

        List sessionaryLst = new ArrayList();
        for (Conversation c : conversations) {
            int messagesInConversation = c.getConversation().size();
            sessionaryLst.add(conversationCounter + "  " + "( " + messagesInConversation + " Message(s) " + ")");
            if (c.isPinned()) {
                int conlistNumber = conversationCounter - 1;
                String s = conversationCounter + "  " + "( " + messagesInConversation + " Message(s) " + ")";
                pineList.put(s, conlistNumber);
                sessionPinList.getItems().add(s);
            }
            conversationCounter++;
        }
        sessionobservableList = FXCollections.observableArrayList(sessionaryLst);

        sessionList.getItems().clear();
        sessionList.getItems().addAll(sessionobservableList);
        sessionList.setValue(sessionobservableList.get(0));

        nextButton.setOnMouseClicked((event) -> {
            chatGridPane.getChildren().clear();
            moveNext();
        });
        previousButton.setOnMouseClicked((event) -> {
            chatGridPane.getChildren().clear();
            movePrevious();
        });
    }

    private void moveNext() {
        previousButton.setDisable(false);
        if (current_position < sessionobservableList.size() - 1) {
            current_position++;
            moveToConversation();
        }else {
            nextButton.setDisable(true);
        }
    }

    private void moveToConversation() {

        sessionList.getSelectionModel().select(current_position);
        if (current_position == sessionobservableList.size() - 1) {
            nextButton.setDisable(true);
        }

        if (conversations.get(current_position).getDefenseStrategy() == -1) {
            strategyField.setText("");
        }
        addConversationIntoChatFrame(conversations, current_position);


    }

    private void movePrevious() {
        nextButton.setDisable(false);
        if (current_position > 0) {
            current_position--;
            sessionList.getSelectionModel().select(current_position);
            if (current_position == 0) {
                previousButton.setDisable(true);
            }

            if (conversations.get(current_position).getDefenseStrategy() == -1) {
                strategyField.setText("");
            }
            addConversationIntoChatFrame(conversations, current_position);
        } else {
            previousButton.setDisable(true);
        }
    }

    private void showChatOverview() {

        chatGridPane = new GridPane();
//        chatGridPane.setGridLinesVisible(true);
//        chatGridPane.getStyleClass().add("grid");
        ColumnConstraints agentCol = new ColumnConstraints();
        agentCol.setPercentWidth(10);
        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPercentWidth(60);
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(10);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(10);
        ColumnConstraints c3 = new ColumnConstraints();
        c2.setPercentWidth(10);
        chatGridPane.getColumnConstraints().addAll(agentCol, c0, c1, c2, c3);
        chatGridPane.setVgap(10);
        chatGridPane.setHgap(2);
        screolPane.setContent(chatGridPane);
    }

    private void addUserDialog(int i, String dialog) {
        if (!dialog.equals("")) {
            String userDialog = dialog;
            Label chatMessage = new Label(userDialog);
            Label speakerLabel = new Label("User");

            chatMessage.setMinHeight(35);
            chatMessage.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");
            speakerLabel.setStyle("-fx-font-weight: bold;");

            chatMessage.setAlignment(Pos.TOP_LEFT);

            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(3000);

            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.setPadding(new Insets(0, 10, 0, 0));

            TextField userTopic = new TextField();
            userTopic.setPrefWidth(100);
            userTopic.setPadding(new Insets(8, 0, 8, 0));
            Conversation c = conversations.get(current_position);
            int messagePositionWithoutInfo = i;
            if (messagePositionWithoutInfo >= 0 && c.getConversation().get(messagePositionWithoutInfo).getTopic() != -1) {
                userTopic.setText("" + c.getConversation().get(messagePositionWithoutInfo).getTopic());
            }
            userTopic.setId("userTopic" + i);

            TextField userValue = new TextField();
            userValue.setPrefWidth(100);
            userValue.setPadding(new Insets(8, 0, 8, 0));
            if (messagePositionWithoutInfo >= 0 && c.getConversation().get(messagePositionWithoutInfo).getValue() != -1) {
                userValue.setText("" + c.getConversation().get(messagePositionWithoutInfo).getValue());
            }

            userValue.setId("userValue" + i);

            Pane p1 = new Pane();
            p1.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");

            Pane p2 = new Pane();
            p2.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");

            Pane p3 = new Pane();
            p3.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");

            p1.getChildren().add(userTopic);
            p1.setPadding(new Insets(0, 50, 0, 50));
            p2.getChildren().add(userValue);
            p2.setPadding(new Insets(0, 50, 0, 50));
            p3.getChildren().add(speakerLabel);
            p3.setPadding(new Insets(0, 10, 0, 50));

            chatGridPane.add(p3, 0, i);
            chatGridPane.add(chatMessage, 1, i);
            chatGridPane.add(p1, 2, i);
            chatGridPane.add(p2, 3, i);

            userTopic.textProperty().addListener((observable, oldValue, newValue) -> {
                String message = "Sie: " + dialog + "|" + userTopic.getText() + "|" + userValue.getText();
                annotation.put(userTopic.getId(), message);
                if (messagePositionWithoutInfo >= 0 && !newValue.isEmpty()) {

                    c.getConversation().get(messagePositionWithoutInfo).setTopic(Integer.parseInt(newValue));
                }
            });

            userValue.textProperty().addListener((observable, oldValue, newValue) -> {
                String message = "Sie: " + dialog + "|" + userTopic.getText() + "|" + userValue.getText();
                annotation.put(userTopic.getId(), message);
                if (messagePositionWithoutInfo >= 0) {
                    c.getConversation().get(messagePositionWithoutInfo).setValue(Integer.parseInt(newValue));
                }
            });
        }
    }

    private void addSystemDialog(int i, String dialog) {
        if (!dialog.equals("")) {
            String systemDialog = dialog;
            Label chatMessage = new Label(systemDialog);
            Label speakerLabel = new Label("Agent");

            chatMessage.setMinHeight(35);
            chatMessage.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
            speakerLabel.setStyle("-fx-font-weight: bold;");

            chatMessage.setAlignment(Pos.TOP_LEFT);
            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(3000);
            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.setText(chatMessage.getText());

            chatMessage.setPadding(new Insets(0, 10, 0, 0));

            Pane p1 = new Pane();
            p1.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            Pane p2 = new Pane();
            p2.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            Pane p3 = new Pane();
            p3.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

            TextField systemTopic = new TextField();
            systemTopic.setPrefWidth(100);
            systemTopic.setPadding(new Insets(8, 0, 8, 0));
            Conversation c = conversations.get(current_position);
            int messagePositionWithoutInfo = i;
            if (messagePositionWithoutInfo >= 0 && c.getConversation().get(messagePositionWithoutInfo).getTopic() != -1) {
                systemTopic.setText("" + c.getConversation().get(messagePositionWithoutInfo).getTopic());
            }
            systemTopic.setId("systemTopic" + i);

            TextField systemValue = new TextField();
            systemValue.setPrefWidth(100);
            systemValue.setPadding(new Insets(8, 0, 8, 0));
            if (messagePositionWithoutInfo >= 0 && c.getConversation().get(messagePositionWithoutInfo).getValue() != -1) {
                systemValue.setText("" + c.getConversation().get(messagePositionWithoutInfo).getValue());
            }
            systemValue.setId("systemValue" + i);

            p1.getChildren().add(systemTopic);
            p1.setPadding(new Insets(0, 50, 0, 50));
            p2.getChildren().add(systemValue);
            p2.setPadding(new Insets(0, 50, 0, 50));
            p3.getChildren().add(speakerLabel);
            p3.setPadding(new Insets(0, 10, 0, 50));
            chatGridPane.add(p3, 0, i);
            chatGridPane.add(chatMessage, 1, i);
            chatGridPane.add(p1, 2, i);
            chatGridPane.add(p2, 3, i);

            systemTopic.textProperty().addListener((observable, oldValue, newValue) -> {
                String message = "{Name}: " + dialog + "|" + systemTopic.getText() + "|" + systemValue.getText();
                annotation.put(systemTopic.getId(), message);
                if (messagePositionWithoutInfo >= 0) {
                    c.getConversation().get(messagePositionWithoutInfo).setTopic(Integer.parseInt(newValue));
                }
            });

            systemValue.textProperty().addListener((observable, oldValue, newValue) -> {
                String message = "{Name}: " + dialog + "|" + systemTopic.getText() + "|" + systemValue.getText();
                annotation.put(systemTopic.getId(), message);
                if (messagePositionWithoutInfo >= 0) {
                    c.getConversation().get(messagePositionWithoutInfo).setValue(Integer.parseInt(newValue));
                }
            });
        }
    }

    private void addInfo(int i, String info) {
        if (!info.equals("")) {
            Label chatInfo = new Label(info);
            Label speakerLabel = new Label("Info");

//            chatInfo.setStyle("-fx-background-color: white; -fx-alignment: left;");
            speakerLabel.setStyle("-fx-font-weight: bold;");

            chatInfo.setAlignment(Pos.TOP_LEFT);

            chatInfo.setWrapText(true);
            chatInfo.setPrefWidth(1000);

            GridPane.setHalignment(chatInfo, HPos.LEFT);

            Pane p1 = new Pane();
//            p1.setStyle("-fx-background-color: white; -fx-alignment: left;");
            p1.getChildren().add(speakerLabel);
            p1.setPadding(new Insets(0, 10, 0, 50));

            chatGridPane.add(p1, 0, i);
            chatGridPane.add(chatInfo, 1, i);
        }
    }
}
