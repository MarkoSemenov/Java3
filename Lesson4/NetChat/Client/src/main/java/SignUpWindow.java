import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SignUpWindow {

    @FXML
    private TextField nickname;
    @FXML
    private TextField login;
    @FXML
    private TextField password;

    public void registration(ActionEvent event) {

        Controller.getClient().sendCommand(Command.signUpNewUser(nickname.getText(), login.getText(), password.getText()));
        NetChat.getSignUpWindow().closeChatStage();

    }
}
