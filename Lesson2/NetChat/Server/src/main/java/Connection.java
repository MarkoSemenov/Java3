import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


class Connection extends Thread {

    private final Server server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
//    User user = new User("Mark", "log1", "pas1");
//    User user1 = new User("Bill", "log2", "pas2");
//    User user2 = new User("Ann", "log3", "pas3");
//    private final List<User> base = List.of(user, user1, user2);
    private volatile boolean isConnectSocket = true;
    private volatile boolean authorization = false;
    private String name;
    Timer timer = new Timer();
    TimerTask timerTask;

    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            if (!authorization) {
                authentication();
                timer.cancel();
            }
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (isConnectSocket) {
            try {
                getMessage();
            } catch (IOException e) {
                System.out.println(name + " разорвал соединение");
                try {
                    if (name != null) {
                        server.broadcastMsg(name + " left chat");
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

    public void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (socket.isConnected()) {
                        outputStream.writeUTF("forcedClose");
                        outputStream.close();
                        inputStream.close();
                        socket.close();
                        Thread.currentThread().interrupt();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 12000);
    }

    public void authentication() {
        SQLiteBase.connect();
        while (true) {
            try {
                String auth = inputStream.readUTF();
                startTimer();
                if (auth.startsWith("/auth")) {
                    String[] s = auth.split(",");
                    System.out.println(Arrays.toString(s));
                    try {
                        if (SQLiteBase.checkLoginPassword(s[1], s[2]).equals("Success")) {
                            name = SQLiteBase.nickname;
                            if (!server.isNickBusy(name)) {
                                insideAuth();
                                return;
                            } else {
                                sendMsg("denied");
                            }
                        } else outputStream.writeUTF("denied");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else if (auth.startsWith("/reg")){
                    String[] str = auth.split(" ", 4);
                    String nick = str[1];
                    String login = str[2];
                    String password = str[3];
                    SQLiteBase.addUser(login, password, nick);
                    name = nick;
                    insideAuth();
                    return;
                }
            } catch (IOException e) {
                System.out.println("Соединение прервано");
                e.printStackTrace();
                return;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void insideAuth() throws IOException {
        sendMsg("Success");
        sendMsg("/client " + gtName());
        server.broadcastMsg(name + " зашел в чат...");
        server.addUsers(this);
        authorization = true;
        System.out.println(name + " авторизован");
    }


    public void getMessage() throws IOException {
        String messageFromUser = inputStream.readUTF();
        if (messageFromUser.startsWith("/w")) {
            String[] str = messageFromUser.split(" ", 3);
            String nick = str[1].substring(1, (str[1].length() - 1));
            String msg = str[2];
            server.sndPersonalMsg(this, nick, msg);
        } else if (messageFromUser.startsWith("/nick")) {
            String[] str = messageFromUser.split(" ", 2);
            SQLiteBase.changeNick(str[1], name);
            name = str[1];
            sendMsg("/client " + gtName());
            server.sendInfoAboutClients();
        } else {
            System.out.println(name + ": " + messageFromUser);
            server.broadcastMsg(name + ": " + messageFromUser);
        }
    }

    public void sendMsg(String msgToUsers) throws IOException {
        if (isConnectSocket) {
            outputStream.writeUTF(msgToUsers + "\n");
        }
    }


    public void closeSocket() {
        if (socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String gtName() {
        return name;
    }

    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public boolean isConnectSocket() {
        return isConnectSocket;
    }

    public void setConnectSocket(boolean connectSocket) {
        isConnectSocket = connectSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }


}