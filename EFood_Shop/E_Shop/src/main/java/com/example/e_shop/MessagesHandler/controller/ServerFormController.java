package com.example.e_shop.MessagesHandler.controller;

import com.example.e_shop.Alert.SuccessAlert;
import com.example.e_shop.App;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MessagesHandler.server.Server;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServerFormController {
    public VBox vBox;
    public ScrollPane scrollPain;
    public AnchorPane pane;
    private Server server;
    private static VBox staticVBox;

    @FXML
    public void initialize(){
        staticVBox = vBox;
       // receiveMessage("Sever Starting..");
        SuccessAlert.displayCustomAlert("Server","Server started successfully.");
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPain.setVvalue((Double) newValue);
            }
        });

        new Thread(() -> {
            try {
                server = Server.getInstance();
                server.makeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

       // receiveMessage("Sever Running..");
       // receiveMessage("Waiting for User..");
    }

    private void sendMsg(String msgToSend) {
        if (!msgToSend.isEmpty()){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,0,10));

            Text text = new Text(msgToSend);
            text.setStyle("-fx-font-size: 14");
            TextFlow textFlow = new TextFlow(text);

//            #0693e3 #37d67a #40bf75
            textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(1,1,1));

            hBox.getChildren().add(textFlow);

            HBox hBoxTime = new HBox();
            hBoxTime.setAlignment(Pos.CENTER_RIGHT);
            hBoxTime.setPadding(new Insets(0,5,5,10));
            String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            Text time = new Text(stringTime);
            time.setStyle("-fx-font-size: 8");

            hBoxTime.getChildren().add(time);

            vBox.getChildren().add(hBox);
            vBox.getChildren().add(hBoxTime);
        }
    }

    public static void receiveMessage(String msgFromClient){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(msgFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0,0,0));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                staticVBox.getChildren().add(hBox);
            }
        });
    }





    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) throws IOException {
//        Stage stage = new Stage();
//       // stage.initModality(Modality.WINDOW_MODAL);
//        stage.initOwner(pane.getScene().getWindow());
//        try {
//            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"))));
//        } catch (IOException e) {
//            e.printStackTrace();
//            new Alert(Alert.AlertType.ERROR,"something went wrong. can't add client").show();
//        }
//        stage.setResizable(false);
//        stage.show();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/example/e_shop/LogInActivities/LogInController.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());

        // Create a new stage for the new scene
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);

        // Show the new stage without hiding the previous one
        newStage.show();
    }


    @FXML
    public  void clearAllMessageHistory() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM message_history");
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                System.out.println("All Chat Deleted");
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM message_history");
                statement.executeUpdate("ALTER TABLE message_history AUTO_INCREMENT = 1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
       cAllMessageHistory();

    }
    public  static void cAllMessageHistory() {
        try (Connection connection = DataBase.connectDB();
             Statement statement = connection.createStatement()) {
            // Check if there are any users in the database
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM message_history");
            resultSet.next();
            int userCount = resultSet.getInt(1);
            if (userCount == 0) {
                System.out.println("All Chat Deleted");
            } else {
                // Delete all user data
                statement.executeUpdate("DELETE FROM message_history");
                statement.executeUpdate("ALTER TABLE message_history AUTO_INCREMENT = 1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
