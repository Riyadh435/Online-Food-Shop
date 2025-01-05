package com.example.e_shop.MainDashBoard;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.LogInActivities.SignUpController;
import com.example.e_shop.MessagesHandler.controller.ClientFormController;
import com.example.e_shop.UserDashBoard.History;
import com.example.e_shop.UserDashBoard.UserDashBoardController;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class AdminDashBoardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button Review_bn;

    @FXML
    private Button customers_btn;

    @FXML
    private Button userDetails_btn;
    @FXML
    private Button message_btn;
    @FXML
    private Button newsfeed_btn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;

    @FXML
    private TableColumn<productData, String> inventory_col_type;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_date;
    @FXML
    private TableColumn<productData, String> inventory_col_discount_status;
    @FXML
    private TableColumn<productData, String> inventory_col_discount_percent;


    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private TextArea inventory_productDetails;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TextField inventory_price;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private ComboBox<?> inventory_type;
    @FXML
    private ComboBox<?> inventory_discountStatus;
    @FXML
    private TextField discount_percent;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane customers_form;

    @FXML
    private AnchorPane userDetails_form;
    @FXML
    private AnchorPane Message_form;
    @FXML
    private AnchorPane Review_form;
    @FXML
    private AnchorPane NewsFeed_form;

    @FXML
    private TableView<customersData> customers_tableView;


    @FXML
    private TableColumn<customersData, String> customers_col_total;

    @FXML
    private TableColumn<customersData, String> customers_col_date;

    @FXML
    private TableColumn<customersData, String> customers_col_cashier;

    @FXML
    private TableColumn<customersData, String> customers_col_productDetails;

    @FXML
    private TableColumn<customersData, String> customers_col_purchaseTime;
    @FXML
    private TableColumn<customersData, String> customers_col_qn;

    @FXML
    private Label dashboard_NC;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TotalI;

    @FXML
    private Label dashboard_NSP;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private BarChart<?, ?> dashboard_CustomerChart;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;


    //Resturent Details.....
    @FXML
    private TableView<AllUserInformation> resturent_tableView;
    @FXML
    private TableColumn<AllUserInformation, String> resturent_col_Name;
    @FXML
    private TableColumn<AllUserInformation, String> resturent_col_Email;
    @FXML
    private TableColumn<AllUserInformation, String> resturent_col_BlockStatus;

    @FXML
    private TableView<AllUserInformation> Users_tableView;
    @FXML
    private TableColumn<AllUserInformation, String> Users_col_Name;
    @FXML
    private TableColumn<AllUserInformation, String> User_col_Email;


    public void dashboardDisplayNC() {
        String sql = "SELECT COUNT(DISTINCT buyer) AS buyer_count FROM Buy_history WHERE uploader = ?";
        Connection connect = null;
        PreparedStatement prepare = null;
        ResultSet result = null;

        try {
            connect = DataBase.connectDB();
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, data.username);
            result = prepare.executeQuery();

            int nc = 0;
            if (result.next()) {
                nc = result.getInt("buyer_count");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Today Sell
    public void dashboardDisplayTodaysIncome() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        //System.out.println(sqlDate);

        String sql = "SELECT SUM(price) AS total_income FROM Buy_History WHERE date = ? AND uploader = ?";

        connect = DataBase.connectDB();

        try {
            double totalIncome = 0;
            prepare = connect.prepareStatement(sql);
            prepare.setDate(1, sqlDate);
            prepare.setString(2, data.username);
            result = prepare.executeQuery();

            if (result.next()) {
                totalIncome = result.getDouble("total_income");
            }

            dashboard_TI.setText(totalIncome + " Tk");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dashboardTotalI() {
        String sql = "SELECT SUM(price) FROM Buy_History WHERE uploader = ?";

        connect = DataBase.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, data.username);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(price)");
            }
            dashboard_TotalI.setText(ti + " Tk");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardNSP() {

        String sql = "SELECT SUM(quantity) FROM Buy_history WHERE uploader = ?";

        connect = DataBase.connectDB();

        try {
            int q = 0;
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, data.username);
            result = prepare.executeQuery();

            if (result.next()) {
                q = result.getInt("SUM(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(q));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date, SUM(price) FROM Buy_history WHERE uploader = ? GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = DataBase.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1,data.username);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart() {
        dashboard_CustomerChart.getData().clear();
        String sql1 = "SELECT date, COUNT(DISTINCT buyer) FROM Buy_history WHERE uploader = ? GROUP BY date ORDER BY TIMESTAMP(date)";

        connect = DataBase.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql1);
            prepare.setString(1,data.username);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboard_CustomerChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void LoadTable(){
        inventoryShowData();

    }

    public void inventoryAddBtn() {

        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_productDetails.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || inventory_discountStatus.getSelectionModel().getSelectedItem() == null
                || data.path == null) {
            ErrorAlert.displayCustomAlert("ERROR", "Please fill all blank fields");

        } else {

            // CHECK PRODUCT ID
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '"
                    + inventory_productID.getText() + "'";

            connect = DataBase.connectDB();

            try {

                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    ErrorAlert.displayCustomAlert("ERROR", inventory_productID.getText() + " is already taken");
                } else {
                    String insertData = "INSERT INTO product "
                            + "(prod_id, prod_name, type, stock, price, status, image, date,prod_details,uname,time,block,prod_parent,discount_status,discount_percent) "
                            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, (String) inventory_type.getSelectionModel().getSelectedItem());
                    prepare.setString(4, inventory_stock.getText());
                    prepare.setString(5, inventory_price.getText());
                    prepare.setString(6, (String) inventory_status.getSelectionModel().getSelectedItem());

                    String path = data.path;
                    path = path.replace("\\", "\\\\");

                    prepare.setString(7, path);

                    // TO GET CURRENT DATE
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.setString(9, inventory_productDetails.getText());
                    // Set uploader name.
                    prepare.setString(10, username.getText());
                    String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
                    prepare.setString(11, stringTimes);
                    prepare.setString(12, "No");
                    prepare.setString(13, "Admin");
                    prepare.setString(14, (String) inventory_discountStatus.getSelectionModel().getSelectedItem());
                    if (((String) inventory_discountStatus.getSelectionModel().getSelectedItem()).equals("Off")) {
                        prepare.setString(15, "0");
                    } else if (((String) inventory_discountStatus.getSelectionModel().getSelectedItem()).equals("On")) {
                        prepare.setString(15, discount_percent.getText());
                    }

                    prepare.executeUpdate();
                    SuccessAlert.displayCustomAlert("Success", "This product Successfully Added!");

                    inventoryShowData();
                    inventoryClearBtn();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void inventoryUpdateButton() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_productDetails.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || inventory_discountStatus.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {
            ErrorAlert.displayCustomAlert("ERROR", "Please fill all blank fields");
        } else {
            String blockStatus = "No";
            String path = data.path;
            path = path.replace("\\", "\\\\");
            String stringTime = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));

            String updateData = "UPDATE product SET prod_id = ?, prod_name = ?, type = ?, stock = ?," +
                    " price = ?, status = ?, image = ?, date = ?, prod_details = ?, uname = ?, " +
                    "time = ?, block = ?, prod_parent = ?,discount_status =?,discount_percent =? WHERE id = ?";

            connect = DataBase.connectDB();

            try {
                if (AcceptAlert.displayCustomAlert("Are you sure to update ?")) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, inventory_type.getSelectionModel().getSelectedItem().toString());
                    prepare.setString(4, inventory_stock.getText());
                    prepare.setString(5, inventory_price.getText());
                    prepare.setString(6, inventory_status.getSelectionModel().getSelectedItem().toString());
                    prepare.setString(7, path);
                    prepare.setString(8, data.date);
                    prepare.setString(9, inventory_productDetails.getText());
                    prepare.setString(10, username.getText());
                    prepare.setString(11, stringTime);
                    prepare.setString(12, blockStatus);
                    prepare.setString(13, "Admin");
                    prepare.setString(14, (String) inventory_discountStatus.getSelectionModel().getSelectedItem());
                    if (((String) inventory_discountStatus.getSelectionModel().getSelectedItem()).equals("Off")) {
                        prepare.setString(15, "0");
                    } else if (((String) inventory_discountStatus.getSelectionModel().getSelectedItem()).equals("On")) {
                        prepare.setString(15, discount_percent.getText());
                    }

                    prepare.setInt(16, data.id);

                    prepare.executeUpdate();
                    SuccessAlert.displayCustomAlert("Success", "Successfully Updated!");
                    inventoryShowData();
                    inventoryClearBtn();
                } else {
                    ErrorAlert.displayCustomAlert("ERROR", "You cancelled the process");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public void inventoryUpdateBtn() {
//
//        if (inventory_productID.getText().isEmpty()
//                || inventory_productName.getText().isEmpty()
//                || inventory_type.getSelectionModel().getSelectedItem() == null
//                || inventory_stock.getText().isEmpty()
//                || inventory_price.getText().isEmpty()
//                || inventory_productDetails.getText().isEmpty()
//                || inventory_status.getSelectionModel().getSelectedItem() == null
//                || data.path == null || data.id == 0) {
//            ErrorAlert.displayCustomAlert("ERROR", "Please fill all blank fields");
//
//        } else {
//            String parent = "Admin";
//
//            String blockStatus = "No";
//            String path = data.path;
//            path = path.replace("\\", "\\\\");
//            String stringTime = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
//
//            String updateData = "UPDATE product SET "
//                    + "prod_id = '" + inventory_productID.getText() + "', prod_name = '"
//                    + inventory_productName.getText() + "', type = '"
//                    + inventory_type.getSelectionModel().getSelectedItem() + "', stock = '"
//                    + inventory_stock.getText() + "', price = '" + inventory_price.getText() + "', status = '"
//                    + inventory_status.getSelectionModel().getSelectedItem() + "', image = '"
//                    + path + "', date = '" + data.date + "', prod_details = '" + inventory_productDetails.getText() +
//                    "', uname = '" + username.getText() + "', time = '" + stringTime + "', block = '" + blockStatus + "',prod_parent= '" + parent + "' WHERE id = " + data.id;
//
//            connect = DataBase.connectDB();
//
//            try {
//                if (AcceptAlert.displayCustomAlert("Are you sure to update ?")) {
//                    prepare = connect.prepareStatement(updateData);
//                    prepare.executeUpdate();
//                    SuccessAlert.displayCustomAlert("SUCCESS", "Product successfully Updated!");
//                    // TO UPDATE YOUR TABLE VIEW
//                    inventoryShowData();
//                    // TO CLEAR YOUR FIELDS
//                    inventoryClearBtn();
//                } else {
//                    ErrorAlert.displayCustomAlert("information", "You cancelled the process");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void inventoryDeleteBtn() {
        if (data.id == null) {
            ErrorAlert.displayCustomAlert("Information", "Please Select item to delete");
        } else {
            if (AcceptAlert.displayCustomAlert("Are you sure to delete ?")) {
                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    SuccessAlert.displayCustomAlert("SUCCESS", "Product id " + data.id + " successfully Deleted!");
                    // TO UPDATE YOUR TABLE VIEW
                    inventoryShowData();
                    // TO CLEAR YOUR FIELDS
                    inventoryClearBtn();

                } catch (Exception e) {
                    ErrorAlert.displayCustomAlert("ERROR", "something went wrong");
                }
            } else {
                data.id = null;
                ErrorAlert.displayCustomAlert("ERROR", "You cancelled the process");
            }
        }
    }

    public void inventoryClearBtn() {

        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_type.getSelectionModel().clearSelection();
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_productDetails.setText("");
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);

    }

    // A BEHAVIOR FOR IMPORT BUTTON FIRST
    public void inventoryImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 120, 127, false, true);

            inventory_imageView.setImage(image);
        }
    }

    // MERGE ALL Data
    public ObservableList<productData> inventoryDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql1 = "SELECT id, prod_id, prod_name, type, stock, price, status, image, date, prod_details,discount_status,discount_percent FROM product WHERE uname = ?";

        try {
            connect = DataBase.connectDB();
            prepare = connect.prepareStatement(sql1);
            prepare.setString(1, username.getText());
            result = prepare.executeQuery();

            while (result.next()) {
                productData prodData = new productData(
                        result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"),
                        result.getString("prod_details"),
                        result.getString("discount_status"),
                        result.getInt("discount_percent")
                );

                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    // TO SHOW DATA ON TABLE
    private ObservableList<productData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        inventory_col_discount_status.setCellValueFactory(new PropertyValueFactory<>("discountStatus"));
        inventory_col_discount_percent.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));

        inventory_tableView.setItems(inventoryListData);

    }


    public void inventorySelectData() {

        productData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(prodData.getProductId());
        inventory_productName.setText(prodData.getProductName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));
        inventory_productDetails.setText(prodData.getProductDetails());

        data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        data.date = String.valueOf(prodData.getDate());
        data.id = prodData.getId();

        image = new Image(path, 120, 127, false, true);
        inventory_imageView.setImage(image);
    }


    private String[] typeList = {"Meals", "Drinks", "Desert", "Snacks"};

    public void inventoryTypeList() {

        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {

        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);

    }

    private String[] DiscountstatusList = {"On", "Off"};

    public void inventoryDiscountStatusList() {

        List<String> statusL = new ArrayList<>();

        for (String data : DiscountstatusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_discountStatus.setItems(listData);

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


    private ObservableList<customersData> customersListData;


    private ObservableList<customersData> customersDataList() {
        ObservableList<customersData> listData = FXCollections.observableArrayList();
        String sql = "SELECT prod_id,prod_name,quantity,price,date,buyer,time FROM Buy_history  WHERE uploader = ?";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            // Assuming you want to search for the word "admin" in the em_username column
            prepare.setString(1, data.username);
            result = prepare.executeQuery();

            customersData cData;

            while (result.next()) {
                cData = new customersData(result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("quantity"),
                        result.getString("price"),
                        result.getString("date"),
                        result.getString("buyer"),
                        result.getString("time"));

                listData.add(cData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void customersShowData() {
        customersListData = customersDataList();


        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("price"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("buyer"));
        customers_col_productDetails.setCellValueFactory(new PropertyValueFactory<>("prod_name"));
        customers_col_purchaseTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        customers_col_qn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        customers_tableView.setItems(customersListData);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn && !dashboard_form.isVisible()) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            customers_form.setVisible(false);
            userDetails_form.setVisible(false);
            Message_form.setVisible(false);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(false);


            dashboardDisplayNC();
            dashboardDisplayTodaysIncome();
            dashboardTotalI();
            dashboardNSP();

            dashboardIncomeChart();
            dashboardCustomerChart();

        } else if (event.getSource() == inventory_btn && !inventory_form.isVisible()) {
            data.id = null;
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            customers_form.setVisible(false);
            userDetails_form.setVisible(false);
            Message_form.setVisible(false);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(false);

            inventoryTypeList();
            inventoryStatusList();
            inventoryDiscountStatusList();
            inventoryShowData();
        } else if (event.getSource() == customers_btn && !customers_form.isVisible()) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customers_form.setVisible(true);
            userDetails_form.setVisible(false);
            Message_form.setVisible(false);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(false);
            customersShowData();
        } else if (event.getSource() == userDetails_btn && !userDetails_form.isVisible()) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customers_form.setVisible(false);
            userDetails_form.setVisible(true);
            Message_form.setVisible(false);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(false);
            BlockStatus();
            ResturentDetailsShowData();
            UsersDetailsShowData();

        } else if (event.getSource() == message_btn && !Message_form.isVisible()) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customers_form.setVisible(false);
            userDetails_form.setVisible(false);
            Message_form.setVisible(true);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(false);
            menuDisplayChat();

        } else if (event.getSource() == Review_bn && !Review_form.isVisible()) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customers_form.setVisible(false);
            userDetails_form.setVisible(false);
            Message_form.setVisible(false);
            Review_form.setVisible(true);
            NewsFeed_form.setVisible(false);
            DisplayReview();
        }else if (event.getSource() == newsfeed_btn && !NewsFeed_form.isVisible()) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            customers_form.setVisible(false);
            userDetails_form.setVisible(false);
            Message_form.setVisible(false);
            Review_form.setVisible(false);
            NewsFeed_form.setVisible(true);
            DisplayReview();
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
            ShoppingCart.clearShoppingCart();

        }
    }

    public void displayUsername() {

        String user = data.username;
        username.setText(user);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();
        dashboardDisplayNC();
        dashboardDisplayTodaysIncome();
        dashboardTotalI();
        dashboardNSP();
        dashboardIncomeChart();
        dashboardCustomerChart();

        inventoryTypeList();
        inventoryStatusList();
        inventoryDiscountStatusList();
        inventoryShowData();


        customersShowData();
        BlockStatus();
        ResturentDetailsShowData();
        UsersDetailsShowData();


        menuDisplayChat();
        UserDashBoardController.nickname = UserDetails.FullName;

        DisplayReview();
        DisplayNewsFeed();


    }

    @FXML
    private void clearAllUserData() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM receipt");
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                System.out.println("v");
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM receipt");
                statement.executeUpdate("ALTER TABLE receipt AUTO_INCREMENT = 1");
                System.out.println("Sucessfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ////////////////////////  others part...
    // Delete user,deliveryman , Resturent account.....


    @FXML
    private AnchorPane DeleteInformation;
    @FXML
    private AnchorPane OthersInformation;
    @FXML
    private Button userB;
    @FXML
    private Button otherB;

    public void switchForm2(ActionEvent event) {

        if (event.getSource() == userB && !DeleteInformation.isVisible()) {
            DeleteInformation.setVisible(true);
            OthersInformation.setVisible(false);


        } else if (event.getSource() == otherB && !OthersInformation.isVisible()) {
            DeleteInformation.setVisible(false);
            OthersInformation.setVisible(true);
            ResturentDetailsShowData();
            UsersDetailsShowData();
        }
    }

    @FXML
    private TextField deleteUsername;

    @FXML
    private RadioButton del_UserBtn;
    @FXML
    private RadioButton del_ResturentBtn;
    @FXML
    private RadioButton del_DeliverymanBtn;

    private String del_Account_Type;

    public void GetDeleteType() {
        if (del_UserBtn.isSelected()) {
            del_Account_Type = "user";

        } else if (del_ResturentBtn.isSelected()) {
            del_Account_Type = "resturent";

        } else if (del_DeliverymanBtn.isSelected()) {
            del_Account_Type = "deliveryman";

        }

    }

    // Delete Account...


    @FXML
    private void deleteAccountFrom_user_Resturent_deliveryman() {
        if (deleteUsername.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter an existing username");
            return;
        }
        if (del_Account_Type == null) {
            ErrorAlert.displayCustomAlert("ERROR", "Please select account type");
            return;
        }

        boolean userFound = false;
        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement deleteStatement = null;
            String tableName = "";

            switch (del_Account_Type) {
                case "user":
                    tableName = "user_information";
                    break;
                case "resturent":
                    tableName = "resturent_info";
                    break;
                case "deliveryman":
                    tableName = "deliveryman_info";
                    break;
                default:
                    ErrorAlert.displayCustomAlert("ERROR", "Invalid account type selected");
                    return;
            }

            // Check if the user exists in the corresponding table
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE user_name = ?");
            checkStatement.setString(1, deleteUsername.getText());


            if (AcceptAlert.displayCustomAlert("Are you sure to delete ?")) {
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    userFound = true;
                    // Delete the user from the corresponding table
                    deleteStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE user_name = ?");
                    deleteStatement.setString(1, deleteUsername.getText());
                    deleteStatement.executeUpdate();
                    SuccessAlert.displayCustomAlert("Success", "Account name " + deleteUsername.getText() + " deleted successfully");
                    if (tableName.equals("resturent_info")) {
                        deleteResturentProductsFromProductsWhenDeleteRes();
                    }
                    UsersDetailsShowData();
                    ResturentDetailsShowData();
                    deleteUsername.clear();
                }
                if (!userFound) {
                    ErrorAlert.displayCustomAlert("ERROR", "Username not found in " + del_Account_Type);

                }
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "something went wrong");
        }
    }

    private void deleteResturentProductsFromProductsWhenDeleteRes() {
        boolean userFound = false;
        try (Connection connection = DataBase.connectDB()) {
            PreparedStatement deleteStatement = null;

            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM product WHERE uname = ?");
            checkStatement.setString(1, deleteUsername.getText());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                userFound = true;
                // Delete the user from the corresponding table
                deleteStatement = connection.prepareStatement("DELETE FROM product WHERE uname = ?");
                deleteStatement.setString(1, deleteUsername.getText());
                deleteStatement.executeUpdate();
                //System.out.println("Delete all products uploaded by " + deleteUsername.getText());
            }
            if (!userFound) {
                ErrorAlert.displayCustomAlert("ERROR", "Username not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField db_fullName;
    @FXML
    private TextField db_username;
    @FXML
    private TextField db_pass;
    @FXML
    private TextField db_email;


    //Some needed thing for radio button...
    @FXML
    RadioButton DeliveryBtn;
    @FXML
    RadioButton ResturentBtn;

    private String AccountType;

    public void getTypeOfAccount() {
        if (DeliveryBtn.isSelected()) {
            AccountType = "db";

        } else if (ResturentBtn.isSelected()) {
            AccountType = "rstrnt";
        }

    }
    /// CREATE ACCOUNT...

    @FXML
    private void createResturentAccount() {
        SignUpController sn = new SignUpController();
        if (AccountType == null) {
            ErrorAlert.displayCustomAlert("information", "Please select account Type");
            return;
        }
        if (db_fullName.getText().isEmpty() || db_username.getText().isEmpty() || db_pass.getText().isEmpty() || db_email.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Looks like you forgot something");
            return;
        }
        boolean foundValidEmail = sn.isValidEmail(db_email.getText());
        if (!foundValidEmail) {
            ErrorAlert.displayCustomAlert("ERROR", "Invalid email format");
            return;
        }
        sn.signUpDetails(db_fullName.getText(), db_username.getText(), db_pass.getText(), db_email.getText(), AccountType);
        db_fullName.clear();
        db_email.clear();
        db_username.clear();
        db_pass.clear();
    }

    //    @FXML
//    private void clearAllDelivery() {
//        try (Connection connection = DataBase.connectDB();
//             Statement statement = connection.createStatement()) {
//            // Check if there are any users in the database
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM deliveryman_info");
//            resultSet.next();
//            int userCount = resultSet.getInt(1);
//            if (userCount == 0) {
//                System.out.println("v");
//            } else {
//                // Delete all user data
//                statement.executeUpdate("DELETE FROM deliveryman_info");
//                statement.executeUpdate("ALTER TABLE deliveryman_info AUTO_INCREMENT = 1");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // Display Resturent Details and user details..

    private ObservableList<AllUserInformation> ResturentDataList() {

        ObservableList<AllUserInformation> listData = FXCollections.observableArrayList();
        String sql = "SELECT user_name, user_email, block_status FROM resturent_info ";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            AllUserInformation ResturentData;
            while (result.next()) {
                ResturentData = new AllUserInformation(result.getString("user_name"),
                        result.getString("user_email"),
                        result.getString("block_status"));
                listData.add(ResturentData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<AllUserInformation> ResListData;

    public void ResturentDetailsShowData() {
        ResListData = ResturentDataList();
        resturent_col_Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        resturent_col_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        resturent_col_BlockStatus.setCellValueFactory(new PropertyValueFactory<>("blockStatus"));
        resturent_tableView.setItems(ResListData);
    }

    // Show Users Details....

    private ObservableList<AllUserInformation> UserDataList() {

        ObservableList<AllUserInformation> listData = FXCollections.observableArrayList();
        String sql = "SELECT user_name, user_email FROM user_information ";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            AllUserInformation UserData;
            while (result.next()) {
                UserData = new AllUserInformation(result.getString("user_name"),
                        result.getString("user_email"));
                listData.add(UserData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<AllUserInformation> UserListData;

    public void UsersDetailsShowData() {
        UserListData = UserDataList();
        Users_col_Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        User_col_Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Users_tableView.setItems(UserListData);
    }

    // Block RESTURENT.....
    @FXML
    private TextField resName;

    @FXML
    private ComboBox<?> block_status;

    @FXML
    public void BlockResturent() {
        String st = (String) block_status.getSelectionModel().getSelectedItem();
        UpdateBlockStatus(resName.getText(), st);
    }


    private void UpdateBlockStatus(String Name, String status) {
        String sql = "UPDATE product SET block = ? WHERE uname = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, Name);


            if (AcceptAlert.displayCustomAlert("Are you sure to block?")) {
                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    // System.out.println("Restaurant " + Name + " has been successfully updated with status " + status + ".");
                    block_status.getSelectionModel().clearSelection();
                    resName.clear();
                    SuccessAlert.displayCustomAlert("SUCCESS", "successfully updated !");
                    UpdateBlockStFromResturent(Name, status);
                } else {
                    System.out.println("Restaurant " + Name + " was not found in the database.");
                    ErrorAlert.displayCustomAlert("ERROR", "Not found in the database !");
                }
            } else {
                ErrorAlert.displayCustomAlert("Information", "Cancel the process");
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "Error updating restaurant status: ");
        }
    }

    private void UpdateBlockStFromResturent(String Name, String status) {
        String sql = "UPDATE resturent_info SET block_status = ? WHERE user_name = ?";

        try (Connection connection = DataBase.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, Name);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println(" successfully updated with status " + status + ".");
            } else {
                System.out.println("was not found in the database.");

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating restaurant status: " + e.getMessage(), e);
        }
    }

    private String[] BlockList = {"No", "Yes"};

    public void BlockStatus() {

        List<String> BList = new ArrayList<>();

        for (String data : BlockList) {
            BList.add(data);
        }

        ObservableList BData = FXCollections.observableArrayList(BList);
        block_status.setItems(BData);

    }

    ///Message Form ........:)
    private void menuDisplayChat() {
        Message_form.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/MessagesHandler/view/ClientForm.fxml"));
            AnchorPane pane = loader.load();
            ClientFormController controller = loader.getController();
            controller.setClientName(UserDetails.FullName);
            Message_form.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Display review
    private void DisplayReview() {
        Review_form.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/UserReview/ResturentReviewShowDashboard.fxml"));
            AnchorPane pane = loader.load();
            Review_form.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void DisplayNewsFeed() {
        NewsFeed_form.getChildren().clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/e_shop/NewsFeed/FeedDashBoard.fxml"));
            AnchorPane pane = loader.load();
            NewsFeed_form.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}