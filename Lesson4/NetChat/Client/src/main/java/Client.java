import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Client {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private final Controller controller;
    private volatile boolean isConnect = true;
    private volatile static boolean authorization = false;
    private boolean isHistoryWrite = true;
    private String nick;
    private BufferedWriter fileWriter;
    private BufferedReader fileReader;


    public Client(Controller controller) throws IOException {

        this.controller = controller;

        try {
            socket = new Socket("localhost", 2022);
            System.out.println("Соединение установлено: " + socket.isConnected());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
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
                    waitCommand();
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

    private void waitCommand() throws IOException {

        if (!authorization) {
            authentication();
        } else {
            getMessage();
        }

    }

    public void getMessage() throws IOException {
        Command command = readCommand();
        switch (command.getType()) {
            case INFO_ABOUT_USERS:
                SendInfoAboutUsers info = (SendInfoAboutUsers) command.getData();
                loadNicksToListView(info.clientsNicknames);
                break;
            case PRIVATE_MESSAGE: {
                PrivateMessageCommandDate data = (PrivateMessageCommandDate) command.getData();
                appendLineIntoTextArea(data.getReceiver(), data.getMessage());
                saveHistory(data.getReceiver(), data.getMessage());
                break;
            }
            case PUBLIC_MESSAGE:
                PublicMessageCommandDate data = (PublicMessageCommandDate) command.getData();
                appendLineIntoTextArea(data.getSender(), data.getMessage());
                saveHistory(data.getSender(), data.getMessage());
                break;
        }

    }

    private void appendLineIntoTextArea(String nickname, String message) {
        Platform.runLater(() -> controller.getChatTextArea().appendText(nickname + ": " + message + System.lineSeparator()));
    }

    public void loadNicksToListView(List<String> info) {
        Platform.runLater(() -> controller.getNickNames().setItems(FXCollections.observableList(info)));
    }

    public void closeConnection() {

        if (socket.isConnected()) {
            try {
                if (!isHistoryWrite) {
                    fileReader.close();
                    fileWriter.close();
                }
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(Command command) {
        try {
            outputStream.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authentication() throws IOException {

        while (!authorization) {
            Command command = readCommand();
            switch (command.getType()) {
                case AUTH_OK: {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    nick = data.getNickname();
                    Platform.runLater(() -> NetChat.getAuthWindow().closeChatStage());
                    Platform.runLater((NetChat::showChat));
                    if (isHistoryWrite) {
                        String historyPath = "history_" + nick + ".txt";
                        fileWriter = new BufferedWriter(new FileWriter(historyPath, true));
                        fileReader = new BufferedReader(new FileReader(historyPath));
                        isHistoryWrite = false;
                    }
                    Platform.runLater(() -> NetChat.getPrimaryStage().setTitle(nick));
                    Platform.runLater(this::loadHistory);
                    authorization = true;
                    break;
                }
                case ERROR:
                    ErrorCommandDate data = (ErrorCommandDate) command.getData();
                    System.out.println(data.getErrorMsg());
                    authorization = false;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + command.getType());
            }

        }
    }

    private void loadHistory() {
        String line;
        try {
            while (((line = fileReader.readLine()) != null)) {
                controller.getChatTextArea().appendText(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return command;
    }

    public void saveHistory(String message) throws IOException {
        fileWriter.write("Я: " + message + System.lineSeparator());
    }

    public void saveHistory(String username, String message) throws IOException {
        fileWriter.write(username + ": " + message + System.lineSeparator());
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


    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
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

    public String getNick() {
        return nick;
    }
}