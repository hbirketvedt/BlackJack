package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DealerHandTest {

    private final CardDeck bigDeck = new CardDeck(100);
    private final Card s1 = new Card('S', 1);
    private final Card d6 = new Card('D', 6);
    private final Card d7 = new Card('D', 7);
    private final Card d8 = new Card('D', 8);
    private final Card d9 = new Card('D', 9);
    private final Card d10 = new Card('D', 10);

    @Test
    public void testPlayDealerHand() {
        DealerHand testHand = new DealerHand(d10, d7);
        testHand.play(bigDeck);
        Assertions.assertEquals(17, testHand.getValue());

        testHand = new DealerHand(s1, d6);
        testHand.play(bigDeck);
        Assertions.assertEquals(17, testHand.getValue());

        testHand = new DealerHand(s1, d7);
        testHand.play(bigDeck);
        Assertions.assertEquals(18, testHand.getValue());

        testHand = new DealerHand(s1, d8);
        testHand.play(bigDeck);
        Assertions.assertEquals(19, testHand.getValue());

        testHand = new DealerHand(s1, d9);
        testHand.play(bigDeck);
        Assertions.assertEquals(20, testHand.getValue());

        testHand = new DealerHand(s1, d10);
        testHand.play(bigDeck);
        Assertions.assertEquals(21, testHand.getValue());

        while (bigDeck.getCardCount() > 15) { // kjører gjennom 100 kortstokker og sjekker at playDealerHand() alltid gir getValue() >= 17 og mindre eller lik 26
            testHand = new DealerHand(bigDeck);
            testHand.play(bigDeck);
            Assertions.assertTrue(testHand.getValue() >= 17 && testHand.getValue() <=26);
        }

    }
}
