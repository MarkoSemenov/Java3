import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ChatStage extends Stage {

    private Stage stage;

    public ChatStage(String title, String pathFXML, Stage stageOwner) {
        try {
            initStage(title, pathFXML);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(stageOwner);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatStage(String title, String pathFXML) {
        try {
            initStage(title, pathFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initStage(String title, String pathFXML) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(pathFXML));
        AnchorPane anchorPane = fxmlLoader.load();
        stage = new Stage();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
    }

    public Stage getStage() {
        return stage;
    }

    public void showChatStage() {
        stage.show();
    }

    public void closeChatStage() {
        stage.close();
    }

}
