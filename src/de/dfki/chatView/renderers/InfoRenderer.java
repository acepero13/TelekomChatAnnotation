package de.dfki.chatView.renderers;
import de.dfki.eliza.files.models.Textable;
import de.dfki.eliza.renderer.Renderable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by alvaro on 3/28/17.
 */
public class InfoRenderer implements Renderable {
    private  Textable message;
    private String infoText = "";
    private GridPane chatGridPane;

    public InfoRenderer( GridPane gridPane){
        this.chatGridPane = gridPane;
    }

    @Override
    public void render(int rowPosition, Textable textable) {
        infoText = textable.getText();
        if (cannotCreateInfoLabel()) {
            return;
        }
        createInfoLabel(rowPosition);
    }

    private void createInfoLabel(int rowPosition) {

        Label speakerLabel = createSpeakerLable();
        Label chatInfo = createChatInfo();
        GridPane.setHalignment(chatInfo, HPos.LEFT);
        Pane p1 = createPane(speakerLabel);
        chatGridPane.add(p1, 0, rowPosition);
        chatGridPane.add(chatInfo, 1, rowPosition);
    }

    private Pane createPane(Label speakerLabel) {
        Pane p1 = new Pane();
        p1.getChildren().add(speakerLabel);
        p1.setPadding(new Insets(0, 10, 0, 50));
        return p1;
    }

    private Label createChatInfo() {
        Label chatInfo = new Label(infoText);
        chatInfo.setAlignment(Pos.TOP_LEFT);
        chatInfo.setWrapText(true);
        chatInfo.setPrefWidth(1000);
        return chatInfo;
    }

    private Label createSpeakerLable() {
        Label speakerLabel = new Label("Info");
        speakerLabel.setStyle("-fx-font-weight: bold;");
        return speakerLabel;
    }

    private boolean cannotCreateInfoLabel() {
        return infoText.equals("");
    }
}
