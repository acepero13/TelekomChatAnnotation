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
    private Button saveFileButton;
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

    private ObservableList<String> sessionobservableList;

//    private CSVReader csvReader;
//    private HashMap<String, Conversation> conversations;
    private Reader reader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        sessionList.setOnAction((event) -> {
            String s = sessionList.getSelectionModel().getSelectedItem();
            if (s != null) {
                // set the setValue of combobox
//                setCurrentPosition();
                chatGridPane.getChildren().clear();
                String sessionId = sessionList.getSelectionModel().getSelectedItem();
                String[] sessionId1 = sessionId.substring(0, sessionId.length() - 1).split("\\s++");
                String sessionId0 = sessionId1[0];
                if (!sessionId.equals("")) {
//                    Conversation conversation = conversations.get(sessionId0);
                    int i = 0;
//                    for (Object objectLine : conversation.getMessages()) {
//                        ConversationLine line = (ConversationLine) objectLine;
//                        addUserDialog(i, line.getUserQuestion(), line);
//                        if (!line.getUserQuestion().equals("")) {
//                            i++;
//                        }
//                        addSystemDialog(i, line.getSystemResponse(), line);
//                        if (!line.getSystemResponse().equals("")) {
//                            i++;
//                        }
//                    }
//
//                    int countMessage = conversation.getMessages().size();
//                    sessionName.setText(s + "      " + countMessage + " Message(s)");

                    sessionName.setText(s);
                }

            }
        });

        openFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sessionName.setText("openFile");
                handleOpen();
            }
        });

        saveFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                // Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                        "TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(reader.getPrimaryStage());
                if (file != null) {
                    // Make sure it has the correct extension
                    if (!file.getPath().endsWith(".txt")) {
                        file = new File(file.getPath() + ".txt");
                    }
                }
                
//                TextField tf = (TextField) chatGridPane.lookup("#"+"userTopic0");
//                String s = tf.getText();
//                System.out.println("****************   " + s);
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                moveNext();
            }
        });

        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                movePrevious();
            }
        });

//        showChatOverview();
    }

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
//    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(reader.getPrimaryStage());
        if (file != null) {
            String filename = file.getAbsolutePath();
//            csvReader = new CSVReader(filename, ';');
//            csvReader.readFile();
//            conversations = csvReader.parse();
//            fillCombobox(conversations);
        }
    }

    public void setMainApp(Reader reader) {
        this.reader = reader;
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
            dialog = "User:" + "\n" + dialog;
            Label chatMessage = new Label(dialog);
            chatMessage.setStyle("-fx-background-color: lightskyblue; -fx-alignment: left;");

            chatMessage.setAlignment(Pos.TOP_LEFT);

            chatMessage.setWrapText(true);
            chatMessage.setPrefWidth(1000);

            GridPane.setHalignment(chatMessage, HPos.LEFT);
            chatMessage.setPadding(new Insets(0, 10, 0, 20));

            TextField userTopic = new TextField();
            userTopic.setPrefWidth(100);
            userTopic.setPadding(new Insets(8, 0, 8, 0));
            userTopic.setId("userTopic" + i);

            TextField userValue = new TextField();
            userValue.setPrefWidth(100);
            userValue.setPadding(new Insets(8, 0, 8, 0));
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
        }
    }

//    private void renderMetadata(ConversationLine line, Label chatMessage) {
//        LinkedList<MetaDataObject> metadataObjects = line.getMetadataObjects();
//        for (MetaDataObject object : metadataObjects) {
//            object.render(chatMessage);
//        }
//    }

    //////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    private void addSystemDialog(int i, String dialog ) {
        if (!dialog.equals("")) {
            dialog = "System:" + "\n" + dialog;
            Label chatMessage = new Label(dialog);
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
        }
    }
}
