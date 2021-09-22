package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { // Kjør GUIStarter dersom koden ikke kompilerer

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WelcomePage.fxml"));
        loader.load();
        primaryStage.setTitle("BlackJack");
//        Scene scene = new Scene(root, 1100, 825);
        Scene scene = new Scene(loader.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





