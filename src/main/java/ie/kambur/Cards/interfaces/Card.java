package ie.kambur.Cards.interfaces;

/**
 * Abstraction of card in any deck
 * @author Dalen Kambur
 *
 */
public interface Card {

	/**
	 * @return Stringified representation of a card
	 */
	public String toString();
	
	/**
	 * @return ordinal position of the card in ordered deck
	 */
	public int returnOrdinalPosition();
	
}
