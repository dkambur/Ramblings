package ie.kambur.Cards;


import java.util.Iterator;
import ie.kambur.Cards.interfaces.Card;
import ie.kambur.Cards.std.StandardDeck;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tester {
	protected static final Logger logger = LogManager.getLogger(Tester.class);

	public static void main(String[] params) {
        var abc = new ShuffledDeck (new StandardDeck());
		
		int i = 0;
		
		for (Iterator<Card> cardIt = abc.iterator(); cardIt.hasNext(); ) {
			Card c = cardIt.next();

            logger.info("Got {}:{}", c, c.returnOrdinalPosition());
			
			if (++i % 2 == 0) {
				abc.returnCard (c);

				logger.info ("Returned {}", c);
			}
		}
	}
}
