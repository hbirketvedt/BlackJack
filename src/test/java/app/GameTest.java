package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

    private final Card s1 = new Card('S', 1);
    private final Card h1 = new Card('H', 1);
    private final Card d5 = new Card('D', 5);
    private final Card d7 = new Card('D', 7);
    private final Card d10 = new Card('D', 10);
    private Game game;

    @BeforeEach
    public void setup() {
        game = new Game("Test", 100);
    }

    @Test
    public void testConstructor() {
        Assertions.assertEquals(100, game.getCoins());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.newPlay(101);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game = new Game(null, 100);
        });
        game = new Game("test", 4);
        Assertions.assertEquals(100, game.getCoins());
    }


    @Test
    public void testNewPlay() {
        game = new Game("Test", 100);
        game.newPlay(50);
        Assertions.assertEquals(2, game.getPlayerHand().numberOfCards());
        Assertions.assertEquals(2, game.getDealerHand().numberOfCards());
    }

    @Test
    public void testCanDoubleDown() {
        game.setCoins(100);
        game.newPlay(10);
        Assertions.assertTrue(game.canDoubleDown());
        game.newPlay(60);
        Assertions.assertFalse(game.canDoubleDown());
    }

    @Test
    public void testDoubleDown() {
        game.setCoins(100);
        game.newPlay(25);
        Assertions.assertEquals(2, game.getPlayerHand().numberOfCards());
        game.doubleDown();
        Assertions.assertEquals(50, game.getCoins());
        Assertions.assertEquals(3, game.getPlayerHand().numberOfCards());
    }


    @Test
    public void testCalculateWinner() {
        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d5, s1, d7);
        game.calculateWinner();
        Assertions.assertEquals(50, game.getCoins(), "Player should lose");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d7, s1, d7);
        game.calculateWinner();
        Assertions.assertEquals(100, game.getCoins(), "Should be draw");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d7, s1, d7);
        game.getPlayerHand().addCard(h1);
        game.calculateWinner();
        Assertions.assertEquals(150, game.getCoins(), "Dealer should win");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d10, s1, d7);
        game.calculateWinner();
        Assertions.assertEquals(175, game.getCoins(), "player should win 1,5x when he gets natural blackjack");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, s1, d7, d7);
        game.getDealerHand().addCard(d10);                          // Passer på at dealer får >21
        game.doubleDown();                                          // Spiller kan ikke få over 21, så vinner alltid
        Assertions.assertEquals(0, game.getCoins());
        game.calculateWinner();
        Assertions.assertEquals(200, game.getCoins(), "Player should win 2x original stake when doubling down");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d7, s1, d10);
        Assertions.assertTrue(game.offerInsurance());
        Assertions.assertEquals(25, game.getInsuranceCost());
        game.buyInsurance();
        Assertions.assertEquals(25, game.getCoins());
        game.calculateWinner();
        Assertions.assertEquals(100, game.getCoins(), "Player should get stake back when insured and dealer get BlackJack with two cards");

        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d7, s1, d5);
        game.getDealerHand().addCard(d5);
        Assertions.assertTrue(game.offerInsurance());
        Assertions.assertEquals(25, game.getInsuranceCost());
        game.buyInsurance();
        Assertions.assertEquals(25, game.getCoins());
        game.calculateWinner();
        Assertions.assertEquals(25, game.getCoins(), "Player shouldn't be payed insurance if dealer draws >2 cards");


        game.setCoins(100);
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d10, s1, d10);
        game.buyInsurance();
        game.calculateWinner();
        Assertions.assertEquals(150, game.getCoins(), "Player should get back stake plus insurance payout if dealer gets natural blackjack while player gets blackjack"); // får ut insurance 2:1 + stake

    }

    @Test
    public void testPlayerFinished() {
        game.ONLY_FOR_TESTING_adminNewPlay(10, s1, d7, s1, d5);
        Assertions.assertFalse(game.playerFinished());
        game.getPlayerHand().addCard(d10);
        Assertions.assertFalse(game.playerFinished());
        game.getPlayerHand().addCard(d10);
        Assertions.assertTrue(game.playerFinished());
    }

    @Test
    public void testGetDeck() {
        game.newPlay(50);
        Assertions.assertTrue(game.getDeck().getCardCount() > 50);
        Assertions.assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                game.playerDrawCard();
            }
        }, "Deck should reshuffle automatically");
    }

    @Test
    public void testPlayerDrawCard() {
        game.newPlay(50);
        Assertions.assertEquals(2, game.getPlayerHand().numberOfCards(), "Player should start with 2 cards");
        game.playerDrawCard();
        Assertions.assertEquals(3, game.getPlayerHand().numberOfCards(), "Drawn cards should be added to player's hand");
        game.playerDrawCard();
        Assertions.assertEquals(4, game.getPlayerHand().numberOfCards(), "Drawn cards should be added to player's hand");
        game.playerDrawCard();
        Assertions.assertEquals(5, game.getPlayerHand().numberOfCards(), "Drawn cards should be added to player's hand");
    }

    @Test
    public void testInsuranceCost() {
        game.setCoins(100000);
        game.newPlay(50);
        Assertions.assertEquals(25, game.getInsuranceCost());
        game.newPlay(200);
        Assertions.assertEquals(100, game.getInsuranceCost());
        game.newPlay(1000);
        Assertions.assertEquals(500, game.getInsuranceCost());
        game.newPlay(666);
        Assertions.assertEquals(333, game.getInsuranceCost());
    }

    @Test
    public void testAddSubtractCoins() {
        game.setCoins(100);
        Assertions.assertEquals(100, game.getCoins());
        game.addCoins(23);
        Assertions.assertEquals(123, game.getCoins());
        game.subtractCoins(72);
        Assertions.assertEquals(51, game.getCoins());
        // tester med vilje ikke at man ikke kan ha under 0 coins, ettersom denne feilhåndteringen blir tatt hånd om andre steder hvor det egner seg bedre.
        // Kunne også gjort at man ikke kan legge til/trekke fra negative tall, som i praksis bytter om funksjonene, men ser ikke noen grunn til å forhindre det.
    }

    @Test
    public void testOfferInsurance() {
        game.ONLY_FOR_TESTING_adminNewPlay(50, s1, d10, s1, d10);
        Assertions.assertTrue(game.offerInsurance());
        game.ONLY_FOR_TESTING_adminNewPlay(50, d10, d10, d10, d10);
        Assertions.assertFalse(game.offerInsurance());
    }

    @Test
    public void testBuyInsurance() {
        game.setCoins(100);
        game.newPlay(50);
        Assertions.assertEquals(50, game.getCoins());
        game.buyInsurance();
        Assertions.assertEquals(25, game.getCoins());

        game.setCoins(10000);
        game.newPlay(666);
        Assertions.assertEquals(9334, game.getCoins());
        game.buyInsurance();
        Assertions.assertEquals(9001, game.getCoins());
    }
}
