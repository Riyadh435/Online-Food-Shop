package com.example.e_shop.MessagesHandler.controller;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MessagesHandler.emoji.EmojiPicker;
import com.example.e_shop.UserDashBoard.UserDashBoardController;
import com.example.e_shop.UserDashBoard.UserDetails;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClientFormController {

    @FXML
    public AnchorPane pane;
    @FXML
    public ScrollPane scrollPain;
    @FXML
    public VBox vBox;
    @FXML
    public TextField txtMsg;
    @FXML
    public Label txtLabel;
    @FXML
    public Button emojiButton;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String clientName = "";


    @FXML
    public void initialize() {
        enterButtonClick();
        txtLabel.setText(UserDashBoardController.nickname);
        // txtLabel.setText(clientName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3001);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    //This method send previous all message in new client....
                    //sendPreviousMessages(ClientFormController.this.vBox);
                    sendPreviousMessageFromDB(ClientFormController.this.vBox);


                    while (socket.isConnected()) {

                        String receivingMsg = dataInputStream.readUTF();
                        receiveMessage(receivingMsg, ClientFormController.this.vBox, getCurrentTime());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        this.vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollPain.setVvalue((Double) newValue);
            }
        });

        emoji();

    }

    public static String FilterTodayOrNot(String DateTime) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
        String TodayDate = currentDate.format(formatter);
        String[] parts = DateTime.split(" AT ");
        if (parts.length >= 2) {
            String datesPart = parts[0];
            String timesParts = parts[1];
            if (TodayDate.equals(datesPart)) {
                return timesParts;

            } else {
                return DateTime;
            }
        } else {
            return "";
        }
    }


//    public static void sendPreviousMessages(VBox vBox) {
//        try (BufferedReader reader = new BufferedReader(new FileReader("server_store.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("&&");
//                if (parts.length >= 3) {
//                    String username = parts[0];
//                    String message = parts[1];
//                    String mTime = parts[2];  // Assuming the message is after the first '-'
//
//                    if (UserDashBoardController.nickname.equals(username)) {
//                        receiveMessageIfItsYou(line, vBox, FilterTodayOrNot(mTime));
//                    } else {
//                        receiveMessage(line, vBox, FilterTodayOrNot(mTime));
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void sendPreviousMessageFromDB(VBox vBox) {
        try (Connection connection = DataBase.connectDB();
             PreparedStatement statement = connection.prepareStatement("SELECT username, messages FROM message_history");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String MessageAndTime = resultSet.getString("messages");
                String[] parts = MessageAndTime.split("&&");
                if (parts.length >= 2) {
                    String message = parts[0];
                    String mTime = parts[1];
                    if (UserDashBoardController.nickname.equals(username)) {
                        receiveMessageIfItsYou(username + "&&" + message, vBox, FilterTodayOrNot(mTime));
                    } else {
                        receiveMessage(username + "&&" + message, vBox, FilterTodayOrNot(mTime));
                    }
                } else {
                    System.out.println("No chat");
                }
            }
        } catch (Exception e) {
            ErrorAlert.displayCustomAlert("ERROR",".....");
        }
    }

    private void emoji() {
        // Create the EmojiPicker
        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150, 300);
        vBox.setLayoutX(400);
        vBox.setLayoutY(175);
        vBox.setStyle("-fx-font-size: 25");

        pane.getChildren().add(vBox);

        // Set the emoji picker as hidden initially
        emojiPicker.setVisible(false);

        // Show the emoji picker when the button is clicked
        emojiButton.setOnAction(event -> {
            if (emojiPicker.isVisible()) {
                emojiPicker.setVisible(false);
            } else {
                emojiPicker.setVisible(true);
            }
        });

        // Set the selected emoji from the picker to the text field
        emojiPicker.getEmojiListView().setOnMouseClicked(event -> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtMsg.setText(txtMsg.getText() + selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }

    public void sendButtonOnAction(ActionEvent actionEvent) {
        sendMsg(txtMsg.getText());
    }

    private void enterButtonClick() {
        txtMsg.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                sendButtonOnAction(null); // Pass null since ActionEvent is not used
            }
        });
    }

    public String getCurrentDateAndTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
        String TodayDate = currentDate.format(formatter);
        String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
        return TodayDate + " AT " + stringTimes;
    }

    public String getCurrentTime() {
        return LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
    }

    private void sendMsg(String msgToSend) {
        if (!msgToSend.isEmpty()) {
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")) {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));

                Text text = new Text(msgToSend);
                //text.setStyle("-fx-font-size: 12");
                text.setStyle("-fx-font-family: 'Microsoft Sans Serif'; -fx-font-size: 14;");
                TextFlow textFlow = new TextFlow(text);
//              #0693e3 #37d67a #40bf75
                textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                text.setFill(Color.color(1, 1, 1));

                hBox.getChildren().add(textFlow);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));

                String stringDateTime = getCurrentDateAndTime();
                String stringTime = getCurrentTime();

                Text time = new Text(stringTime);
               // time.setStyle("-fx-font-size: 8");
                time.setStyle("-fx-font-family: 'Microsoft Sans Serif';-fx-font-size: 8");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);


                try {
                    //Send message to server...
                    dataOutputStream.writeUTF(clientName + "&&" + msgToSend + "&&" + stringDateTime);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                txtMsg.clear();
            }
        }
    }


    public static void receiveMessage(String msg, VBox vBox, String ReceiveTime) throws IOException {
        if (msg.matches(".*\\.(png|jpe?g|gif)$")) {
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(msg.split("[&&]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);

                }
            });

        } else {
            String username = msg.split("&&")[0];
            String msgFromServer = msg.split("&&")[1];


            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            //hBox.setPrefWidth(vBox.getWidth() * 0.8);
            /// SENDER
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
           // hBox.setPrefWidth(vBox.getWidth() * 0.8);
            Text textName = new Text(username);
            textName.setStyle("-fx-font-family: 'Microsoft Sans Serif';-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #666362;");


            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Text text = new Text(msgFromServer);
            text.setStyle("-fx-font-family: 'Microsoft Sans Serif'; -fx-font-size: 14;");
           // text.setStyle("-fx-font-size: 14");
            TextFlow textFlow = new TextFlow(text);
            // #abb8c3 ; -fx-font-weight: bold
            textFlow.setStyle("-fx-background-color: #abb8c3; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0, 0, 0));
            hBox.getChildren().add(textFlow);

            HBox hBoxTime = new HBox();
            hBoxTime.setAlignment(Pos.CENTER_LEFT);
            hBoxTime.setPadding(new Insets(0, 5, 5, 10));
            Text time = new Text(ReceiveTime);
            time.setStyle("-fx-font-family: 'Microsoft Sans Serif';-fx-font-size: 8");

            hBoxTime.getChildren().add(time);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                    vBox.getChildren().add(hBoxTime);
                }
            });
        }

    }

    public static void receiveMessageIfItsYou(String msg, VBox vBox, String ReceiveTime) throws IOException {
        if (msg.matches(".*\\.(png|jpe?g|gif)$")) {
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_RIGHT);
            Text textName = new Text(msg.split("[&&]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);

                }
            });

        } else {
            String username = msg.split("&&")[0];
            String msgFromServer = msg.split("&&")[1];


            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 0, 10));


            Text text = new Text(msgFromServer);
            //text.setStyle("-fx-font-size: 13");
            text.setStyle("-fx-font-family: 'Microsoft Sans Serif'; -fx-font-size: 14;");
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-background-color: #0693e3; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(1, 1, 1));


            hBox.getChildren().add(textFlow);

            HBox hBoxTime = new HBox();
            hBoxTime.setAlignment(Pos.CENTER_RIGHT);
            hBoxTime.setPadding(new Insets(0, 5, 5, 10));
            Text time = new Text(ReceiveTime);
           // time.setStyle("-fx-font-size: 8");
            time.setStyle("-fx-font-family: 'Microsoft Sans Serif';-fx-font-size: 8");

            hBoxTime.getChildren().add(time);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBox);
                    vBox.getChildren().add(hBoxTime);
                }
            });
        }

    }


    public void setClientName(String name) {
        clientName = name;
        // txtLabel.setText(clientName);
    }
}
