package ie.kambur.Cards.service;

import ie.kambur.Cards.core.interfaces.Card;
import ie.kambur.Cards.service.interfaces.CardJsonSerialiser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Registry for managing all card serializers.
 * Serializers are discovered via Java's ServiceLoader mechanism (META-INF/services).
 */
public class CardSerializerRegistry {
    private static final Logger logger = LogManager.getLogger(CardSerializerRegistry.class);

    private static final Map<Class<? extends Card>, CardJsonSerialiser<? extends Card>> serializers = new HashMap<>();
    private static boolean loaded = false;

    /**
     * Load all serializers discovered via ServiceLoader.
     */
    private static synchronized void loadSerializers() {
        if (loaded) return;

        @SuppressWarnings("rawtypes")
        ServiceLoader loader = ServiceLoader.load(CardJsonSerialiser.class);
        for (Object serializerObj : loader) {
            @SuppressWarnings("unchecked")
            CardJsonSerialiser<? extends Card> serializer = (CardJsonSerialiser<? extends Card>) serializerObj;
            Class<? extends Card> cardType = serializer.getCardType();
            serializers.put(cardType, serializer);
            logger.info("Loaded card serializer for: {}", cardType.getName());
        }
        loaded = true;
    }

    /**
     * Register a new serialiser (for programmatic registration if needed).
     *
     * @param serializer serialiser for the card type
     * @param <C> Card
     */
    public static <C extends Card> void register(CardJsonSerialiser<C> serializer) {
        serializers.put(serializer.getCardType(), serializer);
    }

    /**
     * Return Json serialiser for the type.
     * Automatically loads all serializers via ServiceLoader on first call if not already loaded.
     *
     * @param cardType card type
     * @return the serialiser
     * @param <C> the type of card
     */
    public static <C extends Card> CardJsonSerialiser<C> getSerializer(Class<C> cardType) {
        if (!loaded) loadSerializers();

        @SuppressWarnings("unchecked")
        CardJsonSerialiser<C> serializer = (CardJsonSerialiser<C>) serializers.get(cardType);
        if (serializer == null) {
            throw new IllegalArgumentException("No serializer registered for card type: " + cardType.getName());
        }
        return serializer;
    }

    /**
     * Returns serialiser for given card.
     * Automatically loads all serializers via ServiceLoader on first call if not already loaded.
     *
     * @param card the card
     * @return serialise
     * @param <C> the type of card
     */
    @SuppressWarnings("unchecked")
    public static <C extends Card> CardJsonSerialiser<C> getSerializer(C card) {
        return getSerializer((Class<C>) card.getClass());
    }
}
