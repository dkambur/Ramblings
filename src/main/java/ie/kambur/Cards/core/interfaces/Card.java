package ie.kambur.Cards.core.interfaces;

/**
 * Abstraction of card in any deck
 * @author Dalen Kambur
 *
 */
public interface Card {

	/**
	 * @return Stringified representation of a card
	 */
	String toString();
	
	/**
	 * @return ordinal position of the card in ordered deck
	 */
	int returnOrdinalPosition();
	
}
