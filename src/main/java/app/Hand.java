package app;

import java.util.Stack;

public class Hand {
    private Stack<Card> cards;
    private int unusedAces;

    public Hand(Card card1, Card card2) { // Brukes utelukkende til testing
        Stack<Card> cards = new Stack<>();
        cards.push(card1);
        cards.push(card2);
        this.cards = cards;
    }

    public Hand(CardDeck deck) {
        Stack<Card> cards = new Stack<>();
        cards.push(deck.drawCard());
        cards.push(deck.drawCard());
        this.cards = cards;
    }

    public void resetHand(CardDeck deck) {
        Stack<Card> newCards = new Stack<>();
        newCards.push(deck.drawCard());
        newCards.push(deck.drawCard());
        setCards(newCards);
    }

    public Stack<Card> getCards() {
        Stack<Card> copiedHand = new Stack<>();
        copiedHand.addAll(cards);
        return copiedHand;
    }

    public void setCards(Stack<Card> cards) {
        Stack<Card> copied = new Stack<>();
        copied.addAll(cards);
        this.cards = copied;
    }

    private int getUnusedAces() {
        return unusedAces;
    }

    private void setUnusedAces(int unusedAces) {
        this.unusedAces = unusedAces;
    }

    public String toString() {  // brukes utelukkende til feilsøking og testing
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.getCards().size(); i++) {
            stringBuilder.append(this.getCards().get(i));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public void drawCard(CardDeck deck) {
        Card card = deck.drawCard();
        Stack<Card> copy = getCards();
        copy.push(card);
        setCards(copy);
    }

    public int getValue() {
        int value = 0;
        this.setUnusedAces(0); // resetter mellom hver gang fordi den teller kortene på nytt
        if (this.getCards().size() == 0) {
            value = -1;
            return value;
        }
        for (int i = 0; i < this.getCards().size(); i++) {
            int cardValue = this.getCards().get(i).getFace();
            if (cardValue== 11 || cardValue == 12 || cardValue == 13) { // alle billedkort har verdi 10
                value += 10;
            }
            else if (cardValue == 1) { // setter alltid ess til 11 først og deretter ned til en om verdien på hånden overstiger 21
                value += 11;
                this.setUnusedAces(this.getUnusedAces()+1);
            }
            else if (cardValue >= 2 && cardValue <= 10) {
                value += this.getCards().get(i).getFace();
            }
        }
        while (true) {
            if (value >= 22 && this.getUnusedAces() > 0) { // setter ess fra 11 til 1 dersom hånden overstiger 21
                value -= 10;
                this.setUnusedAces(this.getUnusedAces()-1);
            }
            else {
                break;
            }
        }
        return value;
    }

    void addCard(Card card) { // brukes utelukkende til testing, derfor den ikke er public
        Stack<Card> copy = getCards();
        copy.push(card);
        setCards(copy);
    }

    public int numberOfCards() {
        return this.getCards().size();
    }

}
