package ie.kambur.Cards.service.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import ie.kambur.Cards.core.interfaces.Card;

/**
 * Provides serialisation and deserialisation interface
 * @param <C> the subtype of Card for which these are implemented
 */
public interface CardJsonSerialiser<C extends Card> {

    /**
     * Serialisation method
     *
     * @param card card to be serialised.
     * @return resulting JsonNode
     */
    JsonNode serialise(C card);

    /**
     * Deserialisation method
     *
     * @param json Json to be deserialised
     * @return card
     */
    C deserialise(JsonNode json);

    /**
     * Auxilary method to return Class
     *
     * @return Card class
     */
    Class<C> getCardType();
}