package com.example.e_shop.MainDashBoard;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.NewsFeed.PostData;
import com.example.e_shop.UserDashBoard.UserDashBoardController;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderCard {
    @FXML
    private Circle prodimage;
    @FXML
    private Label price;
    @FXML
    private Label quantity;
    @FXML
    private Label prod_name;
    String prodId;

    private productData proddata;


    public void setData(productData proddata) {
        this.proddata = proddata;
        prodId = proddata.getProductId();


        prod_name.setText(proddata.getProductName());
        quantity.setText(String.valueOf(proddata.getQuantity()));
        price.setText(String.valueOf(proddata.getPrice()));
        String path1 = "File:" + proddata.getImage();
        setProfilePic(path1);
    }

    void setProfilePic(String path) {
        prodimage.setStroke(Color.SEAGREEN);
        Image im = new Image(path, false);
        prodimage.setFill(new ImagePattern(im));
        prodimage.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));

    }

    @FXML
    private void menuRemoveButton() {

        if (prodId == null) {
            ErrorAlert.displayCustomAlert("INFORMATION", "Please select the order you want to remove");
        } else {
            String deleteData = "DELETE FROM customer WHERE prod_id = " + prodId;
            Connection connect = DataBase.connectDB();
            PreparedStatement prepare;
            try {
                if (AcceptAlert.displayCustomAlert("Are you sure want to delete this ?")) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    deleteOrderFromShop(prodId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void delete() {
        String deleteQuery = "DELETE FROM customer WHERE prod_id = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            // Set the product ID parameter
            preparedStatement.setString(1, prodId);
            if (AcceptAlert.displayCustomAlert("Are you sure remove this?")) {

                // Execute the delete query
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Product with ID " + prodId + " deleted successfully.");
                    SuccessAlert.displayCustomAlert("SUCCESS", "Item removed successfully");
                    deleteOrderFromShop(prodId);
                    //new UserDashBoardController().displayOrderedItem();
                } else {
                    ErrorAlert.displayCustomAlert("ERROR","No item found in list");
                    // System.out.println("No product found with ID " + prodId + ".");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

    public void deleteOrderFromShop(String prodId) {
        String deleteQuery = "DELETE FROM shop WHERE prod_id = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            // Set the product ID parameter
            preparedStatement.setString(1, prodId);

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product with ID " + prodId + " deleted successfully.");
            } else {
                System.out.println("No product found with ID " + prodId + ".");
            }

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

}
