package app;

public class Game {
    private final String playerName;
    private final FileManager fileManager = new FileManagerImpl();
    private CardDeck deck;
    private int coins;
    private Play currentPlay;

    private Play getCurrentPlay() {
        return currentPlay;
    }

    public Game(String playerName, int coins) {
        if (playerName == null) {
            throw new IllegalArgumentException("PlayerName can't be null");
        }
        if (coins < 5) {
            coins = 100; // settes til 100 dersom coins er mindre enn gyldig i spillet
        }
        this.playerName = playerName;
        this.coins = coins;
        /*Det valideres alltid at det er mulig å opprette en fil med playerName som velges før man oppretter
        en instans av Game, så derfor trengs det ikke validering av playerName her, ettersom gyldige inputs
        for playerName vil avhenge av forskjellige implementasjoner av SaveGame-interfacet.
        */
    }

    public void saveGame() {
        getFileManager().saveGame(getPlayerName(), getCoins());
    }

    public CardDeck getDeck() {
        if (this.deck == null || this.deck.getCardCount() <= 52) { // automatisk reshuffling hvis det er én kortstokk igjen
            this.setDeck(new CardDeck(7));
        }
        return this.deck;
    }

    public String getPlayerName() {
        return playerName;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void playerDrawCard() {
        getCurrentPlay().getPlayerHand().drawCard(getDeck());
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int kroner) {
        this.coins = kroner;
    }

    public void addCoins(int kroner) {
        this.setCoins(this.getCoins() + kroner);
    }

    public void subtractCoins(int kroner) {
        this.setCoins(this.getCoins() - kroner);
    }

    public boolean playerFinished() {
        return getPlayerHandValue() > 20 || getCurrentPlay().isDoubleDown();
    }

    public void setDeck(CardDeck deck) {
        this.deck = deck;
    }

    public Hand getPlayerHand() {
        return getCurrentPlay().getPlayerHand();
    }

    public DealerHand getDealerHand() {
        return getCurrentPlay().getDealerHand();
    }

    public int getInsuranceCost() {
        return getCurrentPlay().getInsuranceCost();
    }

    public boolean offerInsurance() {
        return getCurrentPlay().getDealerHand().visibleAce() && getCoins() >= getCurrentPlay().getStake()/2;
    }

    public void buyInsurance() {
        subtractCoins(getCurrentPlay().getInsuranceCost()); // insuranceCost settes automatisk av getteren dersom den ikke allerede er satt
        getCurrentPlay().setInsurance(true);
    }

    public int getPlayerHandValue() {
        return getCurrentPlay().getPlayerHand().getValue();
    }

    public boolean canDoubleDown() {
        return this.getCoins() >= getCurrentPlay().getStake();
    }

    public void doubleDown() {
        this.subtractCoins(getCurrentPlay().getStake());
        getCurrentPlay().setStake(getCurrentPlay().getStake() * 2);
        getCurrentPlay().setDoubleDown(true);
        getCurrentPlay().getPlayerHand().drawCard(this.getDeck());
    }

    public void newPlay(int stake) throws IllegalArgumentException {
        if (getCoins() < stake) {
            throw new IllegalArgumentException("Not enough coins");
        }
        this.currentPlay = new Play(stake);
    }

    void ONLY_FOR_TESTING_adminNewPlay(int stake, Card playerCard1, Card playerCard2, Card dealerCard1, Card dealerCard2) { // NB! brukes kun til testing
        if (getCoins() < stake) {
            throw new IllegalArgumentException("Not enough coins");
        }
        this.currentPlay = new Play(stake, playerCard1, playerCard2, dealerCard1, dealerCard2);
    }

    public void calculateWinner() {
        getCurrentPlay().calculateWinner();
    }

    public String getWinnerLabel() {
        return getCurrentPlay().getWinnerLabel();
    }

    private class Play{
        private Hand playerHand;
        private DealerHand dealerHand;
        private int stake;
        private int insuranceCost;
        private boolean insurance;
        private boolean doubleDown;
        private String winnerLabel;

        public Play(int stake) {
            this.stake = stake;
            subtractCoins(stake);
            this.playerHand = new Hand(getDeck());
            this.dealerHand = new DealerHand(getDeck());
        }

        public Play(int stake, Card playerCard1, Card playerCard2, Card dealerCard1, Card dealerCard2) { // brukes til testing
            this.stake = stake;
            subtractCoins(stake);
            setPlayerHand(new Hand(playerCard1, playerCard2));
            setDealerHand(new DealerHand(dealerCard1, dealerCard2));
        }

        public Hand getPlayerHand() {
            return playerHand;
        }

        public void setPlayerHand(Hand playerHand) {
            this.playerHand = playerHand;
        }

        public DealerHand getDealerHand() {
            return dealerHand;
        }

        public void setDealerHand(DealerHand dealerHand) {
            this.dealerHand = dealerHand;
        }

        public int getStake() {
            return stake;
        }

        public void setStake(int stake) {
            this.stake = stake;
        }

        public boolean isInsurance() {
            return insurance;
        }

        public void setInsurance(boolean insurance) {
            this.insurance = insurance;
        }

        public boolean isDoubleDown() {
            return doubleDown;
        }

        public void setDoubleDown(boolean doubleDown) {
            this.doubleDown = doubleDown;
        }

        public String getWinnerLabel() {
            return winnerLabel;
        }

        public void setWinnerLabel(String winnerLabel) {
            this.winnerLabel = winnerLabel;
        }

        public int getInsuranceCost() {
            if (this.insuranceCost == 0) {
                this.insuranceCost = this.getStake()/2; // Lagres som egen verdi i tilfelle man kjøper insurance og så doubler down etterpå
            }
            return this.insuranceCost;
        }

        public void calculateWinner() {
            this.getDealerHand().play(getDeck());
            int dealerValue = this.getDealerHand().getValue();
            int playerValue = this.getPlayerHand().getValue();
            int prize = 0;

            if (this.isInsurance() && this.getDealerHand().numberOfCards() == 2 && this.getDealerHand().getValue() == 21) {
                prize += this.getInsuranceCost()*3;
            }
            if (playerValue >= 22) {
                if (prize != 0) {
                    this.setWinnerLabel("Insurance: You Won " + prize + " coins");
                }
                else {
                    this.setWinnerLabel("You Lost");
                }
            }
            else if (playerValue == 21 && this.getPlayerHand().numberOfCards() == 2 && dealerValue != 21) {
                prize += this.getStake() * 2.5;
                this.setWinnerLabel("Natural! You won " + prize + " coins");
            }
            else if (dealerValue >= 22) {
                prize += this.getStake() * 2;
                this.setWinnerLabel("You Won " + prize + " coins");
            }
            else if (dealerValue == playerValue) {
                prize += this.getStake();
                if (prize != 0) {
                    this.setWinnerLabel("Insurance: You won " + prize + " coins");
                }
                this.setWinnerLabel("Draw");
            }
            else if (dealerValue > playerValue) {
                if (prize != 0) {
                    this.setWinnerLabel("Insurance: You won " + prize + " coins");
                }
                else {
                    this.setWinnerLabel("You Lost");
                }
            }
            else if (dealerValue < playerValue) {
                prize += this.getStake() * 2;
                this.setWinnerLabel("You Won " + prize + " coins");
            }
            addCoins(prize);
            if (getCoins() < 5) {  // minste stake er 5, så kan ikke spille mer dersom man har så lite
                setWinnerLabel("Game Over");
                saveGame();
            }
        }
    }

}
