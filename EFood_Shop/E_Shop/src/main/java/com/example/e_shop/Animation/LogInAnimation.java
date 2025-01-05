package com.example.e_shop.Animation;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LogInAnimation {

    public static void RightOpenAnimation(Stage customAlertStage) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(400), customAlertStage.getScene().getRoot());
        translateTransition.setFromX(-customAlertStage.getScene().getWidth()); // Start from the left side
        translateTransition.setToX(0); // Move to the original position
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), customAlertStage.getScene().getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public static void LeftOpenAnimation(Stage customAlertStage) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(400), customAlertStage.getScene().getRoot());
        translateTransition.setFromX(customAlertStage.getScene().getWidth()); // Start from the right side
        translateTransition.setToX(0); // Move to the original position
        translateTransition.play();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), customAlertStage.getScene().getRoot());
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }



    public static void addOpenAnimation(Stage customAlertStage) {
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
