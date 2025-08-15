package ie.kambur.Cards;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import ie.kambur.Cards.interfaces.Card;
import ie.kambur.Cards.interfaces.OrderedDeck;

public class ShuffledDeck<C extends Card, T extends OrderedDeck<C> > implements Iterable<C> {
	/**
	 * Deck containing all cards
	 */
	final private OrderedDeck<C> theDeck;
	
	/**
	 * Bit is set for each used card
	 */
	private BitSet usedCards;

	/**
	 * Bit is unset for each returned card i.e. opposite logic to <code>usedCards</code>
	 */
	private BitSet returnedCards;
	
	/**
	 * Private instance of random
	 */
	final private Random rnd;
	
	/**
	 * Number of cards left in the deck
	 * We do not use cardinality on BitSet as it is imprecise.
	 */
	private int cardsLeft;
	
	/**
	 * Cards in return section of the deck
	 */
	private int cardsReturned;
	
	/**
	 * Creates iterator for shuffled deck.
	 * @param theDeck ordered deck to shuffle
	 */
	public ShuffledDeck(T theDeck, Random rnd) {
		this.theDeck = theDeck;
		
		usedCards = new BitSet (cardsLeft = theDeck.getTotalCards());
		returnedCards = new BitSet (cardsLeft);
		returnedCards.set (0, cardsLeft); 
				
		cardsReturned = 0;
		this.rnd = rnd;
	}
	
	/**
	 * Swaps used and returned cards
	 */
	private void swapsies () {
		// Swap returned cards and cards left
		
		BitSet temp = returnedCards;
		returnedCards = usedCards;
		usedCards = temp;
		
		int tempInt = cardsReturned;
		cardsReturned = cardsLeft;
		cardsLeft = tempInt;		
	}

	public Iterator<C> iterator() {
		return new Iterator<>() {
			public boolean hasNext() {
				return cardsLeft > 0 || cardsReturned > 0;
			}

			public C next() {
				
				if (hasNext()) {
					
					// Small quirk here: if there is no cards left and we're here, that means we need
					// to take cards from returned section of the deck.
					if (cardsLeft == 0) 
						swapsies();
					
					// We'll get first random number as start
					// -1 because number returned is between 0...n and not n-1 as one would expect
					int card = rnd.nextInt (theDeck.getTotalCards() - 1);
					
					// We'll pick first next unused card
					card = usedCards.nextClearBit (card);
					
					// And if we're at the end of the deck, let's go from scratch
					// The reason for dual check is that JavaDoc indicate -1 to be the end however 
					// it'd appear BitSet stops at the end of the word, not used bits.
					if (card == -1 || card == theDeck.getTotalCards()) {
						card = usedCards.nextClearBit (0);

					}
					
					// mark as used up
					usedCards.set (card);
					
					// reduce number of cards left in deck
					cardsLeft--;
					
					// and convert into actual card
					return theDeck.getCardFromOrdinal (card);
					
				}
				else
					throw new NoSuchElementException ("Deck fully used");
			}
			
		};
		
	}
	
	/**
	 * Returns a card to the deck.
	 * 
	 * @param card to return
	 * @throws NoSuchElementException if card was already returned
	 */
	public void returnCard (C card) throws NoSuchElementException {
		int ordinal = card.returnOrdinalPosition();
		
		// Because of inverse logic, 
		if (returnedCards.get (ordinal)) {
			returnedCards.clear (ordinal);
			cardsReturned++;
		}
		else
			throw new NoSuchElementException ("Card already returned");
	}
}
