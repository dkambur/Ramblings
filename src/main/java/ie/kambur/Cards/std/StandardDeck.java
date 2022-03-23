package ie.kambur.Cards.std;

import ie.kambur.Cards.interfaces.Card;
import ie.kambur.Cards.interfaces.OrderedDeck;

public class StandardDeck implements OrderedDeck<StandardCard> {
	
	@Override
	public StandardCard getCardFromOrdinal(int ordinal) {
		return new StandardCard(ordinal);
	}

	@Override
	public int getOrdinalFromCard(StandardCard card) {
		return card.returnOrdinalPosition();
	}

	@Override
	public int getTotalCards() {
		return 52;
	}

}
