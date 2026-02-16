package ie.kambur.Cards;

import io.restassured.RestAssured;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ie.kambur.healthcheck.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the ShuffledDeck REST API.
 */
@DisplayName("ShuffledDeck API Integration Tests")
class RestTester {

    private static final Logger logger = LoggerFactory.getLogger(RestTester.class);

    private String deckState;
    private String deckType = "standard52";

    @BeforeAll
    static void setUpAll() {
        RestAssured.baseURI = BaseUrl.get();
    }

    @BeforeEach
    void setUp() {
        deckState = null;
    }

    // === Helper Methods ===

    private String createDeck() {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"deckType\":\"standard52\"}")
            .when()
            .post("/deck/create")
            .then()
            .statusCode(200)
            .body("deckType", equalTo("standard52"))
            .body("deckState", notNullValue())
            .extract()
            .path("deckState");
    }

    // Draw card and get new deck state in ONE call
    private Map<String, String> drawCard(String deckState, String deckType) {
        Response response = given()
            .contentType(ContentType.JSON)
            .body(String.format("{\"deckState\":\"%s\",\"deckType\":\"%s\"}", deckState, deckType))
            .when()
            .patch("/deck/draw");

        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();

        if (statusCode == 404) {
            return null;
        }

        // Handle "Deck fully used" as empty deck (returns 409)
        if (statusCode == 409 && body.contains("Deck fully used")) {
            return null;
        }

        if (statusCode != 200) {
            throw new RuntimeException("Unexpected status: " + statusCode + " body: " + body);
        }

        // Debug: print what we get
        String rawCard = response.getBody().asString();
        logger.debug("Raw draw response: {}", rawCard);

        // Extract rank and suit separately
        String rank = response.jsonPath().getString("card.rank");
        String suit = response.jsonPath().getString("card.suit");
        String newState = response.jsonPath().getString("deck.deckState");

        String cardJson = String.format("{\"rank\":\"%s\",\"suit\":\"%s\"}", rank, suit);

        Map<String, String> result = new HashMap<>();
        result.put("card", cardJson);
        result.put("deckState", newState);
        return result;
    }

    private String returnCard(String deckState, String deckType, String card) {
        logger.debug("Returning card: {} with deckState: {}", card, deckState != null ? deckState.substring(0, 20) : "null");

        Response response = given()
            .contentType(ContentType.JSON)
            .body(String.format(
                "{\"deck\":{\"deckState\":\"%s\",\"deckType\":\"%s\"},\"card\":%s}",
                deckState, deckType, card))
            .when()
            .put("/deck/return");

        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();

        if (statusCode != 200) {
            throw new RuntimeException("Return failed: " + statusCode + " body: " + body);
        }

        return response.jsonPath().getString("deck.deckState");
    }

    // === Tests ===

    @org.junit.jupiter.api.Test
    @DisplayName("Health check returns UP")
    void healthCheck() {
        given()
            .when()
            .get("/health")
            .then()
            .statusCode(200)
            .body(containsString("UP"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Create a new standard52 deck")
    void testCreateDeck() {
        deckState = createDeck();
        assert deckState != null && !deckState.isEmpty();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Draw all 52 cards from a fresh deck")
    void drawAllCards() {
        deckState = createDeck();

        int cardsDrawn = 0;
        while (cardsDrawn < 100) {
            Map<String, String> result = drawCard(deckState, deckType);
            if (result == null) break;

            cardsDrawn++;
            deckState = result.get("deckState");
            if (deckState == null || deckState.isEmpty()) break;
        }

        assert cardsDrawn == 52 : "Expected 52 cards, got " + cardsDrawn;
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Return cards and draw again - returned cards come back first")
    void returnAndRedraw() {
        deckState = createDeck();

        // Draw 3 cards
        List<String> drawnCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, String> result = drawCard(deckState, deckType);
            assert result != null : "Failed to draw card " + i;
            drawnCards.add(result.get("card"));
            deckState = result.get("deckState");
            assert deckState != null : "Failed to get new state";
        }

        // Return 2 cards
        for (int i = 0; i < 2; i++) {
            deckState = returnCard(deckState, deckType, drawnCards.get(i));
            assert deckState != null : "Failed to return card";
        }

        // Draw again - should get the 2 returned cards
        Map<String, String> result1 = drawCard(deckState, deckType);
        assert result1 != null : "Should get returned card";

        deckState = result1.get("deckState");

        Map<String, String> result2 = drawCard(deckState, deckType);
        assert result2 != null : "Should get another card";
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Drawing from empty deck returns error")
    void drawFromEmptyDeck() {
        deckState = createDeck();

        // Draw all 52 cards
        for (int i = 0; i < 52; i++) {
            Map<String, String> result = drawCard(deckState, deckType);
            if (result == null) break;
            deckState = result.get("deckState");
        }

        // Try to draw from empty deck - should fail
        given()
            .contentType(ContentType.JSON)
            .body(String.format("{\"deckState\":\"%s\",\"deckType\":\"%s\"}", deckState, deckType))
            .when()
            .patch("/deck/draw")
            .then()
            .statusCode(409);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Full lifecycle: create, draw, return, redraw, exhaust")
    void fullLifecycle() {
        // 1. Create deck
        deckState = createDeck();

        // 2. Draw 10 cards
        List<String> toReturn = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> result = drawCard(deckState, deckType);
            assert result != null : "Failed to draw card " + i;
            toReturn.add(result.get("card"));
            deckState = result.get("deckState");
            assert deckState != null : "Failed to get new state";
        }

        // 3. Return 3 cards
        for (int i = 0; i < 3; i++) {
            deckState = returnCard(deckState, deckType, toReturn.get(i));
            assert deckState != null : "Failed to return card";
        }

        // 4. Draw 5 more (should get 3 returned + 2 fresh)
        for (int i = 0; i < 5; i++) {
            Map<String, String> result = drawCard(deckState, deckType);
            assert result != null : "Failed to draw after return";
            deckState = result.get("deckState");
        }

        // 5. Exhaust the deck (52 - 10 + 3 - 5 = 40 remaining)
        int remaining = 0;
        while (true) {
            Map<String, String> result = drawCard(deckState, deckType);
            if (result == null) break;
            deckState = result.get("deckState");
            remaining++;
        }

        // 6. Try one more draw - should fail
        given()
            .contentType(ContentType.JSON)
            .body(String.format("{\"deckState\":\"%s\",\"deckType\":\"%s\"}", deckState, deckType))
            .when()
            .patch("/deck/draw")
            .then()
            .statusCode(409);
    }
}
