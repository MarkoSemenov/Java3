import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button enter;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button sendButton;
    @FXML
    private ListView<String> nickNames;
    @FXML
    private Text userTextField;
    @FXML
    private Button authButton;
    private static Client client;
    private String nickFromListView;


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
                    if (!nickNames.getSelectionModel().getSelectedItems().isEmpty()) {
                        client.sendCommand(Command.privateMessageCommand(nickFromListView, sendTextField.getText()));
                        if (!client.getNick().equals(nickFromListView)) {
                            appendMessage();
                        }
                    } else {
                        client.sendCommand(Command.publicMessageCommand(client.getNick(), sendTextField.getText()));
                        appendMessage();
                    }
                    sendTextField.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Snd Msg");
        t.start();
    }

    private void appendMessage() throws IOException {
        chatTextArea.appendText("Я: " + sendTextField.getText() + System.lineSeparator());
        client.saveHistory(sendTextField.getText());
    }

    public void choiceUserToSendPersonalMsg(MouseEvent mouseEvent) {
        nickFromListView = nickNames.getSelectionModel().getSelectedItem();
        int i = nickNames.getSelectionModel().getSelectedIndex();
        if (mouseEvent.getClickCount() == 2) {
            nickNames.getSelectionModel().clearSelection(i);
        }
    }

    public void changeNick(ActionEvent event) {
        NetChat.changeNickWindow();
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        client.closeConnection();
    }

    public static Client getClient() {
        return client;
    }

    public ListView<String> getNickNames() {
        return nickNames;
    }

    public TextArea getChatTextArea() {
        return chatTextArea;
    }

    public TextField getSendTextField() {
        return sendTextField;
    }

    public Text getUserTextField() {
        return userTextField;
    }
}



