package com.example.e_shop.UserDashBoard;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.EmailSender.Email;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class TopUpController implements Initializable {

    @FXML
    private Label usernamelabel;
    @FXML
    private Label balance;

    @FXML
    private TextField topUpAmount;

    @FXML
    private TextField ConfirmAmount;

    @FXML
    private PasswordField yourCurrentPassword;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Button history_Btn;
    @FXML
    private Button backToTopUp;

    @FXML
    private Label passwordShowLabel;

    @FXML
    private AnchorPane TopUpForm;
    @FXML
    private AnchorPane HistoryForm;

    @FXML
    private TableView<History> h_tableView;
    @FXML
    private TableColumn<History, String> h_col_time;
    @FXML
    private TableColumn<History, String> h_col_date;
    @FXML
    private TableColumn<History, String> h_col_Amount;


    @FXML
    public void TopUp() {
        if (topUpAmount.getText().isEmpty() && ConfirmAmount.getText().isEmpty() && yourCurrentPassword.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please fill the form");
            return;
        }
        double upBalance = Double.parseDouble(topUpAmount.getText());
        if (topUpAmount.getText().isEmpty() || ConfirmAmount.getText().isEmpty() || yourCurrentPassword.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please fill all the fields");
            return;
        }
        if (upBalance < 0) {
            ErrorAlert.displayCustomAlert("ERROR", "You Can not add negative amount");
            return;
        }
        if (!topUpAmount.getText().equals(ConfirmAmount.getText())) {
            ErrorAlert.displayCustomAlert("ERROR", "Please Confirm your  amount");
            return;
        }

        try (Connection connection = DataBase.connectDB();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT user_pass, user_balance FROM user_information WHERE user_name = ?");
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE user_information SET user_balance = ? WHERE user_name = ? and user_pass = ?")) {

            selectStatement.setString(1, UserDetails.Uname);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String storedCurrentPass = resultSet.getString("user_pass");
                double storeBalance = resultSet.getDouble("user_balance");

                if (storedCurrentPass.equals(yourCurrentPassword.getText())) {
                    // double upBalance = Double.parseDouble(topUpAmount.getText());

                    // Update the balance
                    double finalBalance = storeBalance + upBalance;
                    updateStatement.setDouble(1, finalBalance);
                    updateStatement.setString(2, UserDetails.Uname);
                    updateStatement.setString(3, yourCurrentPassword.getText());
                    if (AcceptAlert.displayCustomAlert("Are you Sure to top up ?")) {

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            UserDetails.balance = finalBalance;
                            balance.setText(String.valueOf(finalBalance) + " TK");
                            SuccessAlert.displayCustomAlert("SUCCESS", "Top Up successful");
                            insertTopUpHistory(upBalance);
                            topUpAmount.clear();
                            ConfirmAmount.clear();
                            yourCurrentPassword.clear();
                            toggleButton.setText("Show");
                            passwordShowLabel.setVisible(false);
                            String showBl = "Hi " + UserDetails.Uname + "\n\nBalance added in your account\n\nNewly added balance: " + upBalance +
                                    " Tk\nTotal balance: " + finalBalance + " Tk\n\n\nThank You\nStay with us\nMy Shop";

                            // Send Email After Top Up.
                            // sendEmailShowBalance(showBl, "My Shop", UserDetails.Email);

                        } else {
                            ErrorAlert.displayCustomAlert("ERROR", "Failed to update balance");
                        }
                    } else {
                        ErrorAlert.displayCustomAlert("ERROR", "You Cancel payment process");
                    }
                } else {
                    ErrorAlert.displayCustomAlert("ERROR", "Incorrect password");
                }
            } else {
                ErrorAlert.displayCustomAlert("ERROR", "No user found with given name");
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "Error updating balance. Please try again.");
        }
    }

    public void sendEmailShowBalance(String text, String subject, String to) {
        String from = "shopmine847@gmail.com"; // Update with your Gmail address

        Email email = new Email();

        int result = email.sendEmail(to, from, subject, text);

        if (result == 1) {
            System.out.println("Success: Email sent successfully.");

        } else {
            System.out.println("Error: Failed to send email.");
        }
    }

    public void insertTopUpHistory(Double amount) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Connect to the database (Assuming SQLite for this example)
            conn = DataBase.connectDB();

            // Prepare SQL statement
            String sql = "INSERT INTO top_up_history (time, date, amount, username) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));

            // Set parameters
            pstmt.setString(1, stringTimes);
            pstmt.setString(2, String.valueOf(sqlDate));
            pstmt.setDouble(3, amount);
            pstmt.setString(4, UserDetails.Uname);

            // Execute the insert statement
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


    @FXML
    private void Cancel() {
        if (topUpAmount.getText().isEmpty() && ConfirmAmount.getText().isEmpty() && yourCurrentPassword.getText().isEmpty()) {
            return;
        }
        topUpAmount.clear();
        ConfirmAmount.clear();
        yourCurrentPassword.clear();
        toggleButton.setText("Show");
        passwordShowLabel.setVisible(false);
        ErrorAlert.displayCustomAlert(" info", "You cancel your payment process");
    }

    @FXML
    void toggleButtonPress(ActionEvent event) {
        if (toggleButton.isSelected()) {
            passwordShowLabel.setVisible(true);
            passwordShowLabel.textProperty().bind(Bindings.concat(yourCurrentPassword.getText()));
            toggleButton.setText("Hide");
        } else {
            passwordShowLabel.setVisible(false);
            toggleButton.setText("Show");
        }

    }

    @FXML
    void typePassword(KeyEvent event) {
        passwordShowLabel.textProperty().bind(Bindings.concat(yourCurrentPassword.getText()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernamelabel.setText(UserDetails.Uname);
        balance.setText(String.valueOf(UserDetails.balance) + " TK");
        passwordShowLabel.setVisible(false);
        TopUpHistoryShowData();

    }

    public void switchForm2(ActionEvent event) {

        if (event.getSource() == history_Btn && !HistoryForm.isVisible()) {
            HistoryForm.setVisible(true);
            TopUpForm.setVisible(false);
            TopUpHistoryShowData();
        } else if (event.getSource() == backToTopUp && !TopUpForm.isVisible()) {
            HistoryForm.setVisible(false);
            TopUpForm.setVisible(true);
        }
    }

    private ObservableList<History> TopUpHistory() {
        Connection connect;
        PreparedStatement prepare;
        ResultSet result;

        ObservableList<History> listData = FXCollections.observableArrayList();
        String sql = "SELECT time, date, amount FROM top_up_history WHERE username = ?";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, UserDetails.Uname);
            result = prepare.executeQuery();
            History TUH;

            while (result.next()) {
                TUH = new History(result.getString("time"),
                        result.getDate("date"),
                        result.getDouble("amount"));
                listData.add(TUH);
            }

        } catch (Exception e) {
            ErrorAlert.displayCustomAlert("ERROR", "ERROR");
        }
        return listData;
    }

    private ObservableList<History> TopUpD;

    public void TopUpHistoryShowData() {
        TopUpD = TopUpHistory();
        h_col_time.setCellValueFactory(new PropertyValueFactory<>("Time"));
        h_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        h_col_Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        h_tableView.setItems(TopUpD);
    }


}
