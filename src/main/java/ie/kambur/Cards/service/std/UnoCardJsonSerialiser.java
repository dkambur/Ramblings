package ie.kambur.Cards.service.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.kambur.Cards.core.std.UnoCard;
import ie.kambur.Cards.service.interfaces.CardJsonSerialiser;

public class UnoCardJsonSerialiser implements CardJsonSerialiser<UnoCard> {
    @Override
    public JsonNode serialise(UnoCard card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        json.put("type", "uno");
        json.put("colour", card.getColour().toString());
        json.put("rank", card.getRank().toString());

        return json;
    }

    @Override
    public UnoCard deserialise(JsonNode json) {
        UnoCard.Colour colour = UnoCard.Colour.valueOf(json.get("colour").asText());
        UnoCard.Rank rank = UnoCard.Rank.valueOf(json.get("rank").asText());
        return new UnoCard(colour, rank);
    }

    @Override
    public Class<UnoCard> getCardType() {
        return UnoCard.class;
    }
}
