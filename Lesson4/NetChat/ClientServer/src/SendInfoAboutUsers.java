import java.io.Serializable;
import java.util.LinkedList;

public class SendInfoAboutUsers implements Serializable {
    LinkedList<String> clientsNicknames;


    public SendInfoAboutUsers(LinkedList<String> clientsNicknames) {
        this.clientsNicknames = clientsNicknames;
    }

    public LinkedList<String> getClientsNicknames() {
        return clientsNicknames;
    }
}
