import java.io.Serializable;

public class PublicMessageCommandDate implements Serializable {
    private final String sender;
    private final String message;

    public PublicMessageCommandDate(String sender, String message) {
        this.sender = sender;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }


//    @Override
//    public String toString() {
//        return "PublicMessageCommandDate{" +
//                "nickname='" + nickname + '\'' +
//                ", message='" + message + '\'' +
//                '}';
//    }
}
