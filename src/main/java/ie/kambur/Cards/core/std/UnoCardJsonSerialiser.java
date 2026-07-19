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

        json.put("colour", card.isBlackSuit() ? card.getWildType().name() : card.getColour().toString());
        if (!card.isBlackSuit()) json.put("rank", card.getRank().toString());
        int ordinal = card.getOrdinal();
        if (ordinal >= 0) json.put("ordinal", ordinal);

        return json;
    }
1
    @Override
    public UnoCard deserialise(JsonNode json) {
        String colourName = json.get("colour").asText();

        if (json.has("rank")) {
            // Standard card: colour + rank
            UnoCard.Colour c = UnoCard.Colour.valueOf(colourName);
            UnoCard.Rank r = UnoCard.Rank.valueOf(json.get("rank").asText());
            return new UnoCard(c, r);
        } else {
            // Wild card: colour encodes the type directly
            UnoCard.Colour.Wild wild = UnoCard.Colour.Wild.valueOf(colourName);
            UnoCard card = new UnoCard(wild);
            if (json.has("ordinal")) card.setOrdinal(json.get("ordinal").asInt());
            return card;
        }
    }

    @Override
    public Class<UnoCard> getCardType() {
        return UnoCard.class;
    }
}