package com.example.e_shop.NewsFeed;

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

public class PostCard {
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

    public void setData(PostData rvData) {
        this.rvData=rvData;


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
}
