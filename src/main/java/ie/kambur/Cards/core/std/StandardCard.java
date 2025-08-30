package ie.kambur.Cards.core.std;

import ie.kambur.Cards.core.interfaces.Card;

public class StandardCard implements Card {
	
	public enum colour { SPADES (0), CLUBS (1), DIAMONDS (2), HEARTS (3);
		final private int colour;
		
		colour (int code) {
			colour = code;
		}
		
		int getColourCode() {
			return colour;
		}
	}
	
	public enum position { ACE(0), TWO(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7), NINE(8), TEN(9), JACK(10), QUEEN(11), KING(12);
		final private int position;
		
		position (int code) {
			position = code;
		}
		
		int getPosition() {
			return position;
		}
	}

    public colour getTheColour() {
        return theColour;
    }

    final private colour theColour;

    public position getThePosition() {
        return thePosition;
    }

    final private position thePosition;
	
	public StandardCard (int ordinal) {
		this.theColour = colour.values()[ordinal/13];
		this.thePosition = position.values()[ordinal%13];
	}

    public StandardCard (colour col, position pos) {
        this.thePosition = pos;
        this.theColour = col;
    }

	public String toString () {
		return thePosition + " of " + theColour;
	}

	public int returnOrdinalPosition() {
		return theColour.getColourCode() * 13 + thePosition.getPosition();
	}
}