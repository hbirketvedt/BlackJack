package controllers;

import app.Game;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class StakePageController {
    private Game game;
    private Stage stage;

    @FXML
    private Label coinsLabel, selectStakeLabel;
    @FXML
    private Button stake5, stake10, stake25, stake50, stake100, stake250, stake500, stake1000, stake5000, stake10000;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) coinsLabel.getScene().getWindow();
        }
        return stage;
    }

    public void initialize() {
        coinsLabel.requestFocus(); // gjør så ingen av knappene har fokus
    }

    public void refresh() {
        coinsLabel.setText("Coins: " + getGame().getCoins());
    }

    public void loadPage(ActionEvent event) throws IOException {
        Button buttonPressed = (Button) event.getSource();
        int stake = Integer.parseInt(buttonPressed.getText().replaceAll("\\s+", "")); // fjerner mellomrom fra tallene

//        if (!getGame().enoughCoins(stake)) {
//            selectStakeLabel.setText("Not enough coins");
//            return;
//        }
//        getGame().setStake(stake);
//        getGame().subtractCoins(stake);
        try {
            getGame().newPlay(stake);
        } catch (IllegalArgumentException i) {
            selectStakeLabel.setText("Not enough coins");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePage.fxml"));
        loader.load();

        GamePageController gamePageController = loader.getController();
        gamePageController.setGame(getGame());

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
        gamePageController.init();

    }

    public void quitButtonAction() {
        quitGame(getGame(), getStage());
    }

    public static void quitGame(Game game, Stage mainStage) { // statisk så alle controllers kan quitte
        FXMLLoader loader = new FXMLLoader(StakePageController.class.getResource("/view/SavePrompt.fxml"));

        try {
            loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SavePromptController savePromptController = loader.getController();
        Stage savePromptStage = new Stage();

        savePromptController.setGame(game);
        savePromptController.setMainStage(mainStage);

        savePromptStage.setTitle("Save Game");
        savePromptStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(loader.getRoot(), 600, 300);
        savePromptStage.setScene(scene);
        savePromptStage.show();
    }

}
