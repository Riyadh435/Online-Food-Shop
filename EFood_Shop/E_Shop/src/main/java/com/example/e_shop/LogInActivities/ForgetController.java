package com.example.e_shop.LogInActivities;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.Animation.LogInAnimation;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.EmailSender.Email;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgetController implements Initializable {


    /// Otp input Form and send otp
    @FXML
    private AnchorPane ConfirmUserForm;
    @FXML
    private Button sendOtp;
    @FXML
    private Label userNameFound;


    // Otp Submit Form...
    @FXML
    private AnchorPane OtpSubmitForm;
    @FXML
    private TextField OtpInput;
    @FXML
    private Button OtpConfirm;
    @FXML
    private Label timer;


    //Set Password_Form
    @FXML
    AnchorPane PasswordInputForm;
    @FXML
    private TextField newPassword;
    @FXML
    private Button submitFinal;

    @FXML
    private Label Alertlabel;
    @FXML
    private Label passwordAlert;
    @FXML
    private Label passwordShowLabel;
    @FXML
    private ToggleButton toggleButton;


    //Your Email..
    @FXML
    private TextField yourEmail;

    private String find_name = "";


    @FXML
    public void EmailVerifcationButton() {
        if (yourEmail.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter your email");
            return;
        }
        if (!new SignUpController().isValidEmail(yourEmail.getText())) {
            // showAlert("Invalid input format", false);
            ErrorAlert.displayCustomAlert("ERROR", "Invalid input format");
        } else {
            find_name = getEmailUserName(yourEmail.getText());
            if (find_name.equals("nothing")) {
                //showAlert("No email Found", false);
                ErrorAlert.displayCustomAlert("ERROR", "No email Found in Database.");
            } else {
                userNameFound.setText(find_name);
                ConfirmUserForm.setVisible(true);
            }
        }

    }

    private String code = "";

    @FXML
    void sendOTPBtn() {
        ConfirmUserForm.setVisible(false);
        OtpSubmitForm.setVisible(true);
        String to = yourEmail.getText(); // Update with recipient email
        String from = "shopmine847@gmail.com"; // Update with your Gmail address
        String sub = "My Shop";

        Email email = new Email();
        String verificationCode = email.generateVerificationCode(); // Generate verification code
        code = verificationCode;
        String text = "Hi " + find_name + "\n\nThis is your OTP to reset your password.";

        int result = email.send(to, from, sub, text, verificationCode);


        if (result == 1) {
            System.out.println("OTP sent to your email.");
        } else {
            // showAlert("Failed to send email", false);
            //System.out.println("Error: Failed to send email.");
            ErrorAlert.displayCustomAlert("ERROR", "Failed to send OTP in email.");
        }
    }
    // private int countdownDurationSeconds = 60;
    //  private Timeline timeline;

//    private void startCountdown() {
//        // Set up the timeline to update every second
//        timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
//            countdownDurationSeconds--;
//            if (countdownDurationSeconds >= 0) {
//                String stringTime = String.valueOf(countdownDurationSeconds);
//                timer.setText(stringTime);
//            } else {
//                OtpSubmitForm.setVisible(false);
//                ConfirmUserForm.setVisible(true);
//                code="";
//                // Countdown finished
//                timeline.stop();
//            }
//        }));
//        timeline.setCycleCount(countdownDurationSeconds + 1); // Count from 60 to 0
//        timeline.play();
//    }

    @FXML
    void ConfirmOtp() {
        if (OtpInput.getText().isEmpty()) {
            // System.out.println("Enter otp");
            // showAlert("Invalid Otp entered", false);
            ErrorAlert.displayCustomAlert("ERROR", "Invalid Otp entered");
            return;

        }
        if (OtpInput.getText().equals(code)) {
            // System.out.println("Success: Verification code matched.");
            PasswordInputForm.setVisible(true);
            ConfirmUserForm.setVisible(false);
            OtpSubmitForm.setVisible(false);
        } else {
            ErrorAlert.displayCustomAlert("ERROR", "Invalid verification code");
            //showAlert("Invalid verification code.", false);
            //System.out.println("Error: Invalid verification code.");
        }

    }


    private String getEmailUserName(String email) {
        String userName = null;
        String query = "SELECT user_name FROM user_information WHERE user_email = ?";
        try (Connection conn = DataBase.connectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("user_name");
            }else {
                return "nothing";
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR","Error");
        }
        return userName;
    }

    @FXML
    public void updateUserPassword() {

        if (newPassword.getText().isEmpty()) {
            //  showAlert("Please enter a password", false);
            ErrorAlert.displayCustomAlert("information", "Please enter a password");
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
            return;
        }
        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_information SET user_pass = ? WHERE user_name = ? and user_email = ?")) {
            preparedStatement.setString(1, newPassword.getText());
            preparedStatement.setString(2, userNameFound.getText());
            preparedStatement.setString(3, yourEmail.getText());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                SuccessAlert.displayCustomAlert("SUCCESS", "Password updated successfully");
                passwordShowLabel.setVisible(false);
                toggleButton.setText("Show");
                PasswordInputForm.setVisible(false);
                yourEmail.clear();
            } else {
                ErrorAlert.displayCustomAlert("ERROR", "No user found with given information");
                passwordShowLabel.setVisible(false);
                toggleButton.setText("Show");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        newPassword.clear();
        passwordShowLabel.setVisible(false);
        toggleButton.setText("Show");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OtpSubmitForm.setVisible(false);
        ConfirmUserForm.setVisible(false);
        PasswordInputForm.setVisible(false);
        passwordShowLabel.setVisible(false);
        newPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkPasswordStrength(newValue);
            }
        });
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

    @FXML
    void typePassword(KeyEvent event) {
        passwordShowLabel.textProperty().bind(Bindings.concat(newPassword.getText()));

    }

    @FXML
    void toggleButtonPress(ActionEvent event) {
        if (toggleButton.isSelected()) {
            passwordShowLabel.setVisible(true);
            passwordShowLabel.textProperty().bind(Bindings.concat(newPassword.getText()));
            toggleButton.setText("Hide");
        } else {
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
        }

    }

    private Stage stage;
    private Scene scene;

    @FXML
    private void BackToLogIn(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LogInActivities/LogInController.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        LogInAnimation.LeftOpenAnimation(stage);
        stage.show();

    }
}
