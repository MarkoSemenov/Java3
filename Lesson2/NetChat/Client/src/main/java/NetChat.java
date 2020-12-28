import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public class NetChat extends Application {

    public static Stage primaryStage;
    public static Stage authStage;
    public static Stage stageChangeNick;
    public static Stage stageSignUp;


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
        primaryStage.resizableProperty().setValue(false);
        primaryStage.setScene(new Scene(root, 600, 500));
    }

    public static void authentication() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NetChat.class.getResource("/authentication.fxml"));
        AnchorPane page = loader.load();
        authStage = new Stage();
        authStage.setTitle("Authorization");
        Scene scene = new Scene(page);
        authStage.setScene(scene);
        authStage.resizableProperty().setValue(false);
        authStage.show();

    }

    public static void signUpWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(NetChat.class.getResource("/signUpWindow.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        stageSignUp = new Stage();
        stageSignUp.setTitle("Registration");
        Scene scene = new Scene(anchorPane);
        stageSignUp.setScene(scene);
        stageSignUp.initModality(Modality.WINDOW_MODAL);
        stageSignUp.initOwner(authStage);
        stageSignUp.show();
    }

    public static void changeNickWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(NetChat.class.getResource("/changeNickWindow.fxml"));
        AnchorPane anchorPane = fxmlLoader.load();
        stageChangeNick = new Stage();
        stageChangeNick.setTitle("Change Nickname");
        Scene scene = new Scene(anchorPane);
        stageChangeNick.setScene(scene);
        stageChangeNick.initModality(Modality.WINDOW_MODAL);
        stageChangeNick.initOwner(primaryStage);
        stageChangeNick.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Controller.client.getSocket().close();
    }


    public static void main(String[] args) throws IOException {

        launch(args);

    }
}