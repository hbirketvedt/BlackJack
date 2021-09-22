package controllers;

import app.Game;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GamePageController {
    private Game game;
    private Stage stage;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label coinLabel;
    @FXML
    private Button drawButton, passButton, doubleDownButton;
    @FXML
    private Label playerCardsLabel, dealerCardsLabel, winnerLabel;
    @FXML
    private HBox dealerImageBar, playerImageBar, buttonBar;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Stage getStage() {
        if (this.stage == null) {
            this.stage = (Stage) coinLabel.getScene().getWindow();
        }
        return stage;
    }

    public void refresh() {
        coinLabel.setText("Coins: " + getGame().getCoins());
        playerCardsLabel.setText("Value: " + getGame().getPlayerHandValue());
    }

    public void init() throws IOException { // Avhengig av å få game og stage før den kan initialisere, så kaller manuelt istedenfor å bruke Initialize()
        getStage().setOnCloseRequest(e -> {
            e.consume();
            StakePageController.quitGame(getGame(), getStage());
        });
        loadDealerImagesHidden();
        loadPlayerImages();
        refresh();

        if (getGame().offerInsurance()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InsurancePrompt.fxml"));
            try {
                loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            InsurancePromptController insurancePromptController = loader.getController();
            Stage insuranceStage = new Stage();
            insurancePromptController.setGame(getGame());
            insurancePromptController.init();

            insuranceStage.setTitle("Insurance");
            insuranceStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.getRoot(), 500, 300);
            insuranceStage.setScene(scene);
            insuranceStage.setX(200);
            insuranceStage.setY(200);
            insuranceStage.showAndWait();
        }
        refresh(); // refresher i tilfelle man kjøper insurance, så oppdaterer den labelen med en gang
        if (!getGame().canDoubleDown()) {
            anchorPane.getChildren().remove(doubleDownButton);
        }
        continueGame();

    }


    public void continueGame() throws IOException {
        refresh();
        loadPlayerImages();
        if (getGame().playerFinished()) {
            displayWinner();
        }
    }

    public void loadPlayerImages() throws IOException {
        playerImageBar.getChildren().clear();
        for (int i = 0; i < getGame().getPlayerHand().numberOfCards(); i++) {
            BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/png/" + getGame().getPlayerHand().getCards().elementAt(i) + ".png")));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            playerImageBar.getChildren().add(imageView);
        }
    }

    public void loadDealerImagesHidden() throws IOException {
        dealerImageBar.getChildren().clear();
        BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/png/" + getGame().getDealerHand().getCards().elementAt(0) + ".png")));
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        dealerImageBar.getChildren().add(imageView);
        BufferedImage bufferedImage2 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/png/red_back.png")));
        Image image2 = SwingFXUtils.toFXImage(bufferedImage2, null);
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(150);
        imageView2.setPreserveRatio(true);
        dealerImageBar.getChildren().add(imageView2);
    }

    public void loadDealerImages() throws IOException {
        dealerImageBar.getChildren().clear();
        for (int i = 0; i < getGame().getDealerHand().numberOfCards(); i++) {
            BufferedImage bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/png/" + getGame().getDealerHand().getCards().elementAt(i) + ".png")));
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            dealerImageBar.getChildren().add(imageView);
        }
    }

    public void drawButtonAction() throws IOException {
        anchorPane.getChildren().remove(doubleDownButton);
        getGame().playerDrawCard();
        continueGame();
    }

    public void passButtonAction() throws IOException {
        displayWinner();
    }

    public void doubleDownButtonAction() throws IOException {
        getGame().doubleDown();
        continueGame();
    }

    public void displayWinner() throws IOException {
        getGame().calculateWinner();
        anchorPane.getChildren().remove(doubleDownButton);
        winnerLabel.setText(getGame().getWinnerLabel());
        coinLabel.setText("Coins: " + getGame().getCoins());
        loadDealerImages();
        dealerCardsLabel.setText("Value: " + getGame().getDealerHand().getValue());

        buttonBar.getChildren().clear();
        Button playAgain = new Button("Play again");
        playAgain.setMinWidth(120);
        playAgain.setMinHeight(54);
        playAgain.setDefaultButton(true);
        Button quit = new Button("Quit");
        quit.setMinWidth(120);
        quit.setMinHeight(54);
        quit.setCancelButton(true);
        buttonBar.getChildren().addAll(quit, playAgain);
        buttonBar.setStyle("-fx-font-size:25");
        playAgain.setOnAction(e -> resetGame());
        quit.setOnAction(e -> StakePageController.quitGame(getGame(),getStage()));
        coinLabel.requestFocus();
        if (getGame().getWinnerLabel().matches("Game Over")) {
            getStage().setOnCloseRequest(e -> { // Gjør så man ikke får spørsmål om man skal lagre hvis filen er slettet
            });
            quit.setOnAction(e -> getStage().close()); // Quit button avslutter applikasjonen uten å spørre om man vil lagre, siden man nettopp har tapt og filen er slettet.
            playAgain.setText("Main Menu");
            playAgain.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/WelcomePage.fxml"));
            try {
                loader.load();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
            getStage().setScene(scene);
            });

        }
    }

    public void resetGame() {
        winnerLabel.setText("");
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("view/StakePage.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StakePageController stakePageController = loader.getController();
        stakePageController.setGame(getGame());
        stakePageController.refresh();

        Scene scene = new Scene(loader.getRoot(), getStage().getScene().getWidth(), getStage().getScene().getHeight());
        getStage().setScene(scene);
        }

}


