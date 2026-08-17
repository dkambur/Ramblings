package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.Card;

/**
 * Represents an Uno playing card (108-card deck).
 *
 * <ul>
 *   <li>Coloured cards (RED, BLUE, GREEN, YELLOW): 4 × 25 = 100 cards.
 *       Each colour has: 1 ZERO + 9×2 (ONE..NINE) + 2×(SKIP, REVERSE, DRAW_TWO) = 25 cards.</li>
 *   <li>Wild cards: 4 WILD (ordinals 100-103) + 4 WILD_DRAW_FOUR (ordinals 104-107) = 8 cards.</li>
 * </ul>
 *
 * <p>Each card has a unique ordinal (0-107) identifying its position in the deck.
 * Cards with the same colour+rank (e.g. two RED EIGHTs) are distinguished by ordinal.
 */
public class UnoCard implements Card {

    /**
     * All card descriptors. The first four are actual colours; the last two describe
     * wild cards. They share one enum because they answer the same question:
     * "what kind of card is this?"
     */
    public enum Colour { RED, BLUE, GREEN, YELLOW, WILD, WILD_DRAW_FOUR }

    public enum Rank {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        SKIP, REVERSE, DRAW_TWO
    }

    private final Colour colour;
    /** Null for wild cards (WILD / WILD_DRAW_FOUR). */
    private final Rank rank;
    private final int ordinal;

    /**
     * Validates invariants:
     * <ul>
     *   <li>WILD / WILD_DRAW_FOUR colours require null rank.</li>
     *   <li>Other colours require non-null rank.</li>
     *   <li>Ordinal must be in range 0-107.</li>
     * </ul>
     */
    public UnoCard(Colour colour, Rank rank, int ordinal) {
        if (ordinal < 0 || ordinal > 107) {
            throw new IllegalArgumentException("Ordinal out of range: " + ordinal);
        }
        boolean isWildColour = colour == Colour.WILD || colour == Colour.WILD_DRAW_FOUR;
        if (isWildColour && rank != null) {
            throw new IllegalArgumentException(colour + " must have null rank");
        }
        if (!isWildColour && rank == null) {
            throw new IllegalArgumentException(colour + " must have a non-null rank");
        }
        this.colour = colour;
        this.rank = rank;
        this.ordinal = ordinal;
    }

    public Colour getColour() { return colour; }
    public Rank getRank() { return rank; }

    /** @return true if this card is WILD or WILD_DRAW_FOUR. */
    public boolean isWild() { return colour == Colour.WILD || colour == Colour.WILD_DRAW_FOUR; }

    /** @return true if this card is specifically WILD_DRAW_FOUR. */
    public boolean isDrawFour() { return colour == Colour.WILD_DRAW_FOUR; }

    /** @return ordinal position in the UnoDeck (0-107). */
    public int getOrdinal() { return ordinal; }

    @Override
    public String toString() {
        return colour.name() + (rank != null ? " " + rank.name() : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnoCard that = (UnoCard) o;
        // Ordinal uniquely identifies each card in the deck
        return ordinal == that.ordinal;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ordinal);
    }

    /**
     * Decode an ordinal into a card.
     * <ul>
     *   <li>0-99: coloured cards (ZERO..NINE×2, SKIP×2, REVERSE×2, DRAW_TWO×2)</li>
     *   <li>100-103: WILD</li>
     *   <li>104-107: WILD_DRAW_FOUR</li>
     * </ul>
     */
    static UnoCard fromOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= 108) {
            throw new IllegalArgumentException("Invalid Uno ordinal: " + ordinal);
        }

        // Wilds occupy ordinals 100-107
        if (ordinal >= 100) {
            Colour wildColour = (ordinal >= 104) ? Colour.WILD_DRAW_FOUR : Colour.WILD;
            return new UnoCard(wildColour, null, ordinal);
        }

        Colour colour = Colour.values()[ordinal / 25];
        int pos = ordinal % 25; // 0..24

        Rank rank;
        if (pos == 0) {
            rank = Rank.ZERO;
        } else if (pos <= 18) {
            rank = Rank.values()[(pos - 1) / 2 + 1]; // ONE(1,2), TWO(3,4), ..., NINE(17,18)
        } else if (pos <= 20) {
            rank = Rank.SKIP;
        } else if (pos <= 22) {
            rank = Rank.REVERSE;
        } else {
            rank = Rank.DRAW_TWO;
        }
        return new UnoCard(colour, rank, ordinal);
    }

    @Override
    public int returnOrdinalPosition() {
        return ordinal;
    }
}
