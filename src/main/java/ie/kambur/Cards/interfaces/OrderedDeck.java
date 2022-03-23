package ie.kambur.Cards.interfaces;

/**
 * @author Dalen Kambur
 *
 * Interface providing methods to convert an ordinal position of card in deck into visualised
 */
public interface OrderedDeck<C extends Card> {
	
	/**
	 * Returns a Card matching the ordinal
	 * @param ordinal
	 * @return
	 */
	public C getCardFromOrdinal(int ordinal);
	
	
	/**
	 * Converts a card into its ordinal position
	 * @param card
	 * @return
	 */
	public int getOrdinalFromCard(C card);
	
	
	/**
	 * @return total cards in the deck
	 */
	public int getTotalCards();
}
