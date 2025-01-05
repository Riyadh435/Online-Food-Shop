package com.example.e_shop.Alert;

import com.example.e_shop.Alert.AlertController.ErrorAlertController;
import com.example.e_shop.Alert.AlertController.SuccessAlertController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SuccessAlert {
    private static Stage ownerStage;

    public static void displayCustomAlert( String title, String message) {

        try {
            FXMLLoader loader = new FXMLLoader(SuccessAlert.class.getResource("/com/example/e_shop/Alert/SuccessAlert.fxml"));
            Parent root = loader.load();

            SuccessAlertController controller = loader.getController();

            Stage customAlertStage = new Stage(StageStyle.UNDECORATED);

            customAlertStage.initModality(Modality.APPLICATION_MODAL);
            customAlertStage.initOwner(ownerStage);

            customAlertStage.setScene(new Scene(root));
            controller.setDialogStage(customAlertStage);
            addOpenAnimation(customAlertStage);

            controller.setMessage(message);
            controller.setTitle(title);
            customAlertStage.setTitle(title);
            customAlertStage.show();


            addCloseAnimation(customAlertStage);
            autoCloseAlert(customAlertStage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void autoCloseAlert(Stage st) {

        // Automatically remove the message after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> st.close()));
        timeline.play();
    }

    private static void addOpenAnimation(Stage customAlertStage) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), customAlertStage.getScene().getRoot());
        translateTransition.setFromY(-100);
        translateTransition.setToY(0);
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), customAlertStage.getScene().getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private static void addCloseAnimation(Stage customAlertStage) {
        // You can add a similar animation for closing the alert
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), customAlertStage.getScene().getRoot());
        translateTransition.setFromY(-100);
        translateTransition.setToY(0);
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), customAlertStage.getScene().getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}

