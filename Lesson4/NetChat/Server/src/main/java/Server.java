import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int SERVER_PORT;
    private volatile boolean isConnect = true;
    private final Scanner scanner = new Scanner(System.in);
    private final LinkedList<Connection> allConnections = new LinkedList<>();
    private final int POOL_COUNT = 3;
    private ExecutorService service = Executors.newFixedThreadPool(POOL_COUNT);

    /**
     * Как правило Сервер всегда работает и принимает различные запросы от клиентов, исходя из этой логики,
     * было бы удобно иметь постоянно запущенные потоки для быстрого решения задач
     * + закинуть задачу в коллекцию в очередь быстрее чем каждый раз создавать новый поток (экономия времени и ресурсов);
     * <p>
     * Т.к. наш чат простой и параллельных задач мало, наверное, можно обойтись и обычными потоками,
     * но для многозадачности, ExecutorService мне кажется очень эффективным решением;
     * <p>
     * Применил newFixedThreadPool в двух методах startServer(), sendMsgFromServerToUsers().
     */


    public Server(int SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;

    }

    public void startServer() {
        int clientCount = 1;

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            sendMsgFromServerToUsers();

            while (isConnect) {
                try {
                    Socket socket = serverSocket.accept();
                    service.execute(() -> new Connection(socket, this));
                    ++clientCount;

                    if (clientCount >= POOL_COUNT) {
                        service.shutdown();
                        service = Executors.newFixedThreadPool(POOL_COUNT + clientCount);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    service.shutdownNow();
                    isConnect = false;
                    return;
                }
            }

        } catch (IOException e) {
            System.out.println("Соединение разорвано");
        }

    }


    public synchronized void addUsers(Connection connection) throws IOException {
        allConnections.add(connection);
        sendInfoAboutClients();
    }

    public synchronized void removeUsers(Connection connection) {
        try {
            allConnections.remove(connection);
            sendInfoAboutClients();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void broadcastMsg(String sender, String msg) throws IOException {

        allConnections.stream()
                .filter(c -> !c.getNickname().equals(sender))
                .forEach(c -> c.sendCommand(Command.publicMessageCommand(sender, msg)));
    }

    public synchronized void sndPersonalMsg(Connection sender, String receiver, String msg) {

        allConnections.stream()
                .filter(c -> c.getNickname().equals(receiver))
                .forEach(c -> c.sendCommand(Command.publicMessageCommand(sender.getNickname(), msg)));

        sender.sendCommand(Command.errorMessageCommand("Участника " + receiver + " нет в чате"));
    }

    public void sendInfoAboutClients() throws IOException {
        LinkedList<String> list = new LinkedList<>();

        for (Connection connection : allConnections) {
            list.add(connection.getNickname());
        }

        for (Connection connection : allConnections) {
            connection.sendCommand(Command.sendInfoAboutUsers(list));
        }
    }

    public synchronized boolean isNickBusy(String nick) {

        for (Connection connection : allConnections) {
            if (connection.getNickname().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    private void sendMsgFromServerToUsers() {
        service.execute(() -> {
            while (isConnect) {
                String msgToUsers = scanner.nextLine();
                for (Connection c : allConnections) {
                    c.sendCommand(Command.publicMessageCommand("Server", msgToUsers));
                }
            }
        });
    }


    public LinkedList<Connection> getAllConnections() {
        return allConnections;
    }


    public static void main(String[] args) {
        new Server(2022).startServer();
    }

}

