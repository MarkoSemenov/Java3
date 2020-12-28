import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client {

    private DataInputStream inputMessage;
    private DataOutputStream outputMessage;
    private Socket socket;
    private final Controller controller;
    private volatile boolean isConnect = true;
    public volatile static boolean authorization = false;
    private String[] getNicksFromServer;
    private String nick;


    public Client(Controller controller) throws IOException {

        this.controller = controller;

        try {
            socket = new Socket("localhost", 2021);
            System.out.println("Соединение установлено: " + socket.isConnected());
            outputMessage = new DataOutputStream(socket.getOutputStream());
            inputMessage = new DataInputStream(socket.getInputStream());
            connectServer();

        } catch (IOException e) {
            System.out.println("Сервер не доступен");
            e.printStackTrace();
        }

    }


    public void connectServer() throws IOException {
        Thread readThread = new Thread(() -> {
            while (true) {
                try {
                    if (!authorization) {
                        authentication();
                    }
                    getMessage();

                } catch (IOException e) {
                    System.out.println("Соединение разорвано");
                    isConnect = false;
                    authorization = false;
                    Platform.exit();
                    return;
                }
            }
        });
        readThread.start();

    }

    public void getMessage() throws IOException {
        String getMsg = inputMessage.readUTF();
        if (getMsg.startsWith("/clients ")) {
            getNicksFromServer = getMsg.split(" ");
            Platform.runLater(new Thread(() -> controller.nickNames.setItems(FXCollections.observableArrayList
                    (Arrays.copyOfRange(getNicksFromServer, 1, getNicksFromServer.length)))));

        } else if (getMsg.startsWith("/client ")) {
            String[] msg = getMsg.split(" ", 2);
            nick = msg[1].trim();
            Platform.runLater(new Thread(() -> NetChat.primaryStage.setTitle(nick)));
        } else {
            controller.chatTextArea.appendText(getMsg);
        }
    }


    public void closeConnection() throws IOException {
        if (socket.isConnected()) {
            outputMessage.close();
            inputMessage.close();
            socket.close();
        }
    }

    public void sendMessage() throws IOException {
        if (isConnect) {
            String msgToServer = controller.sendTextField.getText();
            outputMessage.writeUTF(msgToServer);
            controller.sendTextField.clear();
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = inputMessage.readUTF();
            if (str.startsWith("Success")) {
                setAuthorization(true);
                Platform.runLater(new Thread(() -> NetChat.authStage.close()));
                Platform.runLater(new Thread(NetChat::showChat));
                return;
            }
            if (str.startsWith("forcedClose")) {
                authorization = false;
                closeConnection();
                Platform.exit();
            }
        }
    }


    public Client getClient() {
        return this;
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public DataInputStream getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(DataInputStream inputMessage) {
        this.inputMessage = inputMessage;
    }

    public DataOutputStream getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(DataOutputStream outputMessage) {
        this.outputMessage = outputMessage;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }


    public boolean isAuthorization() {
        return authorization;
    }

    public void setAuthorization(boolean authorization) {
        Client.authorization = authorization;
    }


}