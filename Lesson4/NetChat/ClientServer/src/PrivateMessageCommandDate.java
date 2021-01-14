import java.io.Serializable;

public class PrivateMessageCommandDate implements Serializable {
    private final String receiver;
    private final String message;

    public PrivateMessageCommandDate(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;

    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PrivateMessageCommandDate{" +
                "receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
