package app;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CardDeckTest {
    private final CardDeck deck1 = new CardDeck(1);
    private final CardDeck deck2 = new CardDeck(1);

    private Collection<Card> cardArrayList(CardDeck deck){
        Collection<Card> cards = new ArrayList<>();
        int deckCount = deck.getCardCount();  // lager egen variabel ettersom deck.getCardCount minker hver gang man trekker et kort
        for (int i=0; i<deckCount; i++) {
            cards.add(deck.getCard(i));
        }
        return cards;
    }

    @Test
    public void testConstructor() {  // tester konstruktør og getCardCount()
        Assertions.assertEquals(52, new CardDeck(1).getCardCount());
        Assertions.assertEquals(104, new CardDeck(2).getCardCount());
        Assertions.assertEquals(156, new CardDeck(3).getCardCount());
        Assertions.assertEquals(364, new CardDeck(7).getCardCount());
        Collection<Card> cards = this.cardArrayList(deck1);
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1S")).count()); // tester at det bare er en av hver
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13S")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1H")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13H")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1D")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13D")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1H")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13H")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("3H")).count());
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("7D")).count());

    }

    @Test
    public void testGetCard() { // CardDeck kommer ferdig shufflet så kan ikke teste at et kort er på "riktig plass"
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            deck1.getCard(-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            deck1.getCard(deck1.getCardCount());
        });
    }

    @Test
    public void testDrawCard() {
        Assertions.assertEquals(52, deck1.getCardCount());
        Assertions.assertEquals(deck1.getCard(deck1.getCardCount()-1), deck1.drawCard());
        Assertions.assertEquals(51, deck1.getCardCount());
        Assertions.assertEquals(deck1.getCard(deck1.getCardCount()-1), deck1.drawCard());
        Assertions.assertEquals(50, deck1.getCardCount());
        for (int i = 0; i < 50; i++) {
            deck1.drawCard();
        }
        Assertions.assertThrows(IllegalStateException.class, () -> {
            deck1.drawCard();
        });
    }

    @Test
    public void testShuffle() { // tester shuffle()
        Assertions.assertEquals(52, deck2.getCardCount(), "Deck doesn't have 52 cards");
        deck2.shuffle();
        Assertions.assertEquals(52, deck2.getCardCount(), "Different card count after shuffling");
        Collection<Card> cards = this.cardArrayList(deck2);
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1S")).count(), "Card lost during shuffling"); // tester at det bare er en av hver
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13S")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1H")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13H")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1D")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13D")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("1H")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("13H")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("3H")).count(), "Card lost during shuffling");
        Assertions.assertEquals(1, cards.stream().filter(p -> p.toString().equals("7D")).count(), "Card lost during shuffling");

        CardDeck deck4 = new CardDeck(1);
        ArrayList<Card> deck4clone = new ArrayList<>(cardArrayList(deck4));
        deck4.shuffle();
        ArrayList<Card> deck = new ArrayList<>(cardArrayList(deck4));

        int counter = 0;

        for (int i = 0; i < deck4.getCardCount(); i++) {
            if (deck.get(i).getSuit() == deck4clone.get(i).getSuit() && deck.get(i).getFace() == deck4clone.get(i).getFace()) {
                counter += 1;
            }
        }
        Assertions.assertTrue(counter < 6, "6 or more cards were in the same spot before and after shuffling. Please shuffle better");
        System.out.println(counter +" cards were in the same spot after shuffling");
    }
}
