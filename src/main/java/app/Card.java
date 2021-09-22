package app;

public class Card {
    private final char suit;
    private final int face;

    public Card(char suit, int face) throws IllegalArgumentException {
        if (!(1 <= face && face <= 13)) {
            throw new IllegalArgumentException("Suit must be in range 1 - 13");
        }
        if ("SHDC".indexOf(suit) == -1) {
            throw new IllegalArgumentException("Suit of card must be either S, H, D or C");
        }
        this.suit = suit;
        this.face = face;
    }

    public char getSuit() {
        return suit;
    }

    public int getFace() {
        return face;
    }

    public String toString() {
        return "" + this.getFace() + this.getSuit();
    }


}
