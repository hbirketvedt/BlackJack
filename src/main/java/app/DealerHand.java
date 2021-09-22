package app;

public class DealerHand extends Hand{

    public DealerHand(CardDeck deck) {
        super(deck);
    }

    public DealerHand(Card card1, Card card2) {
        super(card1, card2);
    }

    public void play(CardDeck deck) {
        while (this.getValue() < 17) {
            this.drawCard(deck);
        }
    }

    public Card getVisibleCard() {
        return this.getCards().get(0);
    }

    public boolean visibleAce() {
        return this.getVisibleCard().getFace() == 1;
    }
}
