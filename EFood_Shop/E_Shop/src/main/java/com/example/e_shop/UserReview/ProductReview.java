package com.example.e_shop.UserReview;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MainDashBoard.data;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductReview implements Initializable {

    @FXML
    private TextArea reviewText;
    @FXML
    private TextField reviewTitle;
    @FXML
    private RadioButton YesBtn;
    @FXML
    private RadioButton NoBtn;

    private String Type;

    public void getTypeOfAccount() {
        if (YesBtn.isSelected()) {
            Type = "Yes";

        } else if (NoBtn.isSelected()) {
            Type = "No";
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void AddReviewButton() {
        if (reviewTitle.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Enter a title");
            return;
        }
        if (Type == null) {
            ErrorAlert.displayCustomAlert("Error", "Select a option");
            return;

        }
        if (reviewText.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter your text");
            return;
        }


        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO prod_review (reviewer_username, reviewer_fullname" +
                    ", reviewer_image, prod_id, prod_name,prod_comment,prod_image,rv_title,rc , prod_uploader ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setString(1, data.prod_selector_username);
            insertStatement.setString(2, data.prod_selector_fullName);
            insertStatement.setString(3, data.prod_selector_profile_image);
            insertStatement.setString(4, data.prod_idForAddReview);
            insertStatement.setString(5, data.prod_nameForAddReview);
            insertStatement.setString(6, reviewText.getText());
            insertStatement.setString(7, data.prod_imageForAddReview);
            insertStatement.setString(8, reviewTitle.getText());
            insertStatement.setString(9, Type);
            insertStatement.setString(10, data.prod_uploader);
            insertStatement.executeUpdate();
            SuccessAlert.displayCustomAlert("SUCCESS", "Review Added Successfully");
            clearIfNotSub();
            Stage stage = (Stage) reviewText.getScene().getWindow();
            autoCloseAlert(stage);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void closeStage() {
        reviewText.clear();
        clearIfNotSub();

        Stage stage = (Stage) reviewText.getScene().getWindow();
        stage.close();
    }


    private static void autoCloseAlert(Stage st) {

        // Automatically remove the message after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> st.close()));
        timeline.play();
    }

    void clearIfNotSub() {
        data.prod_idForAddReview = null;
        data.prod_nameForAddReview = null;
        data.prod_imageForAddReview = null;
        data.prod_selector_username = null;
        data.prod_selector_fullName = null;
        data.prod_selector_profile_image = null;
        data.prod_uploader = null;

    }

}
