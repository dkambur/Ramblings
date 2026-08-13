package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.OrderedDeck;

/**
 * Standard 108-card Uno deck.
 * Coloured cards (RED/BLUE/GREEN/YELLOW): 4 × 25 ordinals = 100 (ZERO + ONE..NINE×2 + SKIP×2 + REVERSE×2 + DRAW_TWO×2)
 * Wilds: 8 cards at ordinals 100-107 (colour=WILD, rank=null)
 */
public class UnoDeck implements OrderedDeck<UnoCard> {

    @Override
    public String getName() {
        return "uno";
    }

    @Override
    public UnoCard getCardFromOrdinal(int ordinal) {
        // Delegate to card's static factory — all mapping logic lives in the card
        return UnoCard.fromOrdinal(ordinal);
    }

    @Override
    public int getOrdinalFromCard(UnoCard card) {
        // Delegate to card's own ordinal — it knows its position in the deck
        return card.returnOrdinalPosition();
    }

    @Override
    public int getTotalCards() {
        return 108;
    }
}