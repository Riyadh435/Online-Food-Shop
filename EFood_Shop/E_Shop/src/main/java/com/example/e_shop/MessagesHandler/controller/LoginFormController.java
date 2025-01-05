package com.example.e_shop.MessagesHandler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private TextField txtName;


    @FXML
    public void logInButtonOnAction(ActionEvent actionEvent) {
        try {
            if (!txtName.getText().isEmpty() && txtName.getText().matches("[A-Za-z0-9]+")) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/e_shop/MessagesHandler/view/ClientForm.fxml"));
                Stage primaryStage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());

                ClientFormController controller = fxmlLoader.getController();
                controller.setClientName(txtName.getText()); // Set the parameter

                primaryStage.setScene(scene);
                primaryStage.setTitle(txtName.getText());
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
//                primaryStage.setOnCloseRequest(windowEvent -> {
//                    controller.shutdown();
//                });
                primaryStage.show();

                txtName.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception accordingly, e.g., show an error dialog
        }
    }
}
