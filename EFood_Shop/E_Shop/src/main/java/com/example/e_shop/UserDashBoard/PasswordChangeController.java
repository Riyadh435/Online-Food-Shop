package com.example.e_shop.UserDashBoard;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
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

public class PasswordChangeController implements Initializable {
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private Label passwordAlert;
    @FXML
    private Label newpasswordShowLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private ToggleButton newtoggleButton;

    public String getCurrentPassword(String table) {
        String CurrentPass = null;
        String query = "SELECT user_pass FROM " + table + " WHERE user_name = ?";

        try (Connection connection = DataBase.connectDB()) {
            // Execute query to retrieve email based on name
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UserDetails.Uname);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                CurrentPass = resultSet.getString("user_pass");
            } else {
                ErrorAlert.displayCustomAlert("ERROR", "No username found at this name");
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "ERROR");
        }
        return CurrentPass;
    }

    @FXML
    public void UpdatePassword() {

        if (currentPassword.getText().isEmpty() || newPassword.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please Fill the Form");
            return;
        }
        if (newPassword.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter new password");
            return;
        }
        String cpss = getCurrentPassword(UserDetails.AccountType);
        if (!cpss.equals(currentPassword.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Your current password did not matched");
            return;
        }
        String newNameValue = newPassword.getText();
        try (Connection connection = DataBase.connectDB()) {
            int rowsAffected1 = updateNameInTable(connection, "user_information", "user_pass", newNameValue);
            int rowsAffected2 = updateNameInTable(connection, "resturent_info", "user_pass", newNameValue);
            int rowsAffected3 = updateNameInTable(connection, "deliveryman_info", "user_pass", newNameValue);
            int totalRowsAffected = rowsAffected1 + rowsAffected2 + rowsAffected3;
            if (totalRowsAffected > 0) {
                SuccessAlert.displayCustomAlert("SUCCESS", " Password updated successfully.");
            } else {
                ErrorAlert.displayCustomAlert("ERROR", " Failed to update Password.");
            }

        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "An error occurred while updating details.");
        }
        newpasswordShowLabel.setVisible(false);
        passwordLabel.setVisible(false);
        newtoggleButton.setText("Show");
        currentPassword.clear();
        newPassword.clear();

    }

    private int updateNameInTable(Connection connection, String tableName, String ColumnName, String Name) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE " + tableName + " SET " + ColumnName + " = ? WHERE user_name = ?");
        updateStatement.setString(1, Name);
        updateStatement.setString(2, UserDetails.Uname);
        return updateStatement.executeUpdate();
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
        newpasswordShowLabel.setVisible(false);
        passwordLabel.setVisible(false);
        newPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkPasswordStrength(newValue);
            }
        });
    }
    @FXML
    void newTypePassword(KeyEvent event) {
        newpasswordShowLabel.textProperty().bind(Bindings.concat(newPassword.getText()));
    }
    @FXML
    void TypePassword(KeyEvent event) {
        passwordLabel.textProperty().bind(Bindings.concat(currentPassword.getText()));
    }
    @FXML
    void newToggleButtonPress(ActionEvent event) {
        if (newtoggleButton.isSelected()) {
            newpasswordShowLabel.setVisible(true);
            passwordLabel.setVisible(true);
            newpasswordShowLabel.textProperty().bind(Bindings.concat(newPassword.getText()));
            passwordLabel.textProperty().bind(Bindings.concat(currentPassword.getText()));
            newtoggleButton.setText("Hide");
        } else {
            newpasswordShowLabel.setVisible(false);
            passwordLabel.setVisible(false);
            newtoggleButton.setText("Show");
        }
    }

}

