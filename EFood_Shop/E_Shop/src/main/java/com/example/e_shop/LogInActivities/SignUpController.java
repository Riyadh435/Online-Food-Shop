package com.example.e_shop.LogInActivities;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.Animation.BookPage;
import com.example.e_shop.Animation.LogInAnimation;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class SignUpController implements Initializable {
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField full_name;
    @FXML
    private TextField username;
    @FXML
    private PasswordField user_password;
    @FXML
    private PasswordField retype_password;
    @FXML
    private TextField user_email;
    @FXML
    private Label passwordAlert;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private Label passShowLabel1;
    @FXML
    private Label passShowLabel2;


    @FXML
    public void sampleSignUp() {
        if (full_name.getText().isEmpty() || username.getText().isEmpty() || user_password.getText().isEmpty() || user_email.getText().isEmpty()) {
            //showAlert("Looks like you forgot something", false);
            ErrorAlert.displayCustomAlert("ERROR", "Looks like you forgot something");
            return;
        }

        if (!isValidEmail(user_email.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Invalid email format !");
            //showAlert("Invalid email format !", false);
            return;
        }
        if (!user_password.getText().equals(retype_password.getText())) {
            ErrorAlert.displayCustomAlert("information", "Please type same password");
            // showAlert("Type same password", true);
            return;
        }
        signUpDetails(full_name.getText(), username.getText(), user_password.getText(), user_email.getText(), "user99");
        full_name.clear();
        username.clear();
        user_email.clear();
        user_password.clear();
        retype_password.clear();
        passShowLabel2.setVisible(false);
        passShowLabel1.setVisible(false);
        toggleButton.setText("Show");
    }

    public void signUpDetails(String full_name, String user_name, String userpassword, String email, String accountType) {
        try (Connection connection = DataBase.connectDB()) {
            // Check if username or email already exists based on the account type
            String tableName;
            if (accountType.equals("db")) {
                tableName = "deliveryman_info";
            } else if (accountType.equals("rstrnt")) {
                tableName = "resturent_info";
            } else {
                tableName = "user_information";
            }
            // Default add a profile picture...
            String path = "F:\\vecteezy_3d-icons-occupation-job-avatar-for-social-media-profile_28169662.png";
            path = path.replace("\\", "\\\\");

            // check USER table if name or email already exist or not...
            PreparedStatement checkStatement1 = connection.prepareStatement("SELECT * FROM user_information WHERE user_name = ? OR user_email = ?");
            checkStatement1.setString(1, user_name);
            checkStatement1.setString(2, email);
            ResultSet resultSet1 = checkStatement1.executeQuery();

            // check DeliveryMan table if name or email already exist or not...
            PreparedStatement checkStatement2 = connection.prepareStatement("SELECT * FROM deliveryman_info WHERE user_name = ? OR user_email = ?");
            checkStatement2.setString(1, user_name);
            checkStatement2.setString(2, email);
            ResultSet resultSet2 = checkStatement2.executeQuery();

            // check Moderator table if name or email already exist or not...
            PreparedStatement checkStatement3 = connection.prepareStatement("SELECT * FROM resturent_info WHERE user_name = ? OR user_email = ?");
            checkStatement3.setString(1, user_name);
            checkStatement3.setString(2, email);
            ResultSet resultSet3 = checkStatement3.executeQuery();

            if (resultSet1.next() || resultSet2.next() || resultSet3.next()) {
                ErrorAlert.displayCustomAlert("Information", "This username or email already exists");
                return;
            } else {
                // Insert new record if username and email are unique
                PreparedStatement insertStatement;
                if (tableName.equals("user_information")) {
                    insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " (user_name, user_pass, user_email, user_balance, full_name, image) VALUES (?, ?, ?, ?,?,?)");
                    insertStatement.setDouble(4, 500);
                    insertStatement.setString(5, full_name);
                    insertStatement.setString(6, path);
                } else if (tableName.equals("resturent_info")) {
                    insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " (user_name, user_pass, user_email, block_status, full_name, image) VALUES (?, ?, ?, ?, ?,?)");
                    insertStatement.setString(4, "No");
                    insertStatement.setString(5, full_name);
                    insertStatement.setString(6, path);
                } else {
                    insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " (user_name, user_pass, user_email, full_name, image) VALUES (?, ?, ?,?,?)");
                    insertStatement.setString(4, full_name);
                    insertStatement.setString(5, path);
                }
                insertStatement.setString(1, user_name);
                insertStatement.setString(2, userpassword);
                insertStatement.setString(3, email);
                insertStatement.executeUpdate();
                // showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful");
                SuccessAlert.displayCustomAlert("SUCCESS", "Sign Up Successful");
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "OPS sorry something went wrong");
        }
    }


    @FXML
    private void BackToLogIn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LogInActivities/LogInController.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        LogInAnimation.LeftOpenAnimation(stage);
        stage.show();


    }

    // This method check the input email or not
    public boolean isValidEmail(String email) {

        return email.matches("[a-zA-Z0-9._%+-]+@gmail\\.com");
    }


    private void checkPasswordStrength(String password) {
        if (password.isEmpty()) {
            passwordAlert.setText("");
        } else if (password.length() < 4) {
            passwordAlert.setText("Weak");
            passwordAlert.setTextFill(Color.RED);
        } else if (password.length() < 7) {
            passwordAlert.setText("Good");
            passwordAlert.setTextFill(Color.BLUE);
        } else {
            passwordAlert.setText("Strong");
            passwordAlert.setTextFill(Color.GREEN);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passShowLabel1.setVisible(false);
        passShowLabel2.setVisible(false);
        user_password.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkPasswordStrength(newValue);
            }
        });
    }

    @FXML
    void TypePassword(KeyEvent event) {
        passShowLabel1.textProperty().bind(Bindings.concat(user_password.getText()));


    }

    @FXML
    void reTypePassword(KeyEvent event) {
        passShowLabel2.textProperty().bind(Bindings.concat(retype_password.getText()));

    }

    @FXML
    void newToggleButtonPress(ActionEvent event) {
        if (toggleButton.isSelected()) {
            passShowLabel1.setVisible(true);
            passShowLabel2.setVisible(true);
            passShowLabel1.textProperty().bind(Bindings.concat(user_password.getText()));
            passShowLabel2.textProperty().bind(Bindings.concat(retype_password.getText()));
            toggleButton.setText("Hide");
        } else {
            passShowLabel1.setVisible(false);
            passShowLabel2.setVisible(false);
            toggleButton.setText("Show");
        }
    }
}

