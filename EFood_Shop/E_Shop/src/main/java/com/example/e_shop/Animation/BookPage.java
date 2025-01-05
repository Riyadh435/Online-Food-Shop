package com.example.e_shop.Animation;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BookPage {
    public static void openPageAnimation(Stage stage) {
        Node nodeToAnimate = stage.getScene().getRoot(); // Assuming you want to animate the root node of the scene
        double sceneWidth = stage.getScene().getWidth();

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(800), nodeToAnimate);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(-90);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800), nodeToAnimate);
        translateTransition.setFromX(0);
        translateTransition.setToX(-sceneWidth);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, translateTransition);
        parallelTransition.play();
    }

    public static void closePageAnimation(Stage stage) {
        Node nodeToAnimate = stage.getScene().getRoot(); // Assuming you want to animate the root node of the scene
        double sceneWidth = stage.getScene().getWidth();

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(800), nodeToAnimate);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(-90);
        rotateTransition.setToAngle(0);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(800), nodeToAnimate);
        translateTransition.setFromX(-sceneWidth);
        translateTransition.setToX(0);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, translateTransition);
        parallelTransition.play();
    }
}
