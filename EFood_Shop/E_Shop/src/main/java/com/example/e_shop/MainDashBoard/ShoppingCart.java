package com.example.e_shop.MainDashBoard;

import com.example.e_shop.DataBaseConnection.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    public static List<ShoppingCart> items = new ArrayList<>();

    private String prodID;
    int Upquantity;

    public ShoppingCart(String prodID, int Upquantity) {
        this.prodID = prodID;
        this.Upquantity = Upquantity;
    }

    public String getProdID() {
        return prodID;
    }

    public int getUpQuantity() {
        return Upquantity;
    }


    public void loadShoppingCart() {
        String selectQuery = "SELECT prod_id, select_item FROM shop";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String prodID = resultSet.getString("prod_id");
                int quantity = resultSet.getInt("select_item");
                items.add(new ShoppingCart(prodID, quantity));
            }

        } catch (SQLException e) {
            System.err.println("Error loading shopping cart: " + e.getMessage());
        }
        // System.out.println(items.toString());

        //return items;

    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                ", prodID='" + prodID + '\'' +
                ", Upquantity=" + Upquantity +
                '}';
    }

    public static void updateProductQuantities() {
        String selectQuery = "SELECT stock FROM product WHERE prod_id = ?";
        String updateQuery = "UPDATE product SET stock = ? WHERE prod_id = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            for (ShoppingCart item : items) {
                // Fetch the previous quantity from the database
                selectStatement.setString(1, item.getProdID());
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int previousQuantity = resultSet.getInt("stock");
                    int currentQuantity = item.getUpQuantity();
                    int finalQuantity = previousQuantity - currentQuantity;

                    // Update the stock of the product with the final quantity
                    updateStatement.setInt(1, finalQuantity);
                    updateStatement.setString(2, item.getProdID());
                    updateStatement.addBatch(); // Add the update statement to the batch
                } else {
                    System.out.println("No product found with ID " + item.getProdID());
                }
            }

            // Execute the batch update
            int[] rowsUpdated = updateStatement.executeBatch();
            for (int i = 0; i < rowsUpdated.length; i++) {
                if (rowsUpdated[i] > 0) {
                    System.out.println("Product with ID " + items.get(i).getProdID() + " updated successfully.");
                } else {
                    System.out.println("Failed to update product with ID " + items.get(i).getProdID());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating product quantities: " + e.getMessage());
        }
    }

    public static void clearShoppingCart() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM shop");
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                System.out.println("clear item chart");
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM shop");
                statement.executeUpdate("ALTER TABLE shop AUTO_INCREMENT = 1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

