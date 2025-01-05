package com.example.e_shop.UserDashBoard;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.AlertController.ErrorAlertController;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.Animation.LogoutAnimation;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.EmailSender.Email;
import com.example.e_shop.EmailSender.Send_Email;
import com.example.e_shop.LogInActivities.LogInController;
import com.example.e_shop.MainDashBoard.*;
import com.example.e_shop.MessagesHandler.controller.ClientFormController;
import com.example.e_shop.NewsFeed.PostCard;
import com.example.e_shop.NewsFeed.PostData;
import com.example.e_shop.NewsFeed.RVdata;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class UserDashBoardController implements Initializable {

    public static String nickname = "";
    @FXML
    private AnchorPane main_form;
    @FXML
    private Circle myprofile_pic;


    // 4 Form....

    @FXML
    private AnchorPane menuForm;
    @FXML
    private AnchorPane TopUpForm;
    @FXML
    private AnchorPane ChatForm;
    @FXML
    private AnchorPane HistoryForm;
    @FXML
    private AnchorPane ChangeInfoForm;
    @FXML
    private AnchorPane ReviewForm;
    @FXML
    private AnchorPane NewsFeedForm;

    @FXML
    private Label username;


    // 4 button...main form

    @FXML
    private Button menu_btn;
    @FXML
    private Button TopUp_Btn;
    @FXML
    private Button Chat_Btn;
    @FXML
    private Button History_Btn;
    @FXML
    private Button SettingPass_Btn;
    @FXML
    private Button Review_Btn;
    @FXML
    private Button NewsFeed_Btn;

    @FXML
    private Label menu_total;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Label userBalance;

    @FXML
    private AnchorPane orderChartPane;


    //History table
    @FXML
    private TableView<History> history_tableView;
    @FXML
    private TableColumn<History, String> history_col_total;
    @FXML
    private TableColumn<History, String> history_col_date;
    @FXML
    private TableColumn<History, String> history_col_productDetails;
    @FXML
    private TableColumn<History, String> history_col_time;


    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    ///Show product part wise...

    //All product....
    @FXML
    private AnchorPane AllItemForm;
    @FXML
    private Button ALLItemButton;
    @FXML
    private ScrollPane menu_scrollPane;
    @FXML
    private GridPane menu_gridPane;


    // All Meals//
    @FXML
    private AnchorPane MealsForm;
    @FXML
    private Button MealsItemButton;
    @FXML
    private GridPane Meals_gridPane;

    //Drinks..
    @FXML
    private AnchorPane DrinksForm;
    @FXML
    private Button DrinksItemButton;
    @FXML
    private GridPane Drinks_gridPane;
    //Desert
    @FXML
    private AnchorPane DesertsForm;
    @FXML
    private Button DesertItemButton;
    @FXML
    private GridPane Desert_gridPane;

    // Snacks
    @FXML
    private AnchorPane SnacksForm;
    @FXML
    private Button SnacksItemButton;
    @FXML
    private GridPane Snacks_grid_pane;

    // Search pane....
    @FXML
    private TextField userSearch;
    private ObservableList<productData> AllcardListData = FXCollections.observableArrayList();
    private ObservableList<productData> MealscardListData = FXCollections.observableArrayList();
    private ObservableList<productData> DrinkscardListData = FXCollections.observableArrayList();
    private ObservableList<productData> DesertscardListData = FXCollections.observableArrayList();
    private ObservableList<productData> SnackscardListData = FXCollections.observableArrayList();

    public ObservableList<productData> menuGetData() {

        String sql = "SELECT id, prod_id, prod_name, type, stock, price, image, date, prod_details, uname,prod_parent,discount_status,discount_percent FROM product WHERE block = ?";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "No");
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"),
                        result.getString("prod_details"),
                        result.getString("uname"),
                        result.getString("prod_parent"),
                        result.getString("discount_status"),
                        result.getInt("discount_percent"));

                listData.add(prod);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public ObservableList<productData> GetDataFromDB(String type) {

        String sql = "SELECT id, prod_id, prod_name, type, stock, price, image," +
                " date, prod_details, uname,prod_parent,discount_status,discount_percent FROM product WHERE block = ? and type=?";

        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "No");
            prepare.setString(2, type);
            result = prepare.executeQuery();

            productData prod;

            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"),
                        result.getString("prod_details"),
                        result.getString("uname"),
                        result.getString("prod_parent"),
                        result.getString("discount_status"),
                        result.getInt("discount_percent"));

                listData.add(prod);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private void displayProducts(ObservableList<productData> dataList, GridPane gridPane, ObservableList<productData> FindPdocuctfromDb) {
        dataList.clear();
        dataList.addAll(FindPdocuctfromDb);

        int row = 0;
        int column = 0;

        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        for (int q = dataList.size() - 1; q >= 0; q--) {

            // for (int q = 0; q < dataList.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/MainDashBoard/cardProductController.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(dataList.get(q));

                if (column == 2) {
                    column = 0;
                    row += 1;
                }
                //pane.setStyle("-fx-background-color: transparent;");

                gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(8));

            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }

    public void switchMealsButtons(ActionEvent event) {
        if (event.getSource() == ALLItemButton) {
            showAllItems();
        } else if (event.getSource() == MealsItemButton && !MealsForm.isVisible()) {
            showMeals();
        } else if (event.getSource() == DrinksItemButton && !DrinksForm.isVisible()) {
            showDrinks();
        } else if (event.getSource() == DesertItemButton && !DesertsForm.isVisible()) {
            showDeserts();
        } else if (event.getSource() == SnacksItemButton && !SnacksForm.isVisible()) {
            showSnacks();
        }
    }

    private void showAllItems() {
        AllItemForm.setVisible(true);
        MealsForm.setVisible(false);
        DrinksForm.setVisible(false);
        DesertsForm.setVisible(false);
        SnacksForm.setVisible(false);
        displayProducts(AllcardListData, menu_gridPane, menuGetData());
    }

    private void showMeals() {
        AllItemForm.setVisible(false);
        MealsForm.setVisible(true);
        DrinksForm.setVisible(false);
        DesertsForm.setVisible(false);
        SnacksForm.setVisible(false);
        displayProducts(MealscardListData, Meals_gridPane, GetDataFromDB("Meals"));
    }

    private void showDrinks() {
        AllItemForm.setVisible(false);
        MealsForm.setVisible(false);
        DrinksForm.setVisible(true);
        DesertsForm.setVisible(false);
        SnacksForm.setVisible(false);
        displayProducts(DrinkscardListData, Drinks_gridPane, GetDataFromDB("Drinks"));
    }

    private void showDeserts() {
        AllItemForm.setVisible(false);
        MealsForm.setVisible(false);
        DrinksForm.setVisible(false);
        DesertsForm.setVisible(true);
        SnacksForm.setVisible(false);
        displayProducts(DesertscardListData, Desert_gridPane, GetDataFromDB("Desert"));
    }

    private void showSnacks() {
        AllItemForm.setVisible(false);
        MealsForm.setVisible(false);
        DrinksForm.setVisible(false);
        DesertsForm.setVisible(false);
        SnacksForm.setVisible(true);
        displayProducts(SnackscardListData, Snacks_grid_pane, GetDataFromDB("Snacks"));
    }


    private void searchProductDemo1(String searchTerm) {
        // Define a SQL query to search for products based on the search term
        String sql = "SELECT id, prod_id, prod_name, type, stock, price, image, " +
                "date, prod_details, uname, prod_parent, discount_status, discount_percent " +
                "FROM product " +
                "WHERE block = ? AND (prod_name LIKE ? OR type LIKE ? OR prod_parent LIKE ?)";

        ObservableList<productData> searchResults = FXCollections.observableArrayList();
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "No");
            prepare.setString(2, "%" + searchTerm + "%");
            prepare.setString(3, "%" + searchTerm + "%");
            prepare.setString(4, "%" + searchTerm + "%");
            result = prepare.executeQuery();

            while (result.next()) {
                productData prod = new productData(
                        result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"),
                        result.getString("prod_details"),
                        result.getString("uname"),
                        result.getString("prod_parent"),
                        result.getString("discount_status"),
                        result.getInt("discount_percent"));

                searchResults.add(prod);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display search results in the appropriate grid pane
        if (AllItemForm.isVisible()) {
            displayProducts(AllcardListData, menu_gridPane, searchResults);
        } else if (MealsForm.isVisible()) {
            displayProducts(MealscardListData, Meals_gridPane, searchResults);
        } else if (DrinksForm.isVisible()) {
            displayProducts(DrinkscardListData, Drinks_gridPane, searchResults);
        } else if (DesertsForm.isVisible()) {
            displayProducts(DesertscardListData, Desert_gridPane, searchResults);
        } else if (SnacksForm.isVisible()) {
            displayProducts(SnackscardListData, Snacks_grid_pane, searchResults);
        }
    }

    private void searchProductsDemo2(String searchTerm) {
        // Define a SQL query to search for products based on the search term
        String sql = "SELECT id, prod_id, prod_name, type, stock, price, image, " +
                "date, prod_details, uname, prod_parent, discount_status, discount_percent " +
                "FROM product " +
                "WHERE block = ? AND (" +
                "LOWER(prod_name) LIKE CONCAT('%', LOWER(?), '%') OR " +
                "LOWER(type) LIKE CONCAT('%', LOWER(?), '%') OR " +
                "LOWER(prod_parent) LIKE CONCAT('%', LOWER(?), '%'))";

        ObservableList<productData> searchResults = FXCollections.observableArrayList();
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "No");
            prepare.setString(2, searchTerm);
            prepare.setString(3, searchTerm);
            prepare.setString(4, searchTerm);
            result = prepare.executeQuery();

            while (result.next()) {
                String prodName = result.getString("prod_name").toLowerCase();
                String type = result.getString("type").toLowerCase();
                String prodParent = result.getString("prod_parent").toLowerCase();

                // Calculate the length of matched part
                int nameMatchLength = longestCommonSubstringLength(prodName, searchTerm.toLowerCase());
                int typeMatchLength = longestCommonSubstringLength(type, searchTerm.toLowerCase());
                int parentMatchLength = longestCommonSubstringLength(prodParent, searchTerm.toLowerCase());
                int maxMatchLength = Math.max(nameMatchLength, Math.max(typeMatchLength, parentMatchLength));

                // Match if at least 50% of the search term is found
                if (maxMatchLength >= 0.4 * searchTerm.length()) {
                    productData prod = new productData(
                            result.getInt("id"),
                            result.getString("prod_id"),
                            result.getString("prod_name"),
                            result.getString("type"),
                            result.getInt("stock"),
                            result.getDouble("price"),
                            result.getString("image"),
                            result.getDate("date"),
                            result.getString("prod_details"),
                            result.getString("uname"),
                            result.getString("prod_parent"),
                            result.getString("discount_status"),
                            result.getInt("discount_percent"));

                    searchResults.add(prod);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display search results in the appropriate grid pane
        if (AllItemForm.isVisible()) {
            displayProducts(AllcardListData, menu_gridPane, searchResults);
        } else if (MealsForm.isVisible()) {
            displayProducts(MealscardListData, Meals_gridPane, searchResults);
        } else if (DrinksForm.isVisible()) {
            displayProducts(DrinkscardListData, Drinks_gridPane, searchResults);
        } else if (DesertsForm.isVisible()) {
            displayProducts(DesertscardListData, Desert_gridPane, searchResults);
        } else if (SnacksForm.isVisible()) {
            displayProducts(SnackscardListData, Snacks_grid_pane, searchResults);
        }
    }

    // Method to calculate the length of the longest common substring
    private int longestCommonSubstringLength(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        int maxLength = 0;
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    maxLength = Math.max(maxLength, dp[i][j]);
                }
            }
        }
        return maxLength;
    }


    // Event handler for the search button
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = userSearch.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchProductsDemo2(searchTerm);
        } else {
            // If the search field is empty, display all products
            if (AllItemForm.isVisible()) {
                showAllItems();
            } else if (MealsForm.isVisible()) {
                showMeals();
            } else if (DrinksForm.isVisible()) {
                showDrinks();
            } else if (DesertsForm.isVisible()) {
                showDeserts();
            } else if (SnacksForm.isVisible()) {
                showSnacks();
            }
        }
        userSearch.clear();
    }

    private void enterButtonClick() {
        userSearch.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleSearch(null); // Pass null since ActionEvent is not used
            }
        });
    }

    private ObservableList<productData> menuOrderListData = FXCollections.observableArrayList();


    public ObservableList<productData> menuGetOrder() {
        //customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer";

        connect = DataBase.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prod;
            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getDate("date"),
                        result.getString("uploader"),
                        result.getString("prod_image"));
                listData.add(prod);
            }
            // System.out.println(listData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    @FXML
    GridPane ordergrid;
    @FXML
    private AnchorPane orderForm;


    public void displayOrderedItem() {
        menuOrderListData.clear();
        menuOrderListData.addAll(menuGetOrder());

        int row = 0;
        int column = 0;

        ordergrid.getChildren().clear();
        ordergrid.getRowConstraints().clear();
        ordergrid.getColumnConstraints().clear();

        for (int q = 0; q < menuOrderListData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/MainDashBoard/OrderCard.fxml"));
                AnchorPane pane = load.load();
                OrderCard od = load.getController();
                od.setData(menuOrderListData.get(q));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }
                // pane.setStyle("-fx-background-color: transparent;");

                ordergrid.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(4));

            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }


    private double totalPrice;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;

        connect = DataBase.connectDB();

        try {

            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()) {
                totalPrice = result.getDouble("SUM(price)");
                if (totalPrice > 0) {
                    totalPrice_withFinal = totalPrice + 80.0;
                } else if (totalPrice == 0.0) {
                    totalPrice_withFinal = totalPrice;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText(totalPrice + " Tk");
        if (totalPrice > 0) {
            deliveryCrg.setText("80.0 Tk");
        } else {
            deliveryCrg.setText("0.0 Tk");
        }

        TotalFinal.setText(totalPrice_withFinal + " Tk");
    }

    @FXML
    private Label TotalFinal;
    @FXML
    private Label deliveryCrg;

    private double totalPrice_withFinal;


    @FXML
    private void menuPayButton() {
        refreshOrder();
        double amount;
        if (totalPrice == 0) {
            ErrorAlert.displayCustomAlert(" INFO.", "Please choose your order first");
            return;
        }
        double balance = UserDetails.balance;
        if (totalPrice_withFinal > balance) {
            ErrorAlert.displayCustomAlert("Error", "Your have not enough balance");
            return;

        }
        balance = balance - totalPrice_withFinal;
        // menu_change.setText(balance + " Tk");
        String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username, product_details,uploader,time) VALUES(?,?,?,?,?,?,?)";
        connect = DataBase.connectDB();
        try {
            if (AcceptAlert.displayCustomAlert("Are you sure to buy?")) {
                customerID();
                UserDetails.balance = balance;
                displayUserInfo();
                updateStoredBalanceAfterBuy(balance);
                prepare = connect.prepareStatement(insertPay);
                prepare.setString(1, String.valueOf(cID));
                prepare.setString(2, String.valueOf(totalPrice_withFinal));
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(3, String.valueOf(sqlDate));
                prepare.setString(4, UserDetails.Uname);


                /// merge all the order....
                StringBuilder productDetails = new StringBuilder();
                StringBuilder prodUploaders = new StringBuilder();
                for (productData prod : menuOrderListData) {
                    productDetails.append("[P:").append(prod.getProductName()).append(",")
                            .append("Q:").append(prod.getQuantity()).append("]");
                    prodUploaders.append(prod.getProductUploader()).append(" ");
                }
                String productDetailsString = productDetails.toString();
                String productUploaderString = prodUploaders.toString();
                //System.out.println(productDetailsString);
                prepare.setString(5, productDetailsString);

                String trimUpload = removeDuplicates(productUploaderString);
                System.out.println();
                prepare.setString(6, trimUpload);

                //Find the current time....
                String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
                prepare.setString(7, stringTimes);
                prepare.executeUpdate();
                DataBase.exportData(UserDetails.Uname);


                // update in main database..
                ShoppingCart.updateProductQuantities();
                ShoppingCart.clearShoppingCart();


                // Send email.after shopping
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = sdf.format(sqlDate);
                String details = "Hi " + UserDetails.Uname + "\n\nThank You for shopping.\n\nYour current balance: " + balance + " Tk\n\nInformation:\nTotal price: " + totalPrice_withFinal +
                        " Tk\nTime: " + stringTimes + "\nDate: " + dateString + "\n" +
                        "Product:\n" + productDetails + "\n\n\nStay with us.\nMy shop";

                SuccessAlert.displayCustomAlert("Success", "Payment successful");
                menuRestart();
                clearChartList();
                refreshOrder();
                // Send Email After Buying..
                // send(details, "My Shop", UserDetails.Email);
            } else {
                ErrorAlert.displayCustomAlert(" INFO.", "Payment cancelled.");
                //menuRestart();
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "Error occurred while processing payment.");

        }
    }

    public String removeDuplicates(String inputString) {
        // Split the input string into words
        String[] words = inputString.split(" ");

        Set<String> uniqueWords = new HashSet<>();
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!uniqueWords.contains(word)) {
                result.append(word).append(" ");
                uniqueWords.add(word);
            }
        }
        return result.toString().trim();
    }

    public void send(String text, String subject, String to) {
        String from = "shopmine847@gmail.com"; // Update with your Gmail address

        Email email = new Email();

        int result = email.sendEmail(to, from, subject, text);

        if (result == 1) {
            System.out.println("Success: Email sent successfully.");

        } else {
            System.out.println("Error: Failed to send email.");
        }
    }

    void updateStoredBalanceAfterBuy(double bl) {
        try (Connection connection = DataBase.connectDB();
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE user_information SET user_balance = ? WHERE user_name = ?")) {

            // Set the parameters for the prepared statement
            updateStatement.setDouble(1, bl); // Set the new balance
            updateStatement.setString(2, UserDetails.Uname); // Set the username

            // Execute the update query
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Backend balance updated successfully.");
            } else {
                System.out.println("Failed to update backend balance.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void menuRestart() {
        totalPrice = 0;
        menu_total.setText("0.0 Tk");
        //menu_change.setText("0.0 Tk");
        totalPrice_withFinal = 0;
        TotalFinal.setText("0.0 Tk");
    }

    private int cID;

    public void customerID() {

        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            }
//            else if (cID == checkID) {
//                cID += 1;
//            }

            data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage stage;
    private Scene scene;

    @FXML
    private void logout(ActionEvent event) throws IOException {
        if (AcceptAlert.displayCustomAlert("Are you sure to log out ?")) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            clearChartList();
            ShoppingCart.clearShoppingCart();
            LogoutAnimation.addCloseAnimation(stage);

        }


    }

    public void displayUserInfo() {
        String user = UserDetails.Uname;
        username.setText(user);
        double balance = UserDetails.balance;
        System.out.println(balance);
        userBalance.setText(" " + String.valueOf(balance) + " tk"); // Convert balance to String
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DisplayAdd();
        enterButtonClick();
        refreshImageAndName();

        displayUserInfo();


        //Display....Products
        showAllItems();
        displayOrderedItem();

        menuDisplayTotal();
        MoveTopUp();


        menuDisplayChat();

        userHistoryShowData();
        nickname = UserDetails.FullName;
        MoveToPasswordChange();

        DisplayReview();
        DisplayNewsFeedPost();
    }

    @FXML
    public void refreshOrder() {
        menuDisplayTotal();
        displayOrderedItem();

    }

    public void clearChartList() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM customer");
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                return;
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM customer");
                statement.executeUpdate("ALTER TABLE customer AUTO_INCREMENT = 1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == menu_btn && !menuForm.isVisible()) {
//            menu_btn.setStyle("-fx-background-color: #F453AD; -fx-background-radius: 10 10 10 10;");
//            other_Btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 10 10 10 10;");
            menuForm.setVisible(true);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(false);
            DisplayAdd();

            // menuDisplayCard();
            menuDisplayTotal();
            displayOrderedItem();
        } else if (event.getSource() == TopUp_Btn && !TopUpForm.isVisible()) {
//            other_Btn.setStyle("-fx-background-color: #F453AD;-fx-background-radius: 10 10 10 10;");
//            menu_btn.setStyle("-fx-background-color: transparent; -fx-background-radius: 10 10 10 10;");
            menuForm.setVisible(false);
            TopUpForm.setVisible(true);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(false);
            MoveTopUp();
        } else if (event.getSource() == Chat_Btn && !ChatForm.isVisible()) {
            menuForm.setVisible(false);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(true);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(false);
            menuDisplayChat();
            // nickname=username.getText();
        } else if (event.getSource() == History_Btn && !HistoryForm.isVisible()) {
            menuForm.setVisible(false);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(true);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(false);
            userHistoryShowData();

        } else if (event.getSource() == SettingPass_Btn && !ChangeInfoForm.isVisible()) {
            menuForm.setVisible(false);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(true);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(false);
            MoveToPasswordChange();

        } else if (event.getSource() == Review_Btn && !ReviewForm.isVisible()) {
            menuForm.setVisible(false);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(true);
            NewsFeedForm.setVisible(false);
            DisplayReview();
        } else if (event.getSource() == NewsFeed_Btn && !NewsFeedForm.isVisible()) {
            menuForm.setVisible(false);
            TopUpForm.setVisible(false);
            ChatForm.setVisible(false);
            HistoryForm.setVisible(false);
            ChangeInfoForm.setVisible(false);
            ReviewForm.setVisible(false);
            NewsFeedForm.setVisible(true);
            DisplayNewsFeedPost();
        }
    }
    // Top Up Form

    private void MoveTopUp() {
        TopUpForm.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/UserDashBoard/TopUpController.fxml"));
            AnchorPane pane = loader.load();
            TopUpForm.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Password change Form

    private void MoveToPasswordChange() {
        ChangeInfoForm.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/UserDashBoard/ProfileInformation.fxml"));
            AnchorPane pane = loader.load();
            ChangeInfoForm.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void refreshImageAndName() {
        //Set name;
        username.setText(UserDetails.Uname);

        // set profile image
        String path = "File:" + UserDetails.image;
        myprofile_pic.setStroke(Color.SEAGREEN);
        Image im = new Image(path, false);
        myprofile_pic.setFill(new ImagePattern(im));
        myprofile_pic.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
    }


    ///Learn more... info

    @FXML
    private void learnMore(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("UserDashBoard/learnMore.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        // Create a new stage for the new scene
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);

        // Show the new stage without hiding the previous one
        newStage.show();
    }

    //Chat Form.........
    private void menuDisplayChat() {
        ChatForm.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/MessagesHandler/view/ClientForm.fxml"));
            AnchorPane pane = loader.load();
            ClientFormController controller = loader.getController();
            controller.setClientName(nickname);
            //pane.setStyle("-fx-background-color: transparent;");
            ChatForm.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //User purchase history....
    private ObservableList<History> userDataList() {

        ObservableList<History> listData = FXCollections.observableArrayList();
        String sql = "SELECT total, date,  product_details, time FROM receipt WHERE em_username = ?";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, UserDetails.Uname);
            result = prepare.executeQuery();
            History Userhistory;

            while (result.next()) {
                Userhistory = new History(result.getDouble("total"),
                        result.getDate("date"),
                        result.getString("product_details"),
                        result.getString("time"));
                listData.add(Userhistory);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<History> userListData;

    public void userHistoryShowData() {
        userListData = userDataList();
        history_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        history_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        history_col_productDetails.setCellValueFactory(new PropertyValueFactory<>("productDetails"));
        history_col_time.setCellValueFactory(new PropertyValueFactory<>("purchaseTime"));
        history_tableView.setItems(userListData);
    }

    //
    private void DisplayReview() {
        ReviewForm.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/UserReview/ReviewShowDashboard.fxml"));
            AnchorPane pane = loader.load();
            ReviewForm.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Show NewsFeed;

    @FXML
    GridPane gridPane;
    private ObservableList<PostData> PostListData = FXCollections.observableArrayList();

    private ObservableList<PostData> PostDataList() {
        ObservableList<PostData> listData = FXCollections.observableArrayList();

        String sql1 = "SELECT id, post_uploader, full_name, upload_time, profile_image, prod_image, prod_review_text, post_title FROM review_post";

        try {
            Connection connect = DataBase.connectDB();
            PreparedStatement prepare = connect.prepareStatement(sql1);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                PostData pdata = new PostData(
                        result.getInt("id"),
                        result.getString("post_uploader"),
                        result.getString("full_name"),
                        result.getString("upload_time"),
                        result.getString("profile_image"),
                        result.getString("prod_image"),
                        result.getString("prod_review_text"),
                        result.getString("post_title"));
                listData.add(pdata);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    private void DisplayNewsFeedPost() {
        PostListData.clear();
        PostListData.addAll(PostDataList());

        int row = 0;
        int column = 0;

        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        for (int q = PostListData.size() - 1; q >= 0; q--) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/NewsFeed/PostCard.fxml"));
                AnchorPane pane = load.load();
                PostCard ps = load.getController();
                ps.setData(PostListData.get(q));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(8));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private AnchorPane displayaddform;

    private void DisplayAdd() {
        displayaddform.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/ShowAdd/sampleAdd.fxml"));
           // FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/ShowAdd/sampleAddRec.fxml"));
            AnchorPane pane = loader.load();
            displayaddform.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


