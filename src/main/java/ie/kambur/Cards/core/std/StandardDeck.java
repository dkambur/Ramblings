package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.OrderedDeck;

public class StandardDeck implements OrderedDeck<StandardCard> {

    @Override
    public String getName() {
        return "standard52";
    }

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
