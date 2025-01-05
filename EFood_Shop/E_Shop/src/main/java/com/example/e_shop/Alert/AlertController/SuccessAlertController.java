package com.example.e_shop.Alert.AlertController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SuccessAlertController {


    @FXML
    private VBox vBox;
    @FXML
    private Label title;

    private Stage dialogStage;

    public void setMessage(String message) {
        javafx.scene.text.Text text = new Text(message);
        text.setStyle("-fx-font-size: 16");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0, 0, 0));
        vBox.getChildren().add(textFlow);
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    public void setTitle(String m) {
        title.setText(m);
    }



    @FXML
    public void closeAlert() {
        dialogStage.close();
    }
    // #A5DAC8
}



