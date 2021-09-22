package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CardTest {

    private boolean checkEquals(Card card, char suit, int face) {
        return card.getSuit() == suit && card.getFace() == face;
    }

    @Test
    public void testConstructor() {
        Assertions.assertTrue(checkEquals(new Card('S', 1), 'S', 1));
        Assertions.assertTrue(checkEquals(new Card('S', 13), 'S', 13));
        Assertions.assertTrue(checkEquals(new Card('H', 1), 'H', 1));
        Assertions.assertTrue(checkEquals(new Card('H', 13), 'H', 13));
        Assertions.assertTrue(checkEquals(new Card('D', 1), 'D', 1));
        Assertions.assertTrue(checkEquals(new Card('D', 13), 'D', 13));
        Assertions.assertTrue(checkEquals(new Card('C', 1), 'C', 1));
        Assertions.assertTrue(checkEquals(new Card('C', 13), 'C', 13));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('Q', 1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('s', 1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('S', 0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('Q', 14);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Card('Q', -1);
        });
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("2C", new Card('C', 2).toString());
        Assertions.assertEquals("13D", new Card('D', 13).toString());
    }

}
