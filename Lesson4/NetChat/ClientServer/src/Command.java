import java.io.Serializable;
import java.util.LinkedList;

public class Command implements Serializable {

    private final CommandType type;
    private final Object data;
//    private final T type;
//    private final V data;

    public Command(CommandType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command authCommand(String login, String password) {
        return new Command(CommandType.AUTH, new AuthCommandData(login, password));
    }

    public static Command authOkCommand(String nickname) {
        return new Command(CommandType.AUTH_OK, new AuthOkCommandData(nickname));

    }

    public static Command privateMessageCommand(String receiver, String message) {
        return new Command(CommandType.PRIVATE_MESSAGE, new PrivateMessageCommandDate(receiver, message));
    }

    public static Command publicMessageCommand(String nickname, String message) {
        return new Command(CommandType.PUBLIC_MESSAGE, new PublicMessageCommandDate(nickname, message));
    }

    public static Command errorMessageCommand(String message) {
        return new Command(CommandType.ERROR, new ErrorCommandDate(message));
    }


    public static Command sendInfoAboutUsers(LinkedList<String> nicknames) {
        return new Command(CommandType.INFO_ABOUT_USERS, new SendInfoAboutUsers(nicknames));
    }

    public static Command signUpNewUser(String nickname, String login, String password) {
        return new Command(CommandType.REGISTRATION, new RegistrationNewUserData(nickname, login, password));
    }

    public static Command changeNickname(String nickname) {
        return new Command(CommandType.CHANGE_NICKNAME, new ChangeNicknameData(nickname));
    }

}
