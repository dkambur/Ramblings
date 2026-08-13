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
    private final int ordinal;

    public UnoCard(Colour colour, Rank rank, int ordinal) {
        this.colour = colour;
        this.rank = rank;
        this.ordinal = ordinal;
    }

    public Colour getColour() { return colour; }

    public Rank getRank() { return rank; }

    /** Return ordinal position in the UnoDeck (0..107). */
    public int getOrdinal() { return ordinal; }

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

    /** Position within a colour block (0..24). ZERO=0, ONE=1, TWO=3, ..., NINE=17, SKIP=19, REVERSE=21, DRAW_TWO=23. */
    int positionInColour() {
        if (rank == null) return -1; // WILD cards have no fixed position in colour block
        int rankIndex = rank.ordinal();
        if (rankIndex == 0) return 0;                          // ZERO
        if (rankIndex >= 1 && rankIndex <= 9) return (rankIndex - 1) * 2 + 1; // ONE→1, TWO→3, ..., NINE→17
        if (rankIndex == 10) return 19;                         // SKIP
        if (rankIndex == 11) return 21;                         // REVERSE
        return 23;                                              // DRAW_TWO
    }

    /** Decode ordinal into a card. Ordinals 0-99 are coloured (ZERO..NINE×2, SKIP×2, REVERSE×2, DRAW_TWO×2); 100-107 are WILD. */
    static UnoCard fromOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= 108) {
            throw new IllegalArgumentException("Invalid Uno ordinal: " + ordinal);
        }
        // Wilds occupy ordinals 100-107: colour=WILD, rank=null
        if (ordinal >= 100) {
            return new UnoCard(Colour.WILD, null, ordinal);
        }

        Colour colour = Colour.values()[ordinal / 25];
        int pos = ordinal % 25; // 0..24

        Rank rank;
        if (pos == 0) {
            rank = Rank.ZERO;
        } else if (pos <= 18) {
            // Positions 1-18: pairs for ONE(1,2), TWO(3,4), ..., NINE(17,18)
            rank = Rank.values()[(pos - 1) / 2 + 1];
        } else if (pos <= 20) {
            // SKIP at positions 19, 20
            rank = Rank.SKIP;
        } else if (pos <= 22) {
            // REVERSE at positions 21, 22
            rank = Rank.REVERSE;
        } else {
            // DRAW_TWO at position 23, 24
            rank = Rank.DRAW_TWO;
        }

        return new UnoCard(colour, rank, ordinal);
    }

    /** Compute ordinal from colour and rank. WILD cards have no fixed ordinal. */
    static int computeOrdinal(Colour colour, Rank rank) {
        if (colour == Colour.WILD || rank == null) return -1;
        return colour.ordinal() * 25 + new UnoCard(colour, rank, 0).positionInColour();
    }

    @Override
    public int returnOrdinalPosition() {
        return ordinal;
    }
}