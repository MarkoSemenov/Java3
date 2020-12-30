import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client {

    private DataInputStream inputMessage;
    private DataOutputStream outputMessage;
    private Socket socket;
    private final Controller controller;
    private volatile boolean isConnect = true;
    public volatile static boolean authorization = false;
    private String[] getNicksFromServer;
    private String nick;
    BufferedWriter fileWriter = null;
    BufferedReader fileReader;
    private String historyPath;


    public Client(Controller controller) throws IOException {

        this.controller = controller;

        try {
            socket = new Socket("localhost", 2022);
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
                    closeConnection();
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
            historyPath = "history_" + nick + ".txt";
            fileWriter = new BufferedWriter(new FileWriter(historyPath, true));
            fileReader = new BufferedReader(new FileReader(historyPath));
            Platform.runLater(new Thread(() -> NetChat.primaryStage.setTitle(nick)));
        } else if (getMsg.startsWith("/history")) {
            Platform.runLater(new Thread(() -> {
                controller.chatTextArea.appendText(getMsg);
            }));
        } else {
            controller.chatTextArea.appendText(getMsg);
            saveHistory(getMsg);
        }
    }


    public void closeConnection() {
        if (socket.isConnected()) {
            try {
                outputMessage.close();
                inputMessage.close();
                fileWriter.close();
                fileReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                Platform.runLater(new Thread(() -> {
                    try {
                        loadHistory();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                return;
            }
            if (str.startsWith("forcedClose")) {
                authorization = false;
                closeConnection();
                Platform.exit();
            }
        }
    }

    private void loadHistory() throws IOException {
        List<String> list = new ArrayList<>();
        String line;
        while ((line = fileReader.readLine()) != null) {
            list.add(line);
        }
        if (list.size() < 100) {
            for (String s : list) {
                controller.chatTextArea.appendText(s + "\n");
            }
        } else {
            for (int i = list.size() - 100; i < list.size(); i++) {
                controller.chatTextArea.appendText(list.get(i) + "\n");
                controller.chatTextArea.forward();
            }
        }
    }

    public void saveHistory(String str) throws IOException {
        fileWriter.write(str);
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