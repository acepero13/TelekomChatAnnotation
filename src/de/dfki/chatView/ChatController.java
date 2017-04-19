package de.dfki.chatView;

import de.dfki.chatView.defenceStrategy.ChatManagerDefenceStrategy;
import de.dfki.chatView.renderers.InfoRenderer;
import de.dfki.chatView.renderers.SystemRenderer;
import de.dfki.chatView.renderers.UserRenderer;
import de.dfki.eliza.chat.ChatManager;
import de.dfki.eliza.chat.decorators.ChatManagerDecorator;
import de.dfki.eliza.chat.decorators.UnAnnotatedConversationDecorator;
import de.dfki.eliza.chat.decorators.factories.ChatManagerAbstractFactory;
import de.dfki.eliza.files.exceptions.NoValidConversation;
import de.dfki.eliza.files.filestystem.FileSystemReadable;
import de.dfki.eliza.files.filestystem.eliza.ElizaFileSystem;
import de.dfki.eliza.files.filestystem.eliza.ElizaWriter;
import de.dfki.eliza.files.models.Conversation;
import de.dfki.eliza.files.readers.eliza.ElizaReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Robbie
 */
public class ChatController implements Initializable {

    ElizaReader reader;
    ChatManager chatManager;
    UnAnnotatedConversationDecorator unAnnotatedManager;
    String filename = "";
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
    @FXML
    private ComboBox<String> assessmentCombo;
    @FXML
    private TextField assessmentTextField;
    @FXML
    private ComboBox<String> assessmentResultCombo;
    @FXML
    private ComboBox<String> defenceStrategyCombo;
    @FXML
    private ComboBox<String> defenceStrategyResultCombo;
    @FXML
    private MenuItem fileOpenItem;
    @FXML
    private MenuItem fileSaveItem;
    @FXML
    private MenuItem fileSaveAs;
    @FXML
    private Button exportButton;
    private int current_position = 0;
    private GridPane chatGridPane;
    private TelekomChat telecomChat;
    private ObservableList<String> sessionobservableList;
    private Conversation conversation;
    private HashMap<String, Integer> pineList = new HashMap<>();
    private File file;

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        assessmentCombo.setOnAction((event) -> {
            int itemIndex = assessmentCombo.getSelectionModel().getSelectedIndex();
            List assesmentList = new ArrayList();
            ChatManagerDecorator decoratedChatManager = createAssessmentDecorator(itemIndex);
            boolean hasNext = true;
            while (hasNext) {
                try {
                    decoratedChatManager.getNextConversation();
                    assesmentList.add(decoratedChatManager.getCurrentPosition() + 1 + "");
                } catch (NoValidConversation noValidConversation) {
                    hasNext = false;
                }
            }
            assessmentResultCombo.getItems().clear();
            assessmentResultCombo.getItems().addAll(FXCollections.observableArrayList(assesmentList));
        });

        defenceStrategyCombo.setOnAction((event) -> {
            int itemIndex = defenceStrategyCombo.getSelectionModel().getSelectedIndex();
            List defenceStrategyList = new ArrayList();
            ChatManagerDecorator decoratedChatManager = createdeDenceStrategyDecorator(itemIndex);
            boolean hasNext = true;
            while (hasNext) {
                try {
                    decoratedChatManager.getNextConversation();
                    defenceStrategyList.add(decoratedChatManager.getCurrentPosition() + 1 + "");
                } catch (NoValidConversation noValidConversation) {
                    hasNext = false;
                }
            }
            defenceStrategyResultCombo.getItems().clear();
            defenceStrategyResultCombo.getItems().addAll(FXCollections.observableArrayList(defenceStrategyList));
        });

        assessmentResultCombo.setOnAction((event) -> {
            if (assessmentResultCombo.getValue() != null) {
                int index = Integer.parseInt(assessmentResultCombo.getValue().trim());

                current_position = index - 1;
                try {
                    conversation = chatManager.goToConversation(current_position+1);
                } catch (NoValidConversation ex) {
                    Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                sessionList.getSelectionModel().select(current_position);
            }
        });

        defenceStrategyResultCombo.setOnAction((event) -> {
            if (defenceStrategyResultCombo.getValue() != null) {
                int index = Integer.parseInt(defenceStrategyResultCombo.getValue().trim());

                current_position = index - 1;
                try {
                    conversation = chatManager.goToConversation(current_position+1);
                } catch (NoValidConversation ex) {
                    Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                sessionList.getSelectionModel().select(current_position);
            }
        });

        sessionList.setOnAction((event) -> {
            current_position = Math.max(sessionList.getSelectionModel().getSelectedIndex(), 0);
            chatGridPane.getChildren().clear();
            emptyConversationFields();
            addConversationIntoChatFrame();
        });

        sessionPinList.setOnAction((event) -> {
            if (sessionPinList.getValue() != null) {
                int position = pineList.get(sessionPinList.getValue());
                try {
                    conversation = chatManager.goToConversation(position + 1);
                } catch (NoValidConversation noValidConversation) {
                    noValidConversation.printStackTrace();
                }
                sessionList.getSelectionModel().select(position);
                chatGridPane.getChildren().clear();
                emptyConversationFields();
                addConversationIntoChatFrame();


            }
        });

        fileOpenItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleOpen();
            }
        });

        pinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (conversation != null) {

                    if (!conversation.isPinned()) {
                        conversation.setPinned(true);
                        pinButton.setText("UnPin");
                        if (!pineList.containsKey(sessionobservableList.get(current_position))) {
                            pineList.put(sessionobservableList.get(current_position), current_position);
                            sessionPinList.getItems().add(sessionobservableList.get(current_position));
                            sessionPinList.setValue(sessionobservableList.get(current_position));
                        }
                    } else {
                        conversation.setPinned(false);
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

        fileSaveAs.setOnAction((event) -> {
            saveAsAction();
        });

        fileSaveItem.setOnAction(event -> {
            genericSave();
        });

        nextAnot.setOnAction((event) -> {
            try {
                conversation = unAnnotatedManager.getNextConversation();
                updateValues();
            } catch (NoValidConversation noValidConversation) {
                noValidConversation.printStackTrace();
            }
        });

        prevAnot.setOnAction((event) -> {
            try {
                conversation = unAnnotatedManager.getPreviousConversation();
                updateValues();
            } catch (NoValidConversation noValidConversation) {
                noValidConversation.printStackTrace();
            }

        });

        strategyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                conversation.setDefenseStrategy(Integer.parseInt(newValue));
            }
        });

        goTo.setOnAction((event) -> {
            goToConversation();

        });

        goToField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    goToConversation();
                }
            }
        });

        assessmentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isNumeric(newValue) && !newValue.isEmpty()) {
                conversation.setOveralAssesment(Integer.parseInt(newValue));
            }

        });

        exportButton.setOnAction((event) -> {
            handleExportButton();
        });

        fillAssessmentCombo();
        fillDefenceStrategyCombo();
        showChatOverview();

    }

    private ChatManagerDecorator createdeDenceStrategyDecorator(int itemIndex) {
        return ChatManagerDefenceStrategy.createChatManager(itemIndex, chatManager);
    }
    
    private ChatManagerDecorator createAssessmentDecorator(int itemIndex) {
        return ChatManagerAbstractFactory.createChatManager(itemIndex, chatManager);
    }

    private void handleExportButton() {
        String selectedItem = "";
        String timeStamp = new SimpleDateFormat("HH.mm.ss").format(new java.util.Date());
        String filename = "";
        int itemIndex = assessmentCombo.getSelectionModel().getSelectedIndex();
        File fileExport = null;
        if (assessmentCombo.getValue() != null) {
            selectedItem = assessmentCombo.getValue();
            if (selectedItem.equals("Empty")) {
                filename = "Empty-" + timeStamp;
                fileExport = openExportFileChooser(filename);
            } else if (selectedItem.equals("Conspicuous")) {
                filename = "Conspicuous-" + timeStamp;
                fileExport = openExportFileChooser(filename);
            } else if (selectedItem.equals("Not Conspicuous")) {
                filename = "Not Conspicuous-" + timeStamp;
                fileExport = openExportFileChooser(filename);
            }
            if (fileExport != null) {
                exportFile(itemIndex, fileExport);
            }
        }
    }

    private File openExportFileChooser(String filename) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TCA files (*.tca)", "*.tca");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(filename);
        File fileExport = fileChooser.showSaveDialog(telecomChat.getPrimaryStage());
        return fileExport;
    }

    private void genericSave() {
        if (file == null) {
            saveAsAction();
        } else {
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
        if (value.equals("")) {
            return;
        }
        moveTo(value);

    }

    private void moveTo(String value) {
        try {
            int conversationNumber = Integer.valueOf(value);
            conversation = chatManager.goToConversation(conversationNumber);
            updateValues();
        } catch (NumberFormatException e) {

        } catch (NoValidConversation noValidConversation) {
            noValidConversation.printStackTrace();
        }
    }

    private void exportFile(int itemIndex, File fileExport) {
        ChatManagerDecorator exportManager = createAssessmentDecorator(itemIndex);
        saveFile(fileExport, exportManager);
    }

    private void handleSave() {

        saveFile(file, chatManager);

    }

    private void saveFile(File fileToExport, ChatManagerDecorator chatManagerToExport) {
        try {
            if (fileToExport != null) {
                ElizaWriter writer = new ElizaWriter(fileToExport.getAbsolutePath());
                writer.openOverwriting();
                chatManagerToExport.reset();
                do {
                    Conversation c = chatManagerToExport.getNextConversation();
                    String lineToWrite = c.write();
                    writer.write(lineToWrite);
                } while (chatManagerToExport.hastNext());
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoValidConversation noValidConversation) {
            noValidConversation.printStackTrace();
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
            telecomChat.getPrimaryStage().setTitle(file.getName());
            FileSystemReadable fs = new ElizaFileSystem(filename);
            InfoRenderer infoRenderer = new InfoRenderer(chatGridPane);
            SystemRenderer systemRenderer = new SystemRenderer(chatGridPane);
            UserRenderer userRenderer = new UserRenderer(chatGridPane);
            reader = new ElizaReader(fs, infoRenderer, userRenderer, systemRenderer);
            reader.open();
            reader.read();
            LinkedList<Conversation> conversations = reader.getConversations();
            chatManager = new ChatManager(conversations);
            unAnnotatedManager = new UnAnnotatedConversationDecorator(chatManager);
            try {
                conversation = chatManager.getNextConversation();
            } catch (NoValidConversation noValidConversation) {
                noValidConversation.printStackTrace();
            }

            addConversationIntoChatFrame();
            fillCombobox(conversations);
            if (filename.contains(".tca")) {
                this.file = file;
            }
        }

        goTo.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
            @Override
            public void run() {
                genericSave();
            }
        });
    }

    private void addConversationIntoChatFrame() {
        int messageCounter = 0;
        if (conversation.isPinned()) {
            pinButton.setText("UnPin");
            sessionPinList.setValue(sessionobservableList.get(current_position));
        } else {
            pinButton.setText("Pin");
            sessionPinList.setValue(null);
        }

        if (conversation.getDefenseStrategy() >= 0) {
            strategyField.setText("" + conversation.getDefenseStrategy());
        }

        if (conversation.getAssesment() >= 0) {
            assessmentTextField.setText("" + conversation.getAssesment());
        }

        addConversationDialog();
    }

    private void addConversationDialog() {
        conversation.render();
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
            sessionaryLst.add(conversationCounter + "  " + "( " + conversation.getTotalMessages() + " Message(s) " + ")");
            if (c.isPinned()) {
                int conlistNumber = conversationCounter - 1;
                String s = conversationCounter + "  " + "( " + conversation.getTotalMessages() + " Message(s) " + ")";
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

    private void fillAssessmentCombo() {

        List assesmentList = new ArrayList();
        assesmentList.add("0 Empty");
        assesmentList.add("1 Conspicuous");
        assesmentList.add("2 Not Conspicuous");

        assessmentCombo.getItems().addAll(FXCollections.observableArrayList(assesmentList));
    }

    private void fillDefenceStrategyCombo() {

        List defenceStrategy = new ArrayList();
        defenceStrategy.add("0 Withdrawal");
        defenceStrategy.add("1 Attack other");
        defenceStrategy.add("2 Attack Self");
        defenceStrategy.add("3 Avoidance");
        defenceStrategy.add("4 positive Interaktion");
        defenceStrategy.add("5 neutrale Interaktion");

        defenceStrategyCombo.getItems().addAll(FXCollections.observableArrayList(defenceStrategy));
    }

    private void moveNext() {
        previousButton.setDisable(false);
        try {
            conversation = chatManager.getNextConversation();
            updateValues();
        } catch (NoValidConversation noValidConversation) {
            nextButton.setDisable(true);
        }

    }

    private void movePrevious() {
        nextButton.setDisable(false);
        try {
            conversation = chatManager.getPreviousConversation();
            updateValues();
        } catch (NoValidConversation noValidConversation) {
            previousButton.setDisable(true);
        }

    }

    private void updateValues() {
        emptyConversationFields();
        sessionList.getSelectionModel().select(chatManager.getCurrentPosition());
        disableNavigationButtons();
        addConversationIntoChatFrame();

    }

    private void disableNavigationButtons() {
        if (!chatManager.hastNext()) {
            nextButton.setDisable(true);
        }
        if (!chatManager.hasPrevious()) {
            previousButton.setDisable(true);
        }
    }

    private void emptyConversationFields() {
        if (conversation.getDefenseStrategy() == -1) {
            strategyField.setText("");
        }
        if (conversation.getAssesment() == -1) {
            assessmentTextField.setText("");
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
}
