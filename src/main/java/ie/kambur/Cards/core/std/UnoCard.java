package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.Card;
import java.util.Objects;

/**
 * Represents an Uno playing card.
 * Coloured cards (RED, BLUE, GREEN, YELLOW): 4 × 25 = 100 cards
 * WILD / WILD_DRAW_FOUR: nested under Colour.BLACK — the colour field IS both suit and type
 */
public class UnoCard implements Card {

    /** Standard colours for rank-bearing cards. BLACK wilds also live here as a nested enum value. */
    public enum Colour {
        RED, BLUE, GREEN, YELLOW;

        /** Black wild card types — they ARE the colour for wild cards, no separate flag needed. */
        public static enum Wild { WILD, WILD_DRAW_FOUR }
    }

    public enum Rank { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, DRAW_TWO; }

    /** For coloured cards: a Colour (RED/BLUE/GREEN/YELLOW). For wilds: an instance of WildColour. */
    private final Object colour;
    /** Present only for coloured cards. Null for wilds. */
    private final Rank rank;
    /** Ordinal position in the UnoDeck. Set by UnoDeck.getCardFromOrdinal(); -1 until then. */
    private int ordinal = -1;

    // coloured card (RED, BLUE, GREEN, YELLOW)
    public UnoCard(Colour colour, Rank rank) {
        this.colour = colour;
        this.rank = rank;
    }

    // wild card — a WildColour value is also assignable to Colour via casting
    public UnoCard(Colour.Wild wildType) {
        this.colour = wildType;
        this.rank = null;
    }

    protected UnoCard() { this.colour = null; this.rank = null; }

    /** Get the base colour (RED/BLUE/GREEN/YELLOW). Only valid for coloured cards. */
    public Colour getColour() {
        if (colour instanceof Colour.Wild) throw new IllegalStateException("Cannot get colour of a wild card");
        return (Colour) colour;
    }

    /** Get the specific wild type (WILD or WILD_DRAW_FOUR). Only valid for wild cards. */
    public Colour.Wild getWildType() {
        if (!isBlackSuit()) throw new IllegalStateException("Not a wild card");
        return (Colour.Wild) colour;
    }

    public Rank getRank() { return rank; }

    /** Return true if this is a black wild card. */
    public boolean isBlackSuit() {
        return colour instanceof Colour.Wild;
    }

    /** Return ordinal position in the UnoDeck, or -1 if not yet assigned. */
    public int getOrdinal() { return ordinal; }
    void setOrdinal(int ordinal) { this.ordinal = ordinal; }

    @Override
    public String toString() {
        if (isBlackSuit()) {
            return "BLACK " + ((Colour.Wild) colour).name();
        }
        return ((Colour) colour).name() + " " + rank.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnoCard unoCard = (UnoCard) o;
        if (isBlackSuit() != unoCard.isBlackSuit()) return false;
        if (isBlackSuit()) return colour == unoCard.colour; // Wild enum identity
        return ((Colour) colour) == ((Colour) unoCard.colour) && rank == unoCard.rank;
    }

    @Override
    public int hashCode() {
        if (isBlackSuit()) return Objects.hash(colour);
        return Objects.hash(((Colour) colour), rank);
    }

    @Override
    public int returnOrdinalPosition() {
        if (ordinal >= 0) return ordinal;
        throw new UnsupportedOperationException("Card not yet assigned a deck position — use UnoDeck.getOrdinalFromCard(unoCard)");
    }
}