package com.example.e_shop.ShowAdd;

import com.example.e_shop.DataBaseConnection.DataBase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class sampleAdd {

    @FXML
    private AnchorPane mainForm;
    @FXML
    private AnchorPane sample1;
    @FXML
    private Rectangle sampleRectangle1;
    @FXML
    private AnchorPane sample2;
    @FXML
    private Rectangle sampleRectangle2;
    @FXML
    private AnchorPane sample3;
    @FXML
    private Rectangle sampleRectangle3;
    @FXML
    private AnchorPane sample4;
    @FXML
    private Rectangle sampleRectangle4;

    private final int SECONDS_PER_PANE = 5;
    private final int MAX_RECTANGLES = 4;
    private Timeline timeline;

    @FXML
    public void initialize() {
        setLatestImagePathsToRectangles();
        setupTimeline();
        // Initially show the first pane
        showPane(sample1);
    }

    private void setupTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE), event -> showPane(sample1)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 2), event -> showPane(sample2)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 3), event -> showPane(sample3)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 4), event -> showPane(sample4))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showPane(AnchorPane pane) {
        // Hide all panes
        for (int i = 1; i <= MAX_RECTANGLES; i++) {
            AnchorPane samplePane = (AnchorPane) mainForm.lookup("#sample" + i);
            if (samplePane != null)
                samplePane.setVisible(false);
        }
        // Show the specified pane
        pane.setVisible(true);
    }

    void setLatestImagePathsToRectangles() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Execute SQL query to fetch the latest uploaded image paths
            String query = "SELECT prod_image FROM review_post ORDER BY id DESC LIMIT " + MAX_RECTANGLES;
            ResultSet resultSet = statement.executeQuery(query);
            // Counter to track the Rectangle to set
            int rectangleCounter = 1;
            while (resultSet.next() && rectangleCounter <= MAX_RECTANGLES) {
                String imagePath = resultSet.getString("prod_image");
                // Set the image in corresponding Rectangle
                setImage("sampleRectangle" + rectangleCounter, imagePath);
                rectangleCounter++;
            }
            // If fewer than four images were found, set the remaining Rectangles to default
            while (rectangleCounter <= MAX_RECTANGLES) {
                // You can set a default image path here
                setImage("sampleRectangle" + rectangleCounter, "default_image_path");
                rectangleCounter++;
            }
        } catch (SQLException e) {
            // Handle SQL exception more gracefully
            e.printStackTrace();
            // Handle exception here, e.g., display an error message to the user
        }
    }

    private void setImage(String rectangleName, String imagePath) {
        // Get the Rectangle dynamically by name
        Rectangle rectangle = (Rectangle) mainForm.lookup("#" + rectangleName);
        if (rectangle != null) {
            // Set the image in Rectangle
            try {
                InputStream inputStream = getClass().getResourceAsStream(imagePath);
                if (inputStream != null) {
                    Image image = new Image(inputStream, 110, 120, false, true);
                    rectangle.setFill(new ImagePattern(image));
                } else {
                    // Handle case where resource is not found
                    System.err.println("Image resource not found: " + imagePath);
                }
            } catch (Exception e) {
                // Handle any exceptions that may occur during image loading
                e.printStackTrace();
            }
        }
    }

}
