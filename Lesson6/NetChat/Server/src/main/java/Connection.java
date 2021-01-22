import lombok.Data;
import lombok.extern.log4j.Log4j2;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@Log4j2
@Data
class Connection implements Callable {

    private final Server server;
    private final Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile boolean isConnectSocket = true;
    private volatile boolean authorization = false;
    private String nickname;

    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            if (!authorization) {
                authentication();
            }
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (isConnectSocket) {
            try {
                getMessage();
            } catch (IOException e) {
                log.warn(nickname + " разорвал соединение");
                try {
                    if (nickname != null) {
                        server.broadcastMsg(nickname, "left chat");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                server.removeUsers(this);
                isConnectSocket = false;
                return;
            }
        }
    }

    public void authentication() {
        SQLiteBase.connect();
        while (!authorization) {
            try {
                Command command = readCommand();
                switch (command.getType()) {
                    case AUTH: {
                        if (authProcess(command)) return;
                        break;
                    }
                    case REGISTRATION:
                        RegistrationNewUserData data = (RegistrationNewUserData) command.getData();
                        SQLiteBase.addUser(data.getLogin(), data.getPassword(), data.getNickname());
                        nickname = data.getNickname();
                        successAuth();
                        break;
                }
            } catch (IOException e) {
                log.error("Соединение прервано");
                closeConnection();
                e.printStackTrace();
                return;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private boolean authProcess(Command command) throws SQLException, IOException {
        AuthCommandData data = (AuthCommandData) command.getData();
        String login = data.getLogin();
        String password = data.getPassword();
        if (SQLiteBase.checkLoginPassword(login, password).equals("Success")) {
            nickname = SQLiteBase.nickname;
            if (!server.isNickBusy(nickname)) {
                successAuth();
                return true;
            } else {
                sendCommand(Command.errorMessageCommand("Ошибка авторизации"));
                log.warn("Ошибка авторизации");
            }
        }
        return false;
    }

    private void successAuth() throws IOException {
        sendCommand(Command.authOkCommand(nickname));
        server.broadcastMsg(nickname, " зашел в чат...");
        server.addUsers(this);
        authorization = true;
        log.info(nickname + " авторизован");
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            log.catching(new EOFException("Соединение прервано"));
            e.printStackTrace();
        }
        return command;
    }


    public void getMessage() throws IOException {

        Command command = readCommand();
        if (command != null) {
            switch (command.getType()) {
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandDate data = (PrivateMessageCommandDate) command.getData();
                    server.sndPersonalMsg(this, data.getReceiver(), data.getMessage());
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandDate data = (PublicMessageCommandDate) command.getData();
                    server.broadcastMsg(nickname, data.getMessage());
                    System.out.println(nickname + ": " + data.getMessage());
                    log.info(getNickname() + " прислал сообщение");
                    break;
                }
                case CHANGE_NICKNAME:
                    ChangeNicknameData data = (ChangeNicknameData) command.getData();
                    SQLiteBase.changeNick(data.getNickname(), nickname);
                    nickname = data.getNickname();
                    server.sendInfoAboutClients();
                    log.info(getNickname() + " изменил никнейм");
                    break;
                default:
                    log.catching(new IllegalArgumentException("Unknown command type: " + command.getType()));
                    throw new IllegalArgumentException("Unknown command type: " + command.getType());
            }
        }

    }

    public void sendCommand(Command command) {
        if (isConnectSocket) {
            try {
                outputStream.writeObject(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        if (socket.isConnected()) {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
                log.info("Соединение закрыто");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public Object call() {
        return this;
    }
}