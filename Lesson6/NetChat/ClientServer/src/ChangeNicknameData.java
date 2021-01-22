import java.io.Serializable;

public class ChangeNicknameData implements Serializable {

    private final String nickname;

    public ChangeNicknameData(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
