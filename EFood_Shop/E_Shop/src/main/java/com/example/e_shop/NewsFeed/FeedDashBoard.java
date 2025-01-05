package com.example.e_shop.NewsFeed;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MainDashBoard.productData;
import com.example.e_shop.UserDashBoard.UserDetails;
import com.example.e_shop.UserReview.ReviewCard;
import com.example.e_shop.UserReview.ReviewData;
import com.example.e_shop.UserReview.YourReviewCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


public class FeedDashBoard implements Initializable {
    @FXML
    private Button NewsFeed_Btn;
    @FXML
    private Button YourPost_Btn;
    @FXML
    private Button AddPost_Btn;

    @FXML
    private AnchorPane NewsFeedForm;
    @FXML
    private AnchorPane AddPostForm;
    @FXML
    private AnchorPane YourPostForm;


    ///Public News Feed
    @FXML
    private ScrollPane NewsFeedScrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane yourGrid;

    public void switchForm(ActionEvent event) {

        if (event.getSource() == NewsFeed_Btn && !NewsFeedForm.isVisible()) {
            NewsFeedForm.setVisible(true);
            AddPostForm.setVisible(false);
            YourPostForm.setVisible(false);
            DisplayPost();

        } else if (event.getSource() == AddPost_Btn && !AddPostForm.isVisible()) {

            NewsFeedForm.setVisible(false);
            AddPostForm.setVisible(true);
            YourPostForm.setVisible(false);
        } else if (event.getSource() == YourPost_Btn && !YourPostForm.isVisible()) {
            NewsFeedForm.setVisible(false);
            AddPostForm.setVisible(false);
            YourPostForm.setVisible(true);
            DisplayYourPost();
            RVdata.id = null;

        }
    }

    private ObservableList<PostData> PostListData = FXCollections.observableArrayList();

    public ObservableList<PostData> PostDataList() {
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


    private void DisplayPost() {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DisplayPost();
        DisplayYourPost();
        setData();
    }

    ///DisplayYour Post


    private ObservableList<PostData> YourPostListData = FXCollections.observableArrayList();

    public ObservableList<PostData> YourPostDataList() {
        ObservableList<PostData> listData = FXCollections.observableArrayList();

        String sql1 = "SELECT id, post_uploader, full_name, upload_time, profile_image, prod_image, prod_review_text, post_title FROM review_post WHERE post_uploader = ?";

        try {
            Connection connect = DataBase.connectDB();
            PreparedStatement prepare = connect.prepareStatement(sql1);
            prepare.setString(1,UserDetails.Uname);
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

    private void DisplayYourPost() {
       YourPostListData.clear();
        YourPostListData.addAll(YourPostDataList());

        int row = 0;
        int column = 0;

        yourGrid.getChildren().clear();
        yourGrid.getRowConstraints().clear();
        yourGrid.getColumnConstraints().clear();

        for (int q = YourPostListData.size() - 1; q >= 0; q--) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/NewsFeed/YourPostCard.fxml"));
                AnchorPane pane = load.load();
                YourPostCard ypc = load.getController();
                ypc.setData(YourPostListData.get(q));

                if (column == 1) {
                    column = 0;
                    row += 1;
                }

                yourGrid.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(8));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void DeleteBtn() {
        try (Connection connection = DataBase.connectDB()) {
            if (RVdata.id == null) {
                ErrorAlert.displayCustomAlert("ERROR", "Please Select post to delete");
            } else {
                if (AcceptAlert.displayCustomAlert("Are you sure to delete ?")) {
                    String deleteData = "DELETE FROM review_post WHERE id = ?";
                    try (PreparedStatement prepare = connection.prepareStatement(deleteData)) {
                        prepare.setInt(1, RVdata.id);
                        int rowsDeleted = prepare.executeUpdate();
                        if (rowsDeleted > 0) {
                            SuccessAlert.displayCustomAlert("Success", "Successfully deleted!");
                            // TO CLEAR YOUR FIELDS

                        } else {
                            ErrorAlert.displayCustomAlert("ERROR", "Failed to delete record");
                        }
                    } catch (SQLException e) {
                        ErrorAlert.displayCustomAlert("ERROR", "An error occurred while deleting record");
                    }
                } else {
                    ErrorAlert.displayCustomAlert("ERROR", "You cancelled the process");
                    RVdata.id = null;
                }
            }
        } catch (SQLException e) {
            ErrorAlert.displayCustomAlert("ERROR", "Database connection error");
        }
    }



    //Add Review. Part.
    @FXML
    private TextField post_title;
    @FXML
    private ImageView prod_image;
    @FXML
    private Button image_imp_btn;
    @FXML
    private TextArea prod_review_text;
    @FXML
    private Button upload_review_btn;

    private String post_uploader;
    private String full_name;
    private String upload_time;
    private String profile_pic;

    private String image_path;
    private Image image;


    private void setData() {
        full_name = UserDetails.FullName;
        post_uploader = UserDetails.Uname;
        profile_pic = UserDetails.image;
        upload_time = getCurrentDateAndTime();

    }

    public String getCurrentDateAndTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
        String TodayDate = currentDate.format(formatter);
        String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
        return TodayDate + " AT " + stringTimes;
    }

    public void ImportBtn() {

        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "*png", "*jpg"));

        File file = openFile.showOpenDialog(AddPostForm.getScene().getWindow());

        if (file != null) {

            image_path = file.getAbsolutePath();
            // System.out.println(file.getAbsoluteFile());
            image = new Image(file.toURI().toString(), 120, 127, false, true);
            prod_image.setImage(image);

        }
    }


    @FXML
    public void AddPost() {
        if (post_title.getText().isEmpty() || full_name.isEmpty() || post_uploader.isEmpty() || profile_pic.isEmpty() || upload_time.isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please fill the form");
            return;
        }
        if (post_title.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", ".....");
            return;
        }
        if (prod_review_text.getText().isEmpty()) {
            ErrorAlert.displayCustomAlert("ERROR", "Please enter your text");
            return;
        }

        try (Connection connection = DataBase.connectDB()) {
            String prod_path = image_path.replace("\\", "\\\\");
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO review_post (post_uploader, full_name, upload_time, profile_image, prod_image, prod_review_text,post_title) VALUES (?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setString(1, post_uploader);
            insertStatement.setString(2, full_name);
            insertStatement.setString(3, upload_time);
            insertStatement.setString(4, profile_pic);
            insertStatement.setString(5, prod_path);
            insertStatement.setString(6, prod_review_text.getText());
            insertStatement.setString(7, post_title.getText());
            insertStatement.executeUpdate();
            SuccessAlert.displayCustomAlert("SUCCESS", "Post Added Successfully");
            prod_review_text.setText("");
            prod_image.setImage(null);
            post_title.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void refresh(){
        DisplayPost();
        DisplayYourPost();

    }


}


