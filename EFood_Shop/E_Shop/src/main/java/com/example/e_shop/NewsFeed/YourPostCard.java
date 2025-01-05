package com.example.e_shop.NewsFeed;

import com.example.e_shop.Alert.AcceptAlert;
import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MainDashBoard.productData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class YourPostCard {
    @FXML
    private AnchorPane main_form;
    @FXML
    private Label full_name;
    @FXML
    private Label post_time;
    @FXML
    private Label post_title;

    @FXML
    private VBox vbox;
    @FXML
    private ImageView prod_pic;

    @FXML
    private Circle mycircle;

    private String profile_image;
    private String product_image;

    private PostData rvData;
    private Image image2;

     private  Integer id;

    public void setData(PostData rvData) {
        this.rvData=rvData;
        id=rvData.getId();


        full_name.setText(rvData.getFull_name());
        post_time.setText(rvData.getUpload_time());
        post_title.setText(rvData.getPost_title());
        profile_image=rvData.getProfile_image();
        product_image=rvData.getProd_image();
        javafx.scene.text.Text text = new Text(rvData.getProd_review_text());
        text.setStyle("-fx-font-family: 'Microsoft Sans Serif'; -fx-font-size: 13;");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0, 0, 0));
        vbox.getChildren().add(textFlow);
        String path1 = "File:" + rvData.getProfile_image();
        String path2 = "File:" + rvData.getProd_image();
        image2 = new Image(path2, 110, 120, false, true);
        setProfilePic(path1);
        prod_pic.setImage(image2);
    }
    void setProfilePic(String path){
        mycircle.setStroke(Color.SEAGREEN);
        Image im = new Image(path,false);
        mycircle.setFill(new ImagePattern(im));
        mycircle.setEffect(new DropShadow(+25d,0d,+2d,Color.DARKSEAGREEN));

    }

    @FXML
    public void PostDeleteBtn() {
        if (id == null) {
            ErrorAlert.displayCustomAlert("ERROR", "Please click  to delete BTN");
        } else {
            if (AcceptAlert.displayCustomAlert("Are you sure to delete ?")) {
                String deleteData = "DELETE FROM review_post WHERE id = " + id;
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
