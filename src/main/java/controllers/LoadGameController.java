package controllers;

import app.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class LoadGameController implements EventHandler {
    private Stage stage;
    private final FileManager fileManager = new FileManagerImpl();

    @FXML
    private VBox vBox;

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) vBox.getScene().getWindow();
        }
        return stage;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void initialize() {
        Collection<Button> loadGameButtons = new ArrayList<>();
        Collection<String> saveTitles = getFileManager().getSaveTitles();
        for (String saveTitle : saveTitles) {
            Button button = new Button(saveTitle);
            button.setOnAction(this);
            button.setStyle("-fx-font-size:25");
            button.setMinWidth(300);
            button.setMinHeight(50);
            loadGameButtons.add(button);
        }
        vBox.getChildren().addAll(loadGameButtons);
    }

    @Override
    public void handle(Event event) {
        Button pressedButton = (Button) event.getSource();
        Game game = new Game(pressedButton.getText(), getFileManager().loadGame(pressedButton.getText()));
        try {
            loadStakePage(game, getStage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WelcomePage.fxml"));
        loader.load();

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
    }

    public static void loadStakePage(Game game, Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(LoadGameController.class.getResource("/view/StakePage.fxml"));

        loader.load();
        StakePageController stakePageController = loader.getController();
        stakePageController.setGame(game);
        stakePageController.refresh();  // må refreshe manuelt fordi stakePageController ikke har tilgang til game når initialize() calles der

        Scene scene = new Scene(loader.getRoot(), stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);

    }

}
