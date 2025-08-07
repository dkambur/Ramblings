package ie.kambur.Cards.std;

import ie.kambur.Cards.interfaces.Card;

public class StandardCard implements Card {
	
	private enum colour { SPADES (0), CLUBS (1), DIAMONDS (2), HEARTS (3);
		final private int colour;
		
		colour (int code) {
			colour = code;
		}
		
		int getColourCode() {
			return colour;
		}
	}
	
	private enum position { ACE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7), NINE(8), TEN(9), JACK(10), QUEEN(11), KING(12);
		final private int position;
		
		position (int code) {
			position = code;
		}
		
		int getPosition() {
			return position;
		}
	}
	
	final private colour theColour;
	final private position thePosition;
	
	public StandardCard (int ordinal) {
		this.theColour = colour.values()[ordinal/13];
		this.thePosition = position.values()[ordinal%13];
	}
	
	public String toString () {
		return thePosition + " of " + theColour;
	}

	public int returnOrdinalPosition() {
		return theColour.getColourCode() * 13 + thePosition.getPosition();
	}
}