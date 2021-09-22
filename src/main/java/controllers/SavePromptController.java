package controllers;

import app.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class SavePromptController {
    private Game game;
    private Stage mainStage, ownStage;

    @FXML
    private Button saveButton;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Stage getOwnStage() {
        if (this.ownStage == null) {
            this.ownStage = (Stage) saveButton.getScene().getWindow();
        }
        return ownStage;
    }

    public void saveButtonAction() {
        getGame().saveGame();
        getOwnStage().close();
        getMainStage().close();
    }

    public void dontSaveButtonAction() {
        getOwnStage().close();
        getMainStage().close();
    }

    public void saveQuitMainMenu() throws IOException {
        getGame().saveGame();
        getMainStage().setOnCloseRequest(e -> {// gjør så man kan krysse ut programmet igjen uten å få spørsmål om man vil lagre
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WelcomePage.fxml"));
        loader.load();

        Scene scene = new Scene(loader.getRoot(), getMainStage().getScene().getWidth(), getMainStage().getScene().getHeight());
        getMainStage().setScene(scene);
        getOwnStage().close();
    }
}
