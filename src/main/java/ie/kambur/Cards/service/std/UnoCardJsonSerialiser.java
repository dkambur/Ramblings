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

        json.put("colour", card.getColour().name());
        if (card.getRank() != null) {
            json.put("rank", card.getRank().name());
           }
            // Ordinal disambiguates identical instances (e.g. two RED ZEROes)
        json.put("ordinal", card.returnOrdinalPosition());

        return json;
        }

          @Override
    public UnoCard deserialise(JsonNode json) {
        UnoCard.Colour colour = UnoCard.Colour.valueOf(json.get("colour").asText());
        UnoCard.Rank rank = json.has("rank")
               ? UnoCard.Rank.valueOf(json.get("rank").asText())
               : null;
        int ordinal = json.get("ordinal").asInt();

          // Constructor validates that wilds have null rank, coloured cards have non-null rank.
        return new UnoCard(colour, rank, ordinal);
        }

          @Override
    public Class<UnoCard> getCardType() {
        return UnoCard.class;
        }
}
