package ie.kambur.Cards.service;

import ie.kambur.Cards.core.interfaces.Card;
import ie.kambur.Cards.service.interfaces.CardJsonSerialiser;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing all card serializers
 */
public class CardSerializerRegistry {
    /**
     *
     */
    private static final Map<Class<? extends Card>, CardJsonSerialiser<? extends Card>> serializers = new HashMap<>();

    /**
     * Register a new serialiser
     *
     * @param serializer serialiser for the card type
     * @param <C> Card
     */
    public static <C extends Card> void register(CardJsonSerialiser<C> serializer) {
        serializers.put(serializer.getCardType(), serializer);
    }


    /**
     * Return Json serialiser for the type
     *
     * @param cardType card type
     * @return the serialiser
     * @param <C> the type of card
     */
    public static <C extends Card> CardJsonSerialiser<C> getSerializer(Class<C> cardType) {
        @SuppressWarnings("unchecked")
        CardJsonSerialiser<C> serializer = (CardJsonSerialiser<C>) serializers.get(cardType);
        if (serializer == null) {
            throw new IllegalArgumentException("No serializer registered for card type: " + cardType.getName());
        }
        return serializer;
    }

    /**
     * Returns serialiser for given card
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
