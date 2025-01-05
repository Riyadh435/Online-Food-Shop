package com.example.e_shop.Alert.AlertController;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class AcceptAlertController {

    @FXML
    private Stage dialogStage;
    boolean choice = false;
    @FXML
    private Label message;

    @FXML
    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }
    @FXML
    public void ignoreButton(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void acceptButton(ActionEvent event) {
        choice = true;
        dialogStage.close();
    }

    public boolean isAccepted(){
        return choice;
    }

    public void setMessage(String msg) {
        message.setText(msg);
    }
}

