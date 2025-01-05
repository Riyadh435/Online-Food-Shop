package com.example.e_shop.UserReview;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MainDashBoard.data;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class YourReviewCard {
    @FXML
    private AnchorPane review_card;
    @FXML
    private Label full_name;
    @FXML
    private Circle profile_image;
    @FXML
    private VBox commentvBox;
    @FXML
    private Circle prod_image;
    @FXML
    private Label title;

    private  Integer id;


    private ReviewData rvData;

    public void setData(ReviewData rvData) {
        this.rvData = rvData;
        id = rvData.getId();


        full_name.setText(rvData.getReviewer_fullname());
        title.setText(rvData.getRv_title());
        javafx.scene.text.Text text = new Text(rvData.getProd_review());
        //text.setStyle("-fx-font-size: 13");
        text.setStyle("-fx-font-family: 'Microsoft Sans Serif'; -fx-font-size: 13;");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0, 0, 0));
        commentvBox.getChildren().add(textFlow);
        String path1 = "File:" + rvData.getReviewer_profilePic();
        String path2 = "File:" + rvData.getProd_image();
        setProfilePic(path1, profile_image);
        setProfilePic(path2, prod_image);
    }

    void setProfilePic(String path, Circle mycircle) {
        mycircle.setStroke(Color.SEAGREEN);
        Image im = new Image(path, false);
        mycircle.setFill(new ImagePattern(im));
        mycircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));

    }

    @FXML
    public void ReviewDeleteBtn() {
        if (id == null) {
            ErrorAlert.displayCustomAlert("ERROR", "Please Select product to delete");
        } else {
            if (AcceptAlert.displayCustomAlert("Are you sure to delete ?")) {
                String deleteData = "DELETE FROM prod_review WHERE id = " + id;
                try {
                    Connection connect= DataBase.connectDB();
                    PreparedStatement prepare;
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    SuccessAlert.displayCustomAlert("Success", "successfully Deleted!");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                id = null;
                ErrorAlert.displayCustomAlert("ERROR", "You cancelled the process");
            }
        }
    }

}
