package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.OrderedDeck;

/**
 * Standard 108-card Uno deck.
 * Coloured cards (RED/BLUE/GREEN/YELLOW): 4 × 25 ordinals = 100 (ZERO + ONE..NINE×2 + SKIP×2 + REVERSE×2 + DRAW_TWO×2)
 * BLACK wilds: 8 cards at ordinals 100-107 (WILD and WILD_DRAW_FOUR as Colour.Wild values)
 */
public class UnoDeck implements OrderedDeck<UnoCard> {

    private static final int BLOCK_SIZE = 25; // ordinals per colour block
    private static final int COLOURED_TOTAL = 4 * BLOCK_SIZE; // 100

    @Override
    public String getName() {
        return "uno";
    }

    @Override
    public UnoCard getCardFromOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= 108) {
            throw new IllegalArgumentException("Invalid Uno ordinal: " + ordinal);
        }

        // BLACK wilds occupy ordinals 100-107: WILD at even offsets, WILD_DRAW_FOUR at odd
        if (ordinal >= COLOURED_TOTAL) {
            int offset = ordinal - COLOURED_TOTAL;
            UnoCard card = new UnoCard(UnoCard.Colour.Wild.values()[offset % 2]);
            card.setOrdinal(ordinal);
            return card;
        }

        int colourIndex = ordinal / BLOCK_SIZE;
        UnoCard.Colour colour = UnoCard.Colour.values()[colourIndex];
        int pos = ordinal % BLOCK_SIZE; // 0..24

        int rankIndex;
        if (pos == 0) {
            rankIndex = 0; // ZERO
        } else if (pos <= 18) {
            // Positions 1-18: pairs for ONE(1,2), TWO(3,4), ..., NINE(17,18)
            rankIndex = (pos - 1) / 2 + 1;
        } else if (pos <= 20) {
            // SKIP at positions 19, 20
            rankIndex = 10;
        } else if (pos <= 22) {
            // REVERSE at positions 21, 22
            rankIndex = 11;
        } else {
            // DRAW_TWO at position 23, 24
            rankIndex = 12;
        }

        UnoCard card = new UnoCard(colour, UnoCard.Rank.values()[rankIndex]);
        card.setOrdinal(ordinal);
        return card;
    }

    @Override
    public int getOrdinalFromCard(UnoCard card) {
        if (card.isBlackSuit()) {
            return card.getOrdinal();
        }
        int colourCode = card.getColour().ordinal();
        int rankIndex = card.getRank().ordinal();

        int positionInColour;
        if (rankIndex == 0) {
            positionInColour = 0; // ZERO
        } else if (rankIndex >= 1 && rankIndex <= 9) {
            positionInColour = (rankIndex - 1) * 2 + 1; // ONE→1, TWO→3, ..., NINE→17
        } else if (rankIndex == 10) {
            positionInColour = 19; // SKIP
        } else if (rankIndex == 11) {
            positionInColour = 21; // REVERSE
        } else { // rankIndex == 12
            positionInColour = 23; // DRAW_TWO
        }

        return colourCode * BLOCK_SIZE + positionInColour;
    }

    @Override
    public int getTotalCards() {
        return 108;
    }
}