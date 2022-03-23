package ie.kambur.Cards;


import java.util.Iterator;

import ie.kambur.Cards.ShuffledDeck;
import ie.kambur.Cards.interfaces.Card;
import ie.kambur.Cards.std.StandardDeck;

public class Tester { 

	public static void main(String[] params) {
		ShuffledDeck abc = new ShuffledDeck (new StandardDeck());
		
		int i = 0;
		
		for (Iterator<Card> cardIt = abc.iterator(); cardIt.hasNext(); ) {
			Card c = cardIt.next();
			
			System.out.println("Got " + c + ":" + c.returnOrdinalPosition());
			
			if (++i % 2 == 0) {
				abc.returnCard (c);
				
				System.out.println ("Returned " + c);
			}
		}
	}
}
