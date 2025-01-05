package com.example.e_shop.MessagesHandler.client;

import com.example.e_shop.Alert.ErrorAlert;
import com.example.e_shop.MessagesHandler.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ClientHandler {
    private Socket socket;
    private List<ClientHandler> clients;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String msg = "";

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            ErrorAlert.displayCustomAlert("ERROR","ERROR in client handle..");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        msg = dataInputStream.readUTF();
                       // String stringTimes = LocalTime.now(ZoneId.of("Asia/Dhaka")).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
                        for (ClientHandler clientHandler : clients) {
                            if (clientHandler.socket.getPort() != socket.getPort()) {
                                clientHandler.dataOutputStream.writeUTF(msg);
                                clientHandler.dataOutputStream.flush();
                                //Server.storeStringToFile(msg,stringTimes);
                            }
                        }
                      //  System.out.println(msg);
                       // Server.storeStringToFile(msg);
                        Server.StoreMessageInDataBase(msg);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
