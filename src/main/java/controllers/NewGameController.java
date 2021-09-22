package controllers;

import app.FileManagerImpl;
import app.Game;
import app.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class NewGameController {
    private Stage stage;
    private final FileManager fileManager = new FileManagerImpl();


    @FXML
    private TextField nameInput;
    @FXML
    private Button nameInputButton;
    @FXML
    private Label newGameLabel;

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) nameInput.getScene().getWindow();
        }
        return stage;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void nameInputButtonAction() throws IOException {
        String name = nameInput.getText();
        try{
            getFileManager().newSaveGame(name); // feiler her dersom navnet ikke er gyldig
        } catch (IllegalArgumentException e) {
            nameInput.clear();
            nameInput.getStyleClass().add("label-Red");
            if (e.getMessage().equals("Username taken")) {
                nameInput.setPromptText("Username taken");

            }
            else if (e.getMessage().equals("Invalid username")) {
                nameInput.setPromptText("Invalid username");
            }
            else {
                e.printStackTrace();
                nameInput.setPromptText("Error");       // Alle feil som kan komme burde være dekket, men er en failsafe
            }
            return;
        }
        /*name trenger ikke valideres her ettersom begrensingen er om implementasjonen av SaveGame kan lagre det. Trenger derfor ikke ta hensyn
        til validering her ettersom SaveGame thwrower en IllegalArgumentException dersom det ikke går. Dette blir validert i kodeblokken over*/
        Game game = new Game(name, 100);
        LoadGameController.loadStakePage(game, getStage());

    }

    public void backButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WelcomePage.fxml"));
        loader.load();

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
    }
}
