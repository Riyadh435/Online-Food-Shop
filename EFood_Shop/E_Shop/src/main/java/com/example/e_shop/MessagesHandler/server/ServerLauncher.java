package com.example.e_shop.MessagesHandler.server;

import com.example.e_shop.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/example/e_shop/MessagesHandler/view/ServerForm.fxml"))));
        primaryStage.setTitle("Server");
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.hide();

//        Stage stage = new Stage();
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initOwner(primaryStage.getScene().getWindow());
//        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"))));
//        stage.setResizable(false);
//        stage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"));
//        Scene newScene = new Scene(fxmlLoader.load());
//
//        // Create a new stage for the new scene
//        Stage newStage = new Stage();
//        newStage.setScene(newScene);
//        newStage.setResizable(false);
//
//        // Show the new stage without hiding the previous one
//        newStage.show();

    }
}
