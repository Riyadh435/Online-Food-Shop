package com.example.e_shop.ShowAdd;

import com.example.e_shop.DataBaseConnection.DataBase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class sampleAdd1 {

    @FXML
    private AnchorPane mainForm;
    @FXML
    private AnchorPane sample1;
    @FXML
    private ImageView sampleview1;
    @FXML
    private AnchorPane sample2;
    @FXML
    private ImageView sampleview2;
    @FXML
    private AnchorPane sample3;
    @FXML
    private ImageView sampleview3;
    @FXML
    private AnchorPane sample4;
    @FXML
    private ImageView sampleview4;

    private final int SECONDS_PER_PANE = 5;
    private Timeline timeline;

    @FXML
    public void initialize() {
        setLatestImagePathsToImageViews();
        setupTimeline();
        // Initially show the first pane
        showPane(sample1);

        // Add click event handlers to each image view
        sampleview1.setOnMouseClicked(event -> openImageInNewWindow(sampleview1));
        sampleview2.setOnMouseClicked(event -> openImageInNewWindow(sampleview2));
        sampleview3.setOnMouseClicked(event -> openImageInNewWindow(sampleview3));
        sampleview4.setOnMouseClicked(event -> openImageInNewWindow(sampleview4));
    }

    private void setupTimeline() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE), event -> showPane(sample1)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 2), event -> showPane(sample2)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 3), event -> showPane(sample3)),
                new KeyFrame(Duration.seconds(SECONDS_PER_PANE * 3), event -> showPane(sample4))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showPane(AnchorPane pane) {
        // Hide all panes
        sample1.setVisible(false);
        sample2.setVisible(false);
        sample3.setVisible(false);
        sample4.setVisible(false);
        // Show the specified pane
        pane.setVisible(true);
    }

    void setLatestImagePathsToImageViews() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Execute SQL query to fetch the latest uploaded image paths
            String query = "SELECT prod_image FROM review_post ORDER BY id DESC LIMIT 4";
            ResultSet resultSet = statement.executeQuery(query);
            // Counter to track the ImageView to set
            int imageViewCounter = 1;
            while (resultSet.next() && imageViewCounter <= 4) {
                String imagePath = resultSet.getString("prod_image");
                // Set the image in corresponding ImageView
                setImage("sampleview" + imageViewCounter, imagePath);
                imageViewCounter++;
            }
            // If fewer than four images were found, set the remaining ImageViews to default
            while (imageViewCounter <= 4) {
                // You can set a default image path here
                setImage("sampleview" + imageViewCounter, "default_image_path");
                imageViewCounter++;
            }
        } catch (SQLException e) {
            // Handle SQL exception more gracefully
            e.printStackTrace();
        }
    }

    private void setImage(String imageViewName, String imagePath) {
        // Get the ImageView dynamically by name
        ImageView imageView = (ImageView) mainForm.lookup("#" + imageViewName);
        if (imageView != null) {
            // Set the image in ImageView
            Image image = new Image("File:" + imagePath, 110, 120, false, true);
            imageView.setImage(image);
        }
    }

    private void openImageInNewWindow(ImageView imageView) {
        // Create a new stage for displaying the image
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Details");

        StackPane stackPane = new StackPane();
        ImageView imageViewInWindow = new ImageView(imageView.getImage().getUrl());
        imageViewInWindow.setFitWidth(400);
        imageViewInWindow.setFitHeight(400);
        stackPane.getChildren().add(imageViewInWindow);

        // Create a Scene with the StackPane as its root
        Scene scene = new Scene(stackPane);

        // Set the Scene to the stage and show the stage
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
