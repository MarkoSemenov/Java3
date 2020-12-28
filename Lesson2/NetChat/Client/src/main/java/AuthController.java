import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthController {
    Alert alert;
    @FXML
    public TextField login;
    @FXML
    public PasswordField password;


    public boolean isInputCorrect() {
        return ((login.getText().length() > 0) || (password.getText().length() > 0));
    }

    public void entrance(ActionEvent event) {
        alert = new Alert(Alert.AlertType.WARNING);

        try {
            if (isInputCorrect()) {
                Controller.client.getOutputMessage().writeUTF("/auth," + this.login.getText() + "," + this.password.getText());
                login.clear();
                password.clear();
            } else {
                alert.initOwner(NetChat.authStage);
                alert.setHeaderText("Поля не заполнены");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void openRegWindow(ActionEvent event) throws IOException {
            NetChat.signUpWindow();
    }

    public void closeWindow(ActionEvent event) {
        NetChat.authStage.close();
    }
}
