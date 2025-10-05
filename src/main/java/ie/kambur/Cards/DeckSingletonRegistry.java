package ie.kambur.Cards;

import ie.kambur.Cards.core.interfaces.Card;
import ie.kambur.Cards.core.interfaces.OrderedDeck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class DeckSingletonRegistry {
    protected static final Logger logger = LogManager.getLogger(DeckSingletonRegistry.class);

    private final Map<String, OrderedDeck<? extends Card>> registeredDecks = new HashMap<>();

    volatile private static DeckSingletonRegistry INSTANCE = new DeckSingletonRegistry();

    public DeckSingletonRegistry() {
        logger.info("Loading...");
        loadAllDecks();
    }

    public void loadAllDecks() {
        ServiceLoader<OrderedDeck> loader = ServiceLoader.load(OrderedDeck.class);

        for (OrderedDeck<?> deck : loader) {
            logger.info("Loading deck: {}", deck.getName());
            registeredDecks.put(deck.getName(), deck);
        }
    }

    public static DeckSingletonRegistry getInstance () {
        return INSTANCE;
    }

    public OrderedDeck<? extends Card> getDeck(String name) {
        return registeredDecks.get(name);
    }

    public static void reload() {
        INSTANCE = new DeckSingletonRegistry();
    }
}