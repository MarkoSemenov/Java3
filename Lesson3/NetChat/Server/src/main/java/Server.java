import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Server {

    private final int SERVER_PORT;
    private volatile boolean isConnect = true;
    private final Scanner scanner = new Scanner(System.in);
    public static LinkedList<Connection> allConnections = new LinkedList<>();

    public Server(int SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;

    }

    public void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            Thread sendMsgTo = new Thread(this::sendMsgToUsers);
            sendMsgTo.start();

            while (isConnect) {
                try {
                    Socket socket = serverSocket.accept();
                    new Connection(socket, this);
                } catch (IOException e) {
                    e.printStackTrace();
                    isConnect = false;
                    return;
                }
            }

        } catch (IOException e) {
            System.out.println("Соединение разорвано");
        }

    }


    public synchronized void addUsers(Connection o) throws IOException {
        allConnections.add(o);
        sendInfoAboutClients();
    }

    public synchronized void removeUsers(Connection o) {
        allConnections.remove(o);
        try {
            sendInfoAboutClients();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void broadcastMsg(String msg) throws IOException {
        for (Connection c : allConnections) {
            c.sendMsg(msg);
        }
    }

    public synchronized void sendInfoAboutClients() throws IOException {
        StringBuilder sb = new StringBuilder("/clients ");
        for (Connection c : allConnections) {
            if (!allConnections.isEmpty()) {
                sb.append(c.gtName() + " ");
            }
        }
        broadcastMsg(sb.toString());
    }

    public synchronized boolean isNickBusy(String nick) {

        for (Connection o : allConnections) {
            if (o.gtName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void sndPersonalMsg(Connection from, String nickTo, String msg) throws IOException {
        for (Connection o : allConnections) {
            if (o.gtName().equals(nickTo)) {
                o.sendMsg(from.gtName() + ": " + msg);
                from.sendMsg(from.gtName() + ": " + msg);
                return;
            }
        }
        from.sendMsg("Участника " + nickTo + " нет в чате");
    }

    private void sendMsgToUsers() {
        while (isConnect) {
            String msgToUsers = scanner.nextLine();
            for (Connection connection : allConnections) {
                try {
                    connection.getOutputStream().writeUTF("Сервер:" + msgToUsers + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }


    public LinkedList<Connection> getAllConnections() {
        return allConnections;
    }

    public void setAllConnections(LinkedList<Connection> allConnections) {
        Server.allConnections = allConnections;
    }

    public static void main(String[] args) {
        new Server(2022).startServer();
    }

}

