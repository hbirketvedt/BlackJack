package app;

import java.util.Stack;

public class CardDeck {
    private Stack<Card> cards;

    public CardDeck(int numberOfDecks) {
        Stack<Card> cards = new Stack<>();
        String suits = "SHDC";
        for (int i = 0; i < numberOfDecks; i++) {
            for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
                for (int face = 0; face < 13; face++) {
                    Card card = new Card(suits.charAt(suitIndex), face + 1);
                    cards.push(card);
                }
            }
        }
        this.cards = cards;
        shuffle();
    }

    public Stack<Card> getCards() {
        Stack<Card> copiedStack = new Stack<>();
        copiedStack.addAll(cards);
        return copiedStack;
    }

    public Card drawCard() throws IllegalStateException {
        if (this.getCardCount() < 1) {
            throw new IllegalStateException("Too few cards in deck");
        }
        return cards.pop();
    }

    public void shuffle() {
        this.shuffleRandomly();
        this.shufflePerfectly();
        this.shuffleRandomly();
        this.shufflePerfectly();
        this.shuffleRandomly();
        this.shufflePerfectly();
        this.shuffleRandomly();
        this.shufflePerfectly();
        this.shuffleRandomly();
        this.shufflePerfectly();

    }

    public void setCards(Stack<Card> cards) {
        Stack<Card> copiedStack = new Stack<>();
        copiedStack.addAll(cards);
        this.cards = copiedStack;
    }

    public int getCardCount() {
        return cards.size();
    }

    Card getCard(int n) throws IllegalArgumentException { // Brukes til tester, ville vært privat ellers
        if (n >= getCardCount() || n < 0) {
            throw new IllegalArgumentException("invalid index");
        }
        return cards.get(n);
    }

    private void removeCard(int n) throws IllegalArgumentException {
        if (n >= getCardCount() || n < 0) {
            throw new IllegalArgumentException("invalid index");
        }
        cards.removeElementAt(n);
    }

    private void shufflePerfectly() {
        Stack<Card> firstHalf = new Stack<>();
        Stack<Card> secondHalf = new Stack<>();
        Stack<Card> shuffledDeck = new Stack<>();
        for (int i = 0; i < getCardCount() / 2; i++) {  // deler kortstokken i to
            firstHalf.push(cards.elementAt(i));
        }
        for (int i = cards.size() / 2; i < getCardCount(); i++) {
            secondHalf.push(cards.elementAt(i));
        }
        for (int i = 0; i < getCardCount() / 2; i++) { // setter de to stacksene sammen
            shuffledDeck.push(firstHalf.elementAt(i));
            shuffledDeck.push(secondHalf.elementAt(i));
        }
        this.setCards(shuffledDeck);
    }

    private void shuffleRandomly() {
        Stack<Card> shuffledDeck = new Stack<>();
        while (getCardCount() > 0) {
            int index = (int) (Math.random() * getCardCount());
            shuffledDeck.push(getCard(index));
            removeCard(index);
        }
        this.setCards(shuffledDeck);
    }




}
