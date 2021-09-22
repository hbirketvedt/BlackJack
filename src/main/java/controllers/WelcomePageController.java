package controllers;


import app.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomePageController {
    private Stage stage;

    @FXML
    private Button newGameButton, loadGameButton;
    @FXML
    private Label welcomeLabel;

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) newGameButton.getScene().getWindow();
        }
        return stage;
    }



    public void initialize() {
        welcomeLabel.requestFocus();
    } // gjør så ingen av knappene kommer i fokus

    public void newGameButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewGame.fxml"));
        loader.load();

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
    }

    public void loadGameButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoadGame.fxml"));
        loader.load();

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
    }

}
