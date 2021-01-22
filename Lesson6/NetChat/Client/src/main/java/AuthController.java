import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class AuthController {
    Alert alert;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;


    public boolean isInputCorrect() {
        return ((login.getText().length() > 0) || (password.getText().length() > 0));
    }

    public void entrance(ActionEvent event) {
        alert = new Alert(Alert.AlertType.WARNING);
        if (isInputCorrect()) {
            Controller.getClient().sendCommand(Command.authCommand(login.getText(), password.getText()));
            login.clear();
            password.clear();
        } else {
            alert.initOwner(NetChat.getAuthWindow());
            alert.setHeaderText("Поля не заполнены");
            alert.showAndWait();
        }
    }


    public void openRegWindow(ActionEvent event) {
        NetChat.signUpWindow();
    }

    public void closeWindow(ActionEvent event) {
        NetChat.getAuthWindow().closeChatStage();
    }
}
