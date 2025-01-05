package com.example.e_shop.UserDashBoard;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.LogInActivities.LogInController;
import com.example.e_shop.LogInActivities.SignUpController;
import com.example.e_shop.MainDashBoard.data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ProfileInformation implements Initializable {

    @FXML
    private AnchorPane main_form;
    @FXML
    private AnchorPane InfoChangeForm;
    @FXML
    private AnchorPane PasswordChangeForm;
    @FXML
    private Button PassCngBtn;
    @FXML
    private Button backToInfoBtn;

    @FXML
    private ImageView image_view;

    @FXML
    private Button import_Button;

    private Image image;
    @FXML
    private TextField full_name;
    @FXML
    private TextField newName;
    @FXML
    private TextField newEmail;

    private String CurrentName = UserDetails.Uname;


    public void ImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            UserDetails.path = file.getAbsolutePath();
            System.out.println(file.getAbsoluteFile());
            image = new Image(file.toURI().toString(), 120, 127, false, true);

            image_view.setImage(image);
        }
    }

    private int updateNameInTable(Connection connection, String tableName, String ColumnName, String Name) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement(
                "UPDATE " + tableName + " SET " + ColumnName + " = ? WHERE user_name = ?");
        updateStatement.setString(1, Name);
        updateStatement.setString(2, CurrentName);
        return updateStatement.executeUpdate();
    }

    @FXML
    public void UpdateImageForUser() {
        if (image == null || UserDetails.path == null) {
            ErrorAlert.displayCustomAlert("ERROR", "Please select an image");
            return;
        }

        try (Connection connection = DataBase.connectDB()) {
            String path = UserDetails.path.replace("\\", "\\\\");
            int rowsAffected1 = updateNameInTable(connection, UserDetails.AccountType, "image", path);
            if (rowsAffected1 > 0) {
                SuccessAlert.displayCustomAlert("SUCCESS", "Image updated successfully");
                updateImage("review_post", "profile_image", path, UserDetails.Uname, "post_uploader");
                updateImage("prod_review", "reviewer_image", path, UserDetails.Uname, "reviewer_username");
                LogInController lg = new LogInController();
                lg.getImageForUser(UserDetails.Uname, "user_information");
                if (LogInController.notfound) {
                    lg.getImageForUser(UserDetails.Uname, "resturent_info");
                }
                if (LogInController.notfound) {
                    lg.getImageForUser(UserDetails.Uname, "deliveryman_info");
                }
            } else {
                ErrorAlert.displayCustomAlert("ERROR", "Failed to update details");
            }

        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "An error occurred while updating details.");
        }
    }

    // Update Full Name
    private void updateImage(String tableName, String columnName, String name, String currentName, String ccc) throws SQLException {
        try {
            // Establish database connection
            Connection connection = DataBase.connectDB();
            PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + ccc + " = ?");
            updateStatement.setString(1, name);
            updateStatement.setString(2, currentName);

            // Execute update
            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Image updated successfully.");
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    @FXML
    public void UpdateFullName() {

        if (full_name.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter your fullname");
            return;
        }
        if (UserDetails.FullName.equals(full_name.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter Different name");
            return;
        }
        String newNameValue = full_name.getText();
        try (Connection connection = DataBase.connectDB()) {
            int rowsAffected = updateNameInTable(connection, UserDetails.AccountType, "full_name", newNameValue);
            if (rowsAffected > 0) {
                UpdateInnerName("review_post", UserDetails.FullName, newNameValue, "full_name");
                UpdateInnerName("message_history", CurrentName, newNameValue, "username");
                UpdateInnerName("prod_review", UserDetails.FullName, newNameValue, "reviewer_fullname");
                // Update successful
                SuccessAlert.displayCustomAlert("SUCCESS", "Full name updated successfully.");
                UserDetails.FullName = newNameValue;
                UserDashBoardController.nickname = newNameValue;

            } else {
                ErrorAlert.displayCustomAlert("ERROR", "Failed to update details.");
            }

        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "An error occurred while updating details.");
        }
    }

    /// UPDATE NAME

    @FXML
    public void UpdateName() {

        if (newName.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter your new  username");
            return;
        }
        if (UserDetails.Uname.equals(newName.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Your Enter same name");
            return;
        }
        String newNameValue = newName.getText();
        try (Connection connection = DataBase.connectDB()) {
            // Check if the new name or email already exists in any of the tables

            PreparedStatement checkStatement1 = connection.prepareStatement("SELECT * FROM user_information WHERE user_name = ?");
            checkStatement1.setString(1, newNameValue);
            ResultSet resultSet1 = checkStatement1.executeQuery();

            // check DeliveryMan table if name or email already exist or not...
            PreparedStatement checkStatement2 = connection.prepareStatement("SELECT * FROM deliveryman_info WHERE user_name = ?");
            checkStatement2.setString(1, newNameValue);
            ResultSet resultSet2 = checkStatement2.executeQuery();

            // check Moderator table if name or email already exist or not...
            PreparedStatement checkStatement3 = connection.prepareStatement("SELECT * FROM resturent_info WHERE user_name = ?");
            checkStatement3.setString(1, newNameValue);
            ResultSet resultSet3 = checkStatement3.executeQuery();

            if (resultSet1.next() || resultSet2.next() || resultSet3.next()) {
                ErrorAlert.displayCustomAlert("ERROR", "Username  already exists");
            } else {
                int rowsAffected1 = updateNameInTable(connection, UserDetails.AccountType, "user_name", newNameValue);
                if (rowsAffected1 > 0) {
                    // Update successful
                    SuccessAlert.displayCustomAlert("SUCCESS", "Username updated successfully.");
                    UpdateInnerName("product", CurrentName, newNameValue, "uname");
                    UpdateInnerName("receipt", CurrentName, newNameValue, "em_username");
                    UpdateInnerName("top_up_history", CurrentName, newNameValue, "username");
                    UpdateInnerName("review_post", CurrentName, newNameValue, "post_uploader");
                    UpdateInnerName("prod_review", CurrentName, newNameValue, "reviewer_username");
                    UpdateInnerName("prod_review", CurrentName, newNameValue, "prod_uploader");
                    getUploader(CurrentName, newNameValue);
                    // Update the current name to the new name
                    UserDetails.Uname = newNameValue;
                    UserDashBoardController.nickname = newNameValue;
                    data.username = newNameValue;
                    CurrentName = newNameValue;


                } else {
                    ErrorAlert.displayCustomAlert("ERROR", "Failed to update details.");
                }
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "An error occurred while updating details.");
        }
    }

    private void UpdateInnerName(String tableName, String PrevName, String NewName, String cName) {
        String sql = "UPDATE " + tableName + " SET " + cName + " = ? WHERE " + cName + " = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, NewName);
            preparedStatement.setString(2, PrevName);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("successfully updated with " + tableName);
            } else {
                System.out.println("Row was not found in " + tableName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating database: " + e.getMessage(), e);
        }
    }

    public void getUploader(String prevName, String newName) {
        String query = "SELECT em_username, uploader FROM receipt";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String buyer = resultSet.getString("em_username");
                String uploader = resultSet.getString("uploader");
                String[] arr = uploader.split(" ");

                // Iterate through the array and update if necessary
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals(prevName)) {
                        arr[i] = newName;
                    }
                }

                // Reconstruct the uploader string
                String updatedUploader = String.join(" ", arr);

                // Update the database
                updateUp(buyer, updatedUploader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUp(String name, String updatedUploader) {
        String sql = "UPDATE receipt SET uploader = ? WHERE em_username = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, updatedUploader);
            preparedStatement.setString(2, name);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Successfully updated for buyer: " + name);
            } else {
                System.out.println("Row was not found for buyer: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating database: " + e.getMessage(), e);
        }
    }


    /// UPDATE EMAIL

    @FXML
    public void UpdateEmail() {

        SignUpController SN = new SignUpController();

        if (!SN.isValidEmail(newEmail.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Invalid Email Format");
            return;
        }
        if (UserDetails.Email.equals(newEmail.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "You enter same email");
            return;
        }
        String newEmailValue = newEmail.getText();

        try (Connection connection = DataBase.connectDB()) {
            // Check if the new name or email already exists in any of the tables

            PreparedStatement checkStatement1 = connection.prepareStatement("SELECT * FROM user_information WHERE user_email = ?");
            checkStatement1.setString(1, newEmailValue);
            ResultSet resultSet1 = checkStatement1.executeQuery();

            // check DeliveryMan table if name or email already exist or not...
            PreparedStatement checkStatement2 = connection.prepareStatement("SELECT * FROM deliveryman_info WHERE user_email = ?");
            checkStatement2.setString(1, newEmailValue);
            ResultSet resultSet2 = checkStatement2.executeQuery();

            // check Moderator table if name or email already exist or not...
            PreparedStatement checkStatement3 = connection.prepareStatement("SELECT * FROM resturent_info WHERE user_email = ?");
            checkStatement3.setString(1, newEmailValue);
            ResultSet resultSet3 = checkStatement3.executeQuery();

            if (resultSet1.next() || resultSet2.next() || resultSet3.next()) {
                ErrorAlert.displayCustomAlert("ERROR", "This Email already exists");
            } else {
                int rowsAffected1 = updateNameInTable(connection, UserDetails.AccountType, "user_email", newEmailValue);
                if (rowsAffected1 > 0) {
                    // Update successful
                    SuccessAlert.displayCustomAlert("SUCCESS", "Email updated successfully.");
                    UserDetails.Email = newEmailValue;
                    // Update the current name to the new name
                } else {
                    ErrorAlert.displayCustomAlert("ERROR", "Failed to update details.");
                }

            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "An error occurred while updating details.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        full_name.setText(UserDetails.FullName);
        newName.setText(UserDetails.Uname);
        newEmail.setText(UserDetails.Email);

        String path = "File:" + UserDetails.image;

        image = new Image(path, 120, 127, false, true);
        image_view.setImage(image);
        DisplayPassCNG();
    }

    public void switchForm2(ActionEvent event) {

        if (event.getSource() == PassCngBtn && !PasswordChangeForm.isVisible()) {
            PasswordChangeForm.setVisible(true);
            InfoChangeForm.setVisible(false);
            DisplayPassCNG();
        } else if (event.getSource() == backToInfoBtn && !InfoChangeForm.isVisible()) {
            PasswordChangeForm.setVisible(false);
            InfoChangeForm.setVisible(true);
        }
    }

    @FXML
    private AnchorPane passwordChangeSub;

    private void DisplayPassCNG() {
        try {
            passwordChangeSub.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/UserDashBoard/PasswordChangeController.fxml"));
            AnchorPane pane = loader.load();
            passwordChangeSub.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
