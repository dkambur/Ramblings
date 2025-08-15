package ie.kambur.Cards;


import ie.kambur.Cards.std.StandardCard;
import ie.kambur.Cards.std.StandardDeck;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Tester {
	protected static final Logger logger = LogManager.getLogger(Tester.class);

	public static void main(String[] params) {
        var abc = new ShuffledDeck<> (new StandardDeck(), new Random());
		
		int i = 0;

        for (StandardCard c : abc) {
            logger.info("Got {}:{}", c, c.returnOrdinalPosition());

            if (++i % 2 == 0) {
                abc.returnCard(c);

                logger.info("Returned {}", c);
            }
        }
	}
}
