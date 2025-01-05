package com.example.e_shop.UserReview;

import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.UserDashBoard.UserDetails;
import com.example.e_shop.UserReview.YourReviewCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class ResturentReviewShowDashBoard implements Initializable {
    @FXML
    private AnchorPane ReviewForm;
    @FXML
    private AnchorPane YourReviewForm;

    @FXML
    private Button yourButton;
    @FXML
    private Button allButton;


    public void switchForm(ActionEvent event) {
        if (event.getSource() == yourButton && !YourReviewForm.isVisible()) {
            YourReviewForm.setVisible(true);
            ReviewForm.setVisible(false);
            DisplayYourReviewPost();


        } else if (event.getSource() == allButton && !ReviewForm.isVisible()) {
            YourReviewForm.setVisible(false);
            ReviewForm.setVisible(true);
            DisplayReviewPost();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(UserDetails.AccountType.equals("resturent_info")){
            ReviewForm.setVisible(false);
            yourButton.setVisible(false);
            allButton.setVisible(false);
        }

        DisplayReviewPost();
        DisplayYourReviewPost();
    }


    ///Show review card.
    private ObservableList<ReviewData> ReviewListData = FXCollections.observableArrayList();
    private ObservableList<ReviewData> YourReviewListData = FXCollections.observableArrayList();


    public ObservableList<ReviewData> ReviewDataList() {
        ObservableList<ReviewData> listData = FXCollections.observableArrayList();

        String sql1 = "SELECT id, reviewer_username, reviewer_fullname, reviewer_image, prod_id, prod_name, prod_comment, prod_image,rv_title,rc FROM prod_review";

        try {
            Connection connect = DataBase.connectDB();
            PreparedStatement prepare = connect.prepareStatement(sql1);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                ReviewData rvd = new ReviewData(
                        result.getInt("id"),
                        result.getString("reviewer_username"),
                        result.getString("reviewer_fullname"),
                        result.getString("reviewer_image"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("prod_comment"),
                        result.getString("prod_image"),
                        result.getString("rv_title"),
                        result.getString("rc"));
                listData.add(rvd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    @FXML
    private GridPane reviewGrid;
    @FXML
    private GridPane yourGrid;

    private void DisplayReviewPost() {
        ReviewListData.clear();
        ReviewListData.addAll(ReviewDataList());

        int row = 0;
        int column = 0;

        reviewGrid.getChildren().clear();
        reviewGrid.getRowConstraints().clear();
        reviewGrid.getColumnConstraints().clear();

        for (int q = ReviewListData.size() - 1; q >= 0; q--) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/UserReview/ReviewCard.fxml"));
                AnchorPane pane = load.load();
                ReviewCard rv = load.getController();
                rv.setData(ReviewListData.get(q));

                if (column == 2) {
                    column = 0;
                    row += 1;
                }

                reviewGrid.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(8));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /// Your postReview
    public ObservableList<ReviewData> GetReviewFromYou() {
        Connection connect;
        PreparedStatement prepare;
        ResultSet result;

        String sql1 = "SELECT id, reviewer_username, reviewer_fullname, reviewer_image, prod_id, prod_name, prod_comment, prod_image,rv_title,rc FROM prod_review WHERE prod_uploader = ?";
        ObservableList<ReviewData> listData = FXCollections.observableArrayList();
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql1);
            prepare.setString(1, UserDetails.Uname);
            result = prepare.executeQuery();

            while (result.next()) {
                ReviewData rvd = new ReviewData(
                        result.getInt("id"),
                        result.getString("reviewer_username"),
                        result.getString("reviewer_fullname"),
                        result.getString("reviewer_image"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("prod_comment"),
                        result.getString("prod_image"),
                        result.getString("rv_title"),
                        result.getString("rc"));
                listData.add(rvd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private void DisplayYourReviewPost() {
        YourReviewListData.clear();
        YourReviewListData.addAll(GetReviewFromYou());

        int row = 0;
        int column = 0;

        yourGrid.getChildren().clear();
        yourGrid.getRowConstraints().clear();
        yourGrid.getColumnConstraints().clear();

        for (int q = YourReviewListData.size() - 1; q >= 0; q--) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/com/example/e_shop/UserReview/ReviewCard.fxml"));
                AnchorPane pane = load.load();
                ReviewCard rv = load.getController();
                rv.setData(YourReviewListData.get(q));

                if (column == 2) {
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
    private void Refresh() {
        DisplayReviewPost();
        DisplayYourReviewPost();
    }


}




