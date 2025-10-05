package ie.kambur.Cards.service.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.kambur.Cards.core.std.StandardCard;
import ie.kambur.Cards.service.interfaces.CardJsonSerialiser;

public class StandardCardJsonSerialiser implements CardJsonSerialiser<StandardCard> {
    @Override
    public JsonNode serialise(StandardCard card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("rank", card.getThePosition().toString());
        json.put("suit", card.getTheColour().toString());

        return json;
    }

    @Override
    public StandardCard deserialise(JsonNode json) {
        return new StandardCard( StandardCard.colour.valueOf(json.get("suit").asText()), StandardCard.position.valueOf(json.get("rank").asText()) );
    }

    @Override
    public Class<StandardCard> getCardType() {
        return StandardCard.class;
    }
}