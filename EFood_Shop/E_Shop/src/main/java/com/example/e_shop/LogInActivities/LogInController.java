package com.example.e_shop.LogInActivities;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.Animation.BookPage;
import com.example.e_shop.Animation.LogInAnimation;
import com.example.e_shop.Animation.LogoutAnimation;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MainDashBoard.data;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    private Scene scene;
    private Stage stage;

    @FXML
    private TextField username;
    @FXML
    private PasswordField user_password;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private Label passwordShowLabel;



    @FXML
    public void LogInPress(ActionEvent event) throws IOException, SQLException {
        if (username.getText().isEmpty() && user_password.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter username and password");
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
            return;
        } else {
            if (username.getText().isEmpty()) {
                ErrorAlert.displayCustomAlert("ERROR", "Please enter your username");
                passwordShowLabel.setVisible(false);
                toggleButton.setText("Show");
                return;
            }
            if (user_password.getText().isEmpty()) {
                ErrorAlert.displayCustomAlert("ERROR", "Please enter your password");
                passwordShowLabel.setVisible(false);
                toggleButton.setText("Show");
                return;
            }
        }
        String Name = username.getText();
        String password = user_password.getText();
        boolean isAdmin = Name.equals("admin") && password.equals("admin");
        boolean isValidUser = validateForUser();
        boolean isDeliveryMan = validateForDeliveryMan();
        boolean isResturent = validateForResturent();

        if (!isValidUser && !isAdmin && !isDeliveryMan && !isResturent) {
            ErrorAlert.displayCustomAlert("Error", "Invalid username or password");
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
        } else if (isAdmin) {
            //Admin Dashboard
            String path = "F:\\vecteezy_3d-icons-occupation-job-avatar-for-social-media-profile_28169662.png";
            path = path.replace("\\", "\\\\");
            UserDetails.image=path;

            data.username = username.getText();
            UserDetails.Uname = username.getText();
            UserDetails.AccountType="admin";
            UserDetails.FullName="Main Admin";
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/MainDashBoard/AdminDashBoardController.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            LogInAnimation.RightOpenAnimation(stage);
            clear();
        } else if (isDeliveryMan) {
            UserDetails.AccountType="deliveryman_info";
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/DeliveryMan/DeliveryManDashBoard.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            LogInAnimation.RightOpenAnimation(stage);
            clear();
        } else if (isValidUser) {
            UserDetails.Uname = username.getText();
            UserDetails.AccountType="user_information";
            getImageForUser(username.getText(), "user_information");
            getEmailAndFullNameForUser("user_information");
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("UserDashBoard/UserDashBoardController.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            LogInAnimation.RightOpenAnimation(stage);
            clear();
        } else if (isResturent) {
            data.username = username.getText();
            UserDetails.Uname = username.getText();
            UserDetails.AccountType="resturent_info";
            getImageForUser(username.getText(), "resturent_info");
            getEmailAndFullNameForUser("resturent_info");
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/AddResturent/ResturentDashBoardController.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            LogInAnimation.RightOpenAnimation(stage);
            clear();
        }

    }

    void clear() {
        username.clear();
        user_password.clear();
    }

    public static boolean notfound = false;

    public void getImageForUser(String name, String table) {
        String image = null;

        try (Connection connection = DataBase.connectDB()) {
            String query = "SELECT image FROM " + table + " WHERE user_name = ?";
            // Execute query to retrieve email based on name
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Retrieve email data from the result set
                image = resultSet.getString("image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserDetails.image = image;
        notfound = false;
        if (image == null) {
            notfound = true;
        }
        // System.out.println(image);


    }

    public void getEmailAndFullNameForUser(String table) {
        String email = null;
        String fullName = null;
        String query = "SELECT user_email , full_name FROM " + table + " WHERE user_name = ?";

        try (Connection connection = DataBase.connectDB()) {
            // Execute query to retrieve email based on name
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username.getText());
            ResultSet resultSet = statement.executeQuery();

            // Check if the result set has any rows
            if (resultSet.next()) {
                fullName = resultSet.getString("full_name");
                email = resultSet.getString("user_email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserDetails.FullName = fullName;
        UserDetails.Email = email;
        //System.out.println(email);


    }


    // Check username password for User
    public boolean validateForUser() {
        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_information WHERE user_name = ? and user_pass = ?");
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, user_password.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Username and password are correct
                UserDetails.balance = resultSet.getDouble("user_balance");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Check id password for DeliveryMan
    public boolean validateForDeliveryMan() {
        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM deliveryman_info WHERE user_name = ? and user_pass = ?");
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, user_password.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Username and password are correct
                // UserDetails.balance = resultSet.getDouble("user_balance");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // check user for Resturent or not.....
    public boolean validateForResturent() {
        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resturent_info WHERE user_name = ? and user_pass = ?");
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, user_password.getText());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }


    @FXML
    private void SignUpPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LogInActivities/SignUpController.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        LogInAnimation.RightOpenAnimation(stage);
        stage.show();


    }


    @FXML
    private void ForgotPassword(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LogInActivities/ForgetController.fxml"));
        scene = new Scene(fxmlLoader.load());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        LogInAnimation.RightOpenAnimation(stage);
        stage.show();

    }
    @FXML
    void toggleButtonPress(ActionEvent event) {
        if (toggleButton.isSelected()) {
            passwordShowLabel.setVisible(true);
            passwordShowLabel.textProperty().bind(Bindings.concat(user_password.getText()));
            toggleButton.setText("Hide");
        } else {
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
        }

    }


    @FXML
    void typePassword(KeyEvent event) {
        passwordShowLabel.textProperty().bind(Bindings.concat(user_password.getText()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //setUpEnterKeyHandler();
        passwordShowLabel.setVisible(false);

    }


}


