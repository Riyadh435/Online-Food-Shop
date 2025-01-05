module com.example.e_shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires emoji.java;


    opens com.example.e_shop to javafx.fxml;
    opens com.example.e_shop.LogInActivities to javafx.fxml;
    opens com.example.e_shop.MainDashBoard to javafx.fxml;
    opens com.example.e_shop.UserDashBoard to javafx.fxml;
    opens com.example.e_shop.DeliveryMan to javafx.fxml;
    opens com.example.e_shop.AddResturent to javafx.fxml;
    opens com.example.e_shop.MessagesHandler.client to javafx.fxml;
    opens com.example.e_shop.MessagesHandler.controller to javafx.fxml;
    opens com.example.e_shop.MessagesHandler.emoji to javafx.fxml;
    opens com.example.e_shop.MessagesHandler.server to javafx.fxml;
    opens com.example.e_shop.NewsFeed to javafx.fxml;
    opens com.example.e_shop.Alert to javafx.fxml;
    opens com.example.e_shop.Alert.AlertController to javafx.fxml;
    opens com.example.e_shop.Animation to javafx.fxml;
    opens com.example.e_shop.UserReview to javafx.fxml;
    opens com.example.e_shop.ShowAdd to javafx.fxml;

    exports com.example.e_shop;
    exports com.example.e_shop.LogInActivities;
    exports com.example.e_shop.DataBaseConnection;
    exports com.example.e_shop.MainDashBoard;
    exports com.example.e_shop.UserDashBoard;
    exports com.example.e_shop.DeliveryMan;
    exports com.example.e_shop.AddResturent;
    exports com.example.e_shop.EmailSender;
    exports com.example.e_shop.MessagesHandler.client;
    exports com.example.e_shop.MessagesHandler.controller;
    exports com.example.e_shop.MessagesHandler.emoji;
    exports com.example.e_shop.MessagesHandler.server;
    exports com.example.e_shop.NewsFeed;
    exports com.example.e_shop.Alert;
    exports com.example.e_shop.Alert.AlertController;
    exports com.example.e_shop.Animation;
    exports com.example.e_shop.UserReview;
    exports com.example.e_shop.ShowAdd;
}