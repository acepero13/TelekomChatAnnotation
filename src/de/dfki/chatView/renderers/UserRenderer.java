package de.dfki.chatView.renderers;

import de.dfki.chatView.ChatController;

import de.dfki.eliza.files.models.Message;
import de.dfki.eliza.files.models.Textable;
import de.dfki.eliza.renderer.Renderable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
/**
 * Created by alvaro on 3/28/17.
 */
public class UserRenderer implements Renderable {

    private final GridPane chatGridPane;
    private String userText;


    public UserRenderer(GridPane gridPane){
        this.chatGridPane = gridPane;
    }
    @Override
    public void render(int i, Textable textable) {
        userText = textable.getText();
        Message message = (Message) textable;
        if (cannotRender()) {
            return;
        }
        createComponents(i, message);
    }

    private void createComponents(int i, Message message) {
        String userDialog = userText;

        Label speakerLabel = new Label("User");
        Label chatMessageLabel = createChatMessage(userDialog, speakerLabel);
        TextField userTopic = createUserTopicTextField(message);
        TextField userValue = createUserValue(message);
        TextField defenceStrategy = createDefenseStrategieText(i);


        Pane p1 = createTopicPane();
        Pane p2 = createValuePane();
        Pane p3 = createSpeakerPane();
        Pane p4 = createDefensePane(defenceStrategy);

        p1.getChildren().add(userTopic);
        p1.setPadding(new Insets(0, 50, 0, 50));
        p2.getChildren().add(userValue);
        p2.setPadding(new Insets(0, 50, 0, 50));
        p3.getChildren().add(speakerLabel);
        p3.setPadding(new Insets(0, 10, 0, 50));

        chatGridPane.add(p3, 0, i);
        chatGridPane.add(chatMessageLabel, 1, i);
        chatGridPane.add(p1, 2, i);
        chatGridPane.add(p2, 3, i);
        chatGridPane.add(p4, 4, i);

        setUserTopicListener(message, userTopic);
        setUserValueListener(message, userValue);
        setUserValueListener(message, defenceStrategy);
        if (i >= 0 && message.getValue() != -1) {
            defenceStrategy.setText("" + message.getValue());
        }
    }

    private boolean cannotRender() {
        return userText.equals("");
    }

    private void setUserValueListener(Message message, TextField userValue) {
        userValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (ChatController.isNumeric(newValue)) {
                message.setValue(Integer.parseInt(newValue));
            }
        });
    }

    private void setUserTopicListener(Message message, TextField userTopic) {
        userTopic.textProperty().addListener((observable, oldValue, newValue) -> {
            if (ChatController.isNumeric(newValue)) {
                    message.setTopic(Integer.parseInt(newValue));
            }
        });
    }

    private Pane createDefensePane(TextField defenceStrategy) {
        Pane p4 = new Pane();
        p4.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");

        p4.getChildren().add(defenceStrategy);
        p4.setPadding(new Insets(0, 10, 0, 50));
        return p4;
    }

    private TextField createDefenseStrategieText(int i) {
        TextField defenceStrategy = new TextField();
        defenceStrategy.setPrefWidth(100);
        defenceStrategy.setPadding(new Insets(8, 0, 8, 0));
        defenceStrategy.setId("defenceStrategy" + i);
        return defenceStrategy;
    }

    private Pane createSpeakerPane() {
        Pane p3 = new Pane();
        p3.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");
        return p3;
    }

    private Pane createValuePane() {
        Pane p2 = new Pane();
        p2.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");
        return p2;
    }

    private Pane createTopicPane() {
        Pane p1 = new Pane();
        p1.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");
        return p1;
    }

    private TextField createUserValue(Message message) {
        TextField userValue = new TextField();
        userValue.setPrefWidth(100);
        userValue.setPadding(new Insets(8, 0, 8, 0));
        if (message.getValue() != -1) {
            userValue.setText("" + message.getValue());
        }

        //userValue.setId("userValue" + i);
        return userValue;
    }

    private TextField createUserTopicTextField(Message message) {
        TextField userTopic = new TextField();
        userTopic.setPrefWidth(100);
        userTopic.setPadding(new Insets(8, 0, 8, 0));

        if (message.getTopic() != -1) {
            userTopic.setText("" + message.getTopic());
        }
        //userTopic.setId("userTopic" + i);
        return userTopic;
    }

    private Label createChatMessage(String userDialog, Label speakerLabel) {
        Label chatMessage = new Label(userDialog);
        chatMessage.setMinHeight(35);
        chatMessage.setStyle("-fx-background-color: #EFFFFF; -fx-alignment: left;");
        speakerLabel.setStyle("-fx-font-weight: bold;");

        chatMessage.setAlignment(Pos.TOP_LEFT);

        chatMessage.setWrapText(true);
        chatMessage.setPrefWidth(3000);

        GridPane.setHalignment(chatMessage, HPos.LEFT);
        chatMessage.setPadding(new Insets(0, 10, 0, 0));
        return chatMessage;
    }


}
