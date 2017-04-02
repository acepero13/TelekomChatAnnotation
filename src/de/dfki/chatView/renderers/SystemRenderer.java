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
public class SystemRenderer implements Renderable {


    private final GridPane chatGridPane;
    private String systemText;


    public SystemRenderer(GridPane gridPane){
        this.chatGridPane = gridPane;
    }

    @Override
    public void render(int i, Textable textable) {
        systemText = textable.getText();
        Message message = (Message) textable;
        if (cannotRender()) {
            return;
        }

        String systemDialog = systemText;
        Label speakerLabel = new Label("Agent");
        Label chatMessage = createChatMessage(systemDialog, speakerLabel);



        TextField systemTopic = createSystemTopicTextField(message);

        TextField systemValue = createSystemValueTextField(message);

        Pane p1 = createTopicPane();
        Pane p2 = createValuePane();
        Pane p3 = createSpeakerLabelPane();
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

        Pane p4 = new Pane();
        p4.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");

        p4.setPadding(new Insets(0, 10, 0, 50));

        chatGridPane.add(p4, 4, i);

        addSystemTopicListener(message, systemTopic);

        addSystemValueListener(message, systemValue);

    }

    private boolean cannotRender() {
        return systemText.equals("");
    }

    private void addSystemValueListener(Message message, TextField systemValue) {
        systemValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (ChatController.isNumeric(newValue)) {
                message.setValue(Integer.parseInt(newValue));
            }
        });
    }

    private void addSystemTopicListener(Message message, TextField systemTopic) {
        systemTopic.textProperty().addListener((observable, oldValue, newValue) -> {

            if (ChatController.isNumeric(newValue)) {
                message.setTopic(Integer.parseInt(newValue));
            }
        });
    }

    private Pane createSpeakerLabelPane() {
        Pane p3 = new Pane();
        p3.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
        return p3;
    }

    private Pane createValuePane() {
        Pane p2 = new Pane();
        p2.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
        return p2;
    }

    private Pane createTopicPane() {
        Pane p1 = new Pane();
        p1.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
        return p1;
    }

    private TextField createSystemValueTextField(Message message) {
        TextField systemValue = new TextField();
        systemValue.setPrefWidth(100);
        systemValue.setPadding(new Insets(8, 0, 8, 0));
        if (message.getValue() != -1) {
            systemValue.setText("" + message.getValue());
        }
        //systemValue.setId("systemValue" + i);
        return systemValue;
    }

    private TextField createSystemTopicTextField(Message message) {
        TextField systemTopic = new TextField();
        systemTopic.setPrefWidth(100);
        systemTopic.setPadding(new Insets(8, 0, 8, 0));

        if (message.getTopic() != -1) {
            systemTopic.setText("" + message.getTopic());
        }
        //systemTopic.setId("systemTopic" + i);
        return systemTopic;
    }

    private Label createChatMessage(String systemDialog, Label speakerLabel) {
        Label chatMessage = new Label(systemDialog);

        chatMessage.setMinHeight(35);
        chatMessage.setStyle("-fx-background-color: cornsilk; -fx-alignment: left;");
        speakerLabel.setStyle("-fx-font-weight: bold;");

        chatMessage.setAlignment(Pos.TOP_LEFT);
        chatMessage.setWrapText(true);
        chatMessage.setPrefWidth(3000);
        GridPane.setHalignment(chatMessage, HPos.LEFT);
        chatMessage.setText(chatMessage.getText());

        chatMessage.setPadding(new Insets(0, 10, 0, 0));
        return chatMessage;
    }
}
