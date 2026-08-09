package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.Card;
import java.util.Objects;

/**
 * Represents an Uno playing card.
 * Coloured cards (RED, BLUE, GREEN, YELLOW): 4 × 25 = 100 cards
 * WILD / WILD_DRAW_FOUR: nested under Colour.BLACK — the colour field IS both suit and type
 */
public class UnoCard implements Card {

    public enum Colour { RED, BLUE, GREEN, YELLOW, WILD }

    public enum Rank { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, DRAW_TWO; }

    private final Colour colour;
    /** Null for wild cards (WILD). */
    private final Rank rank;
    /** Ordinal position in the UnoDeck. Set by UnoDeck.getCardFromOrdinal(); -1 until then. */
    private int ordinal = -1;

    public UnoCard(Colour colour, Rank rank) {
        this.colour = colour;
        this.rank = rank;
    }

    /** Wild card constructor — colour is WILD, rank is null. */
    public UnoCard() {
        this(Colour.WILD, null);
    }

    public Colour getColour() { return colour; }

    public Rank getRank() { return rank; }

    /** Return ordinal position in the UnoDeck, or -1 if not yet assigned. */
    public int getOrdinal() { return ordinal; }
    public void setOrdinal(int ordinal) { this.ordinal = ordinal; }

    @Override
    public String toString() {
        if (colour == Colour.WILD) {
            return "WILD";
        }
        return colour.name() + " " + rank.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnoCard unoCard = (UnoCard) o;
        return colour == unoCard.colour && rank == unoCard.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, rank);
    }

    @Override
    public int returnOrdinalPosition() {
        if (ordinal >= 0) return ordinal;
        throw new UnsupportedOperationException("Card not yet assigned a deck position — use UnoDeck.getOrdinalFromCard(unoCard)");
    }
}