package com.example.e_shop.MessagesHandler.server;

import com.example.e_shop.DataBaseConnection.DataBase;
import com.example.e_shop.MessagesHandler.client.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private static Server server;

    private List<ClientHandler> clients = new ArrayList<>();


    private Server() throws IOException {
        serverSocket = new ServerSocket(3001);
    }

    public static Server getInstance() throws IOException {
        return server != null ? server : (server = new Server());
    }

    public void makeSocket() {
        try {

            while (!serverSocket.isClosed()) {
                // try{
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, clients);
                clients.add(clientHandler);
                System.out.println("client socket accepted " + socket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static void storeStringToFile(String message) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("server_store.txt", true))) {
//            writer.write(message);
//            writer.newLine(); // Add a new line after each string
//            System.out.println("Message Stored successfully.");
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//    }

    public static void StoreMessageInDataBase(String message) {
        String[] parts = message.split("&&");
        if (parts.length >= 3) {
            String username = parts[0];
            String mssg = parts[1];
            String mTime = parts[2];
            try (Connection connection = DataBase.connectDB();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO message_history (username, messages) VALUES (?,?)")) {
                statement.setString(1, username);
                statement.setString(2, mssg + "&&" + mTime);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Message store  into database successfully.");
                } else {
                    System.out.println("Failed to insert message into database.");
                }
            } catch (SQLException e) {
                System.err.println("Error inserting string into database: " + e.getMessage());
            }
        } else {
            System.out.println("...'");
        }
    }


}
