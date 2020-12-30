import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChangeNickController {
    @FXML
    public TextField nickname;

    public void changeNickname(ActionEvent event) {

        try {
            Controller.client.getOutputMessage().writeUTF("/nick" + " " + nickname.getText());
            NetChat.stageChangeNick.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
