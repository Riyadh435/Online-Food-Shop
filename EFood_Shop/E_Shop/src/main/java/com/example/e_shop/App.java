package com.example.e_shop;
import com.example.e_shop.Animation.LogInAnimation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Image logo = new Image(getClass().getResource("/Pictures/foodshop.png").toExternalForm());
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        LogInAnimation.addOpenAnimation(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}