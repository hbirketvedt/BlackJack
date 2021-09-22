package controllers;

import app.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InsurancePromptController {
    private Stage stage;
    private Game game;

    @FXML
    private Label insuranceLabel;

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) insuranceLabel.getScene().getWindow();
        }
        return stage;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void init() {
        insuranceLabel.setText("for " + getGame().getInsuranceCost() + " coins?");
    }

    public void yesButtonAction() {
        getGame().buyInsurance();
        getStage().close();
    }

    public void noButtonAction() {
        getStage().close();
    }
}
