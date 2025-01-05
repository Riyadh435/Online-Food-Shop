package com.example.e_shop.DeliveryMan;

import com.example.e_shop.App;
import com.example.e_shop.MainDashBoard.ShoppingCart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DeliveryManDashBoardController {
    private Stage stage;
    private Scene scene;


    @FXML
    private void logout(ActionEvent event) throws IOException {

        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
        al.setContentText("Are you sure for Log out ?");
        Optional<ButtonType> option = al.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }
}
