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

        json.put("colour", card.getColour().toString());
        if (card.getRank() != null) {
            json.put("rank", card.getRank().toString());
        }
        // Ordinal is required to distinguish identical card instances (e.g., two RED ZEROes)
        json.put("ordinal", card.returnOrdinalPosition());

        return json;
    }

    @Override
    public UnoCard deserialise(JsonNode json) {
        UnoCard.Colour colour = UnoCard.Colour.valueOf(json.get("colour").asText());
        UnoCard.Rank rank = json.has("rank") ? UnoCard.Rank.valueOf(json.get("rank").asText()) : null;
        int ordinal = json.has("ordinal") ? json.get("ordinal").asInt() : -1;

        // Single constructor — all fields passed at construction time
        return new UnoCard(colour, rank, ordinal);
    }

    @Override
    public Class<UnoCard> getCardType() {
        return UnoCard.class;
    }
}