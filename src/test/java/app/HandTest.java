package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Stack;

public class HandTest {

    private CardDeck deck = new CardDeck(1);
    private Card s1 = new Card('S', 1);
    private Card s13 = new Card('S', 13);
    private Card h1 = new Card('H', 1);
    private Card d5 = new Card('D', 5);
    private Card d7 = new Card('D', 7);
    private Card d10 = new Card('D', 10);
    private Hand hand1 = new Hand(deck);

    @Test
    public void testConstructor() { // Tester konstruktør, numberOfCards() og getCards()
        Assertions.assertEquals(2, new Hand(deck).numberOfCards()); // Tester at konstruktøren som faktisk brukes trekker riktig antall kort
        Stack<Card> controlStack = new Stack<>();
        controlStack.push(s1);
        controlStack.push(s13);
        Assertions.assertArrayEquals(new Stack[]{controlStack}, new Stack[]{new Hand(s1, s13).getCards()});
    }

    @Test
    public void testDrawCard() { // Tester drawCard() og resetHand()
        Assertions.assertEquals(2, hand1.numberOfCards());
        hand1.drawCard(deck);
        Assertions.assertEquals(3, hand1.numberOfCards());
        hand1.drawCard(deck);
        Assertions.assertEquals(4, hand1.numberOfCards());
        hand1.drawCard(deck);
        Assertions.assertEquals(5, hand1.numberOfCards());
        hand1.drawCard(deck);
        Assertions.assertEquals(6, hand1.numberOfCards());
        hand1.resetHand(deck);
        Assertions.assertEquals(2, hand1.numberOfCards());
        hand1.drawCard(deck);
        Assertions.assertEquals(3, hand1.numberOfCards());
        hand1.resetHand(deck);
        Assertions.assertEquals(2, hand1.numberOfCards());
    }

    @Test
    public void testGetValue() {
        Assertions.assertEquals(12, new Hand(d5, d7).getValue());
        Hand hand2 = new Hand(d10, s1);
        Assertions.assertEquals(21, hand2.getValue());
        hand2.addCard(d5);
        Assertions.assertEquals(16, hand2.getValue()); // tester at verdien til ess blir satt til 1 hvis total verdi overstiger 21
        Hand hand3 = new Hand(d10, s1);
        Assertions.assertEquals(21, hand3.getValue());
        hand3.addCard(h1);
        Assertions.assertEquals(12, hand3.getValue()); // tester at to ess gir verdi 12, altså at ett av essene gir verdi 11
        Hand hand4 = new Hand(s1, h1);
        Assertions.assertEquals(12, hand4.getValue());
        hand4.addCard(d5);
        Assertions.assertEquals(17, hand4.getValue()); // tester ett ess med verdi 11, ett med 1 og så legge til kort
        hand4.addCard(d7);
        Assertions.assertEquals(14, hand4.getValue()); // tester at verdien til ess nummer to blir satt til 1

    }


}
