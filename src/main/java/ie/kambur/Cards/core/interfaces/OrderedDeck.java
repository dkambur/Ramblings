package ie.kambur.Cards.core.interfaces;

/**
 * @author Dalen Kambur
 *
 * Interface providing methods to convert an ordinal position of card in deck into visualised
 */
public interface OrderedDeck<C extends Card> {

    /**
     * @return the name the deck is known to callers
     */
    String getName ();

	/**
	 * @param ordinal the ordinal
	 * @return card matching the ordinal.
	 */
	C getCardFromOrdinal(int ordinal);

	/**
	 * @param card the card
	 * @return ordinal position of the card.
	 */
	int getOrdinalFromCard(C card);

	/**
	 * @return total cards in the deck
	 */
	int getTotalCards();
}
