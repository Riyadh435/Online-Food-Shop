package com.example.e_shop.MainDashBoard;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.UserDashBoard.UserDashBoardController;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class cardProductController implements Initializable {

    private final String selectQueryForShop = "SELECT select_item FROM shop WHERE prod_id = ?";
    private final String selectQueryForCustomer = "SELECT select_item FROM shop WHERE prod_id = ?";

    @FXML
    private AnchorPane card_form;
    @FXML
    private AnchorPane discount_bar;
    @FXML
    private Label discountPercent;
    @FXML
    private Label AfterDiscountPrice;
    @FXML
    private Label prod_parent;
    @FXML
    private Label priceTk;
    @FXML
    private Label priceTk1;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Label prod_details;

    @FXML
    private Line cutline;


    private productData prodData;
    private Image image;

    private String prodID;
    private String type;
    private String prod_date;
    private String prod_image;
    private String prod_uploader;
    private String discount_status;
    private String prod_Uploader;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void setData(productData prodData) {
        this.prodData = prodData;

        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        type = prodData.getType();
        prodID = prodData.getProductId();
        prod_parent.setText(prodData.getProductParent());
        prod_Uploader=prodData.getProductUploader();
        prod_name.setText(prodData.getProductName());
        prod_price.setText(String.valueOf(prodData.getPrice()));



        prod_details.setText(" " + insertLineBreaks(prodData.getProductDetails(), 28)); // Insert line breaks
        String path = "File:" + prodData.getImage();
        image = new Image(path, 110, 120, false, true);
        prod_imageView.setImage(image);
        OriginalPrice = prodData.getPrice();
        prod_uploader = prodData.getProductUploader();
        discount_status = prodData.getDiscountStatus();
        priceTk.setText("Tk");
        if (prodData.getDiscountStatus().equals("Off")) {
            discount_bar.setVisible(false);
            discountPercent.setText("");
            AfterDiscountPrice.setText("");
            priceTk1.setVisible(false);
            cutline.setVisible(false);

        } else if (prodData.getDiscountStatus().equals("On")) {
            cutline.setVisible(true);
            discount_bar.setVisible(true);
            priceTk1.setVisible(true);
            discountPercent.setText(prodData.getDiscountPercent().toString() + " % Off");
            afterDiscountPrice = calculatePriceAfterPercent(prodData.getPrice(), prodData.getDiscountPercent());
            AfterDiscountPrice.setText(String.valueOf(afterDiscountPrice));

        }

    }

    // This method for create next line
    public String insertLineBreaks(String text, int interval) {
        StringBuilder builder = new StringBuilder(text);
        int offset = 0;
        while (offset < builder.length()) {
            offset += interval;
            if (offset < builder.length()) {
                while (offset > 0 && !Character.isWhitespace(builder.charAt(offset))) {
                    offset--;
                }
                if (offset > 0) {
                    builder.insert(offset, '\n');
                    offset++;
                }
            }
        }
        return builder.toString();
    }


    private int qty;


    private int quantity = 0;


    @FXML
    private Label qnlabel;

    @FXML
    public void increaseQuantity() {
        quantity++;
        updateQuantityLabel();
    }

    @FXML
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
            updateQuantityLabel();
        }
    }

    private void updateQuantityLabel() {
        // This label should be in your FXML file
        qnlabel.setText(String.valueOf(quantity));
    }

    private double calculatePriceAfterPercent(double originalPrice, int percent) {
        double percentAmount = originalPrice * (percent / 100.0);
        return originalPrice - percentAmount;
    }


    private double totalP;
    private double OriginalPrice;
    private double afterDiscountPrice;

    public void addBtn() {

        AdminDashBoardController admin = new AdminDashBoardController();
        admin.customerID();

        qty = quantity;
        String check = "";
        String checkAvailable = "SELECT status FROM product WHERE prod_id = '"
                + prodID + "'";

        connect = DataBase.connectDB();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM product WHERE prod_id = '"
                    + prodID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("stock");
            }
            prepare = connect.prepareStatement(checkAvailable);
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }
            if (qty == 0) {
                ErrorAlert.displayCustomAlert(" Info", "Please select your quantity");
            } else {
                int currentSelectstock = getCurrentSelect(prodID, selectQueryForShop);
                // System.out.println("prev selected: " + currentSelectstock);
                if (!check.equals("Available")) {
                    ErrorAlert.displayCustomAlert(" Info.", "Product is currently unavailable");
                    currentSelectstock = 0;
                    qnlabel.setText("0");

                } else if (checkStck < qty || (currentSelectstock + qty) > checkStck) {
                    ErrorAlert.displayCustomAlert(" Info.", "This product is out of stock :)");
                    currentSelectstock = 0;
                    qnlabel.setText("0");

                } else {
                    int currentQuantity = getCurrentSelect(prodID, selectQueryForCustomer);
                    if (currentQuantity >= 0) {
                        int totalquantity = currentQuantity + qty;
                        // Product already exists in the shopping cart, update quantity
                        String updateQuery = "UPDATE customer SET quantity = ?, price = ? WHERE prod_id = ?";
                        prepare = connect.prepareStatement(updateQuery);
                        prepare.setString(1, String.valueOf(totalquantity));
                        if (discount_status.equals("Off")) {
                            totalP = totalquantity * OriginalPrice;
                        } else if (discount_status.equals("On")) {
                            totalP= totalquantity * afterDiscountPrice;
                        }
                        prepare.setString(2, String.valueOf(totalP));
                        prepare.setString(3, prodID);
                        prepare.executeUpdate();
                        // System.out.println("Quantity updated for product with ID " + prodID);

                    } else {
                        // Product does not exist in the shopping cart, insert new row
                        String insertData = "INSERT INTO customer "
                                + "(customer_id, prod_id, prod_name, type, quantity, price, date, uploader,prod_image) "
                                + "VALUES(?,?,?,?,?,?,?,?,?)";
                        prepare = connect.prepareStatement(insertData);
                        prepare.setString(1, String.valueOf(data.cID));
                        prepare.setString(2, prodID);
                        prepare.setString(3, prod_name.getText());
                        prepare.setString(4, type);
                        prepare.setString(5, String.valueOf(qty));
                        if (discount_status.equals("Off")) {
                            totalP = qty * OriginalPrice;
                        } else if (discount_status.equals("On")) {
                            totalP= qty * afterDiscountPrice;
                        }
                        prepare.setString(6, String.valueOf(totalP));

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        prepare.setString(7, String.valueOf(sqlDate));
                        prepare.setString(8, prod_uploader);
                        prepare.setString(9,prod_image);
                        prepare.executeUpdate();

                    }
                    int upStock = checkStck - qty;
                    addToCart(prodID, qty);

                    ShoppingCart chart = new ShoppingCart(prodID, upStock);
                    chart.loadShoppingCart();
                    SuccessAlert.displayCustomAlert("SUCCESS", "This item successfully added.");
                    qty = 0;
                    currentSelectstock = 0;
                    qnlabel.setText("0");
                    quantity = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateQuantityLabel();
    }

    public void addToCart(String prodID, int quantity) {
        String selectQuery = "SELECT * FROM shop WHERE prod_id = ?";
        String insertQuery = "INSERT INTO shop (prod_id, select_item) VALUES (?, ?)";
        String updateQuery = "UPDATE shop SET select_item = ? WHERE prod_id = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            selectStatement.setString(1, prodID);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                //Product already exists in the shopping cart, update quantity
                int currentQuantity = resultSet.getInt("select_item");
                updateStatement.setInt(1, currentQuantity + quantity);
                updateStatement.setString(2, prodID);
                updateStatement.executeUpdate();
                // System.out.println("Quantity updated for product with ID " + prodID);
            } else {
                // Product does not exist in the shopping cart, insert new row
                insertStatement.setString(1, prodID);
                insertStatement.setInt(2, quantity);
                insertStatement.executeUpdate();
                // System.out.println("Product added to shopping cart successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding product to shopping cart: " + e.getMessage());
        }
    }

    public int getCurrentSelect(String prodId, String selectQuery) {
        int currentSelect = -1; // Default value if no quantity is found

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            // Set the product ID parameter
            preparedStatement.setString(1, prodId);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if result set has any rows
            if (resultSet.next()) {
                currentSelect = resultSet.getInt("select_item");
                //System.out.println("count: " + currentSelect);
            } else {
                System.out.println("No data found for product ID: " + prodId);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving product quantity: " + e.getMessage());
        }
        return currentSelect;
    }


    ///Review part...
    @FXML
    private void AddProdReviewButton(ActionEvent event) throws IOException {
        selectInfoWhenSelectItem();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/UserReview/ProductReview.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        // Create a new stage for the new scene
        Stage newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setScene(newScene);
        newStage.setResizable(false);

        // Show the new stage without hiding the previous one
        newStage.show();

    }

    void selectInfoWhenSelectItem() {
        data.prod_idForAddReview = prodID;
        data.prod_nameForAddReview = prod_name.getText();
        data.prod_imageForAddReview = prod_image;
        data.prod_selector_username = UserDetails.Uname;
        data.prod_selector_fullName = UserDetails.FullName;
        data.prod_selector_profile_image = UserDetails.image;
        data.prod_uploader=prod_Uploader;


    }

}

