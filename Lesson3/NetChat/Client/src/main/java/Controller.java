import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button enter;
    @FXML
    public TextArea chatTextArea;
    @FXML
    public TextField sendTextField;
    @FXML
    public Button sendButton;
    @FXML
    public ListView<String> nickNames;
    @FXML
    private Text userTextField;
    @FXML
    private Button authButton;

    public static Client client;


    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {

        try {
            client = new Client(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickOnSendButton(ActionEvent actionEvent) {
        Thread t = new Thread(() -> {
            try {
                if (!sendTextField.getText().equals("")) {
                    client.sendMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Snd Msg");
        t.start();
    }

    public void choiceUserToSendPersonalMsg(MouseEvent mouseEvent) {
        ObservableList<String> s = nickNames.getSelectionModel().getSelectedItems();
        sendTextField.clear();
        String str = "/w" + " " + s + " ";
        sendTextField.appendText(str);
            if (sendTextField.getText().startsWith("/w")) {
                sendTextField.clear();
                sendTextField.appendText(str);
            }
    }

    public void changeNick(ActionEvent event) {
        try {
            NetChat.changeNickWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) throws IOException {
        Platform.exit();
        client.closeConnection();
    }

    public TextArea getChatTextArea() {
        return chatTextArea;
    }

    public void setChatTextArea(TextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }


    public TextField getSendTextField() {
        return sendTextField;
    }

    public void setSendTextField(TextField sendTextField) {
        this.sendTextField = sendTextField;
    }

    public Text getUserTextField() {
        return userTextField;
    }

    public void setUserTextField(Text userTextField) {
        this.userTextField = userTextField;
    }


    public Button getAuthButton() {
        return authButton;
    }

    public void setAuthButton(Button authButton) {
        this.authButton = authButton;
    }

}



