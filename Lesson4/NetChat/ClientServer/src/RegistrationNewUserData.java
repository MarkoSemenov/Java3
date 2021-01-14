import java.io.Serializable;

public class RegistrationNewUserData implements Serializable {

    private final String nickname;
    private final String login;
    private final String password;

    public RegistrationNewUserData(String nickname, String login, String password) {
        this.nickname = nickname;
        this.login = login;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
