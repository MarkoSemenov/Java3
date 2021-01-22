import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class NetChat extends Application {

    private static Stage primaryStage;
    private static final ChatStage authWindow = new ChatStage("Authorization", "/authentication.fxml");
    private static ChatStage signUpWindow;
    private static ChatStage changeNickWindow;

    @Override
    public void start(Stage primaryStage) throws Exception {

        NetChat.primaryStage = primaryStage;

        authentication();
        initChat();
    }

    public static void showChat() {
        primaryStage.show();
    }

    public void initChat() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Simple chat");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 500));
    }

    public static void authentication(){

        authWindow.showChatStage();
    }

    public static void signUpWindow() {
        signUpWindow = new ChatStage("Registration", "/signUpWindow.fxml", authWindow.getStage());
        signUpWindow.showChatStage();
    }

    public static void changeNickWindow() {
        changeNickWindow = new ChatStage("Change your nickname", "/changeNickWindow.fxml", primaryStage);
        changeNickWindow.showChatStage();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Controller.getClient().getSocket().close();
    }

    public static ChatStage getAuthWindow() {
        return authWindow;
    }

    public static ChatStage getSignUpWindow() {
        return signUpWindow;
    }

    public static ChatStage getChangeNickWindow() {
        return changeNickWindow;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {

        launch(args);

    }
}