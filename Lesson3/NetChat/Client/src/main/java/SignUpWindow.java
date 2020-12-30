import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignUpWindow {

    @FXML
    public TextField nickname;
    @FXML
    public TextField login;
    @FXML
    public TextField password;

    public void registration(ActionEvent event) {

        try {
            Controller.client.getOutputMessage().writeUTF("/reg" + " " + nickname.getText() + " " + login.getText() + " " + password.getText());
            NetChat.stageSignUp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
