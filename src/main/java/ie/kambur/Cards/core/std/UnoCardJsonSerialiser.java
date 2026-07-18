package ie.kambur.Cards.core.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ie.kambur.Cards.service.interfaces.CardJsonSerialiser;

public class UnoCardJsonSerialiser implements CardJsonSerialiser<UnoCard> {
    @Override
    public JsonNode serialise(UnoCard card) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();

        if (card.isBlackSuit()) {
            json.put("type", "uno_wild");
            json.put("wildType", card.getWildType().name());
            int ordinal = card.getOrdinal();
            if (ordinal >= 0) {
                json.put("ordinal", ordinal);
            }
        } else {
            json.put("type", "uno");
            json.put("colour", card.getColour().name());
            json.put("rank", card.getRank().name());
        }

        return json;
    }

    @Override
    public UnoCard deserialise(JsonNode json) {
        if ("uno_wild".equals(json.get("type").asText())) {
            UnoCard.Colour.Wild wildType = UnoCard.Colour.Wild.valueOf(json.get("wildType").asText());
            UnoCard card = new UnoCard(wildType);
            if (json.has("ordinal")) {
                card.setOrdinal(json.get("ordinal").asInt());
            }
            return card;
        } else {
            UnoCard.Colour colour = UnoCard.Colour.valueOf(json.get("colour").asText());
            UnoCard.Rank rank = UnoCard.Rank.valueOf(json.get("rank").asText());
            return new UnoCard(colour, rank);
        }
    }

    @Override
    public Class<UnoCard> getCardType() {
        return UnoCard.class;
    }
}