package com.example.e_shop.DataBaseConnection;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.MainDashBoard.data;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class DataBase {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/sample";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "riyadh";

    public static Connection connectDB() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("Error", "Failed to connect with db");
            return null;
        }
    }

    public static void clearHistory(String tableName) {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                System.out.println("All Deleted");
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM " + tableName);
                statement.executeUpdate("ALTER TABLE" + tableName + "AUTO_INCREMENT = 1");
                System.out.println("Delete successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        //clearHistory("Buy_history");
        //exportData("Riyadh");
        //inventoryDeleteBtn();

    }

    public static void exportData(String buyer) {
        String sourceTable = "customer"; // Source table name
        String destinationTable = "Buy_history"; // Destination table name
        String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));

        try (
                // Open connections to source and destination databases
                Connection sourceConnection = connectDB();
                Connection destinationConnection = connectDB();
                // Prepare statements for source query and destination insert
                PreparedStatement sourceStatement = sourceConnection.prepareStatement("SELECT * FROM " + sourceTable);
                PreparedStatement insertStatement = destinationConnection.prepareStatement(
                        "INSERT INTO " + destinationTable + " (prod_id, prod_name, type, quantity, price, date, uploader, buyer,time) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)")
        ) {
            // Execute source query
            try (ResultSet resultSet = sourceStatement.executeQuery()) {
                // Process result set
                while (resultSet.next()) {
                    // Retrieve data from source result set
                    String prod_id = resultSet.getString("prod_id");
                    String prod_name = resultSet.getString("prod_name");
                    String type = resultSet.getString("type");
                    String quantity = resultSet.getString("quantity");
                    double price = resultSet.getDouble("price");
                    String date = resultSet.getString("date");
                    String uploader = resultSet.getString("uploader");

                    // Set parameters for destination insert statement
                    insertStatement.setString(1, prod_id);
                    insertStatement.setString(2, prod_name);
                    insertStatement.setString(3, type);
                    insertStatement.setString(4, quantity);
                    insertStatement.setDouble(5, price);
                    insertStatement.setString(6, date);
                    insertStatement.setString(7, uploader);
                    insertStatement.setString(8, buyer);
                    insertStatement.setString(9, stringTimes);

                    // Execute insert statement
                    insertStatement.executeUpdate();
                }
                System.out.println("Data exported successfully from " + sourceTable + " to " + destinationTable);
            } catch (SQLException e) {
                System.err.println("Error executing source query: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }


    public static void inventoryDeleteBtn() {

        String deleteData = "DELETE FROM review_post WHERE id = ?";
        try {
            Connection connect = connectDB();
            PreparedStatement prepare = connect.prepareStatement(deleteData);
            prepare.setInt(1, 8);
            prepare.executeUpdate();
            SuccessAlert.displayCustomAlert("Success", "successfully Deleted!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}



