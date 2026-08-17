package ie.kambur.Cards;

import ie.kambur.Cards.core.std.UnoCard;
import ie.kambur.Cards.core.std.UnoDeck;
import ie.kambur.Cards.service.std.UnoCardJsonSerialiser;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Standalone verification for the UnoCard / UnoDeck implementation.
 * Run: java -ea -cp <classpath> ie.kambur.Cards.UnoTester
 */
public class UnoTester {
    protected static final Logger logger = LogManager.getLogger(UnoTester.class);
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testRoundTripAllOrdinals();
        testWildDistinction();
        testHashCodeEquivalence();
        testFullDeckIteration();
        testJsonSerialisation();
        testConstructorValidation();

        System.out.println("\n=== RESULTS ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
          }
      }

        /** Every ordinal 0-107 must round-trip through UnoDeck.getCardFromOrdinal. */
    static void testRoundTripAllOrdinals() {
        UnoDeck deck = new UnoDeck();
        for (int i = 0; i < 108; i++) {
            UnoCard c = deck.getCardFromOrdinal(i);
            assert c != null : "null for ordinal " + i;
            assert c.getOrdinal() == i : "ordinal mismatch for " + i + " got " + c.getOrdinal();
            assert deck.getOrdinalFromCard(c) == i : "round-trip broken for " + i;

                 // coloured cards must have rank, wilds must have rank=null
            if (!c.isWild()) {
                assert c.getRank() != null : "coloured card " + i + " has null rank";
              } else {
                assert c.getRank() == null : "wild card " + i + " has non-null rank";
              }
          }
        pass("Round-trip all 108 ordinals");
       }

         /** WILD (100-103) and WILD_DRAW_FOUR (104-107) must be distinguishable. */
    static void testWildDistinction() {
        UnoDeck deck = new UnoDeck();

        for (int i = 100; i < 104; i++) {
            UnoCard c = deck.getCardFromOrdinal(i);
            assert c.getColour() == UnoCard.Colour.WILD : "ordinal " + i + " should be WILD, got " + c.getColour();
            assert c.isWild() : "ordinal " + i + " should be wild";
            assert !c.isDrawFour() : "ordinal " + i + " should not be draw-four";
            assert c.toString().equals("WILD") : "unexpected toString " + c.toString();
          }

        for (int i = 104; i < 108; i++) {
            UnoCard c = deck.getCardFromOrdinal(i);
            assert c.getColour() == UnoCard.Colour.WILD_DRAW_FOUR : "ordinal " + i + " should be WILD_DRAW_FOUR, got " + c.getColour();
            assert c.isWild() : "ordinal " + i + " should be wild";
            assert c.isDrawFour() : "ordinal " + i + " should be draw-four";
            assert c.toString().equals("WILD_DRAW_FOUR") : "unexpected toString " + c.toString();
          }

           // WILD and WILD_DRAW_FOUR must NOT be equals — different ordinals
        UnoCard w = deck.getCardFromOrdinal(100);
        UnoCard wdf = deck.getCardFromOrdinal(104);
        assert !w.equals(wdf) : "WILD and WILD_DRAW_FOUR should not be equal";

        pass("WILD vs WILD_DRAW_FOUR distinction");
       }

        /** All 108 cards must have unique hashCodes AND be distinct under equals. */
    static void testHashCodeEquivalence() {
        UnoDeck deck = new UnoDeck();
        Set<UnoCard> allCards = new HashSet<>();

        for (int i = 0; i < 108; i++) {
            UnoCard c = deck.getCardFromOrdinal(i);
            assert allCards.add(c) : "Duplicate in set: ordinal " + i + " " + c;
          }
        assert allCards.size() == 108 : "Expected 108 unique cards, got " + allCards.size();

        pass("All 108 cards are unique in HashSet (equals/hashCode correct)");
       }

        /** Draw all 108 cards from a full deck via ShuffledDeck. */
    static void testFullDeckIteration() {
        UnoDeck deck = new UnoDeck();
        var shuffled = new ShuffledDeck<>(deck, new java.util.Random(42));

        int count = 0;
        for (UnoCard c : shuffled) {
            assert c != null : "null card";
            assert c.getOrdinal() >= 0 && c.getOrdinal() < 108 : "ordinal out of range: " + c.getOrdinal();
            count++;
            if (count == 108) break; // deck has exactly 108 cards
          }
        assert count == 108 : "Expected 108 cards, got " + count;

        pass("Full deck iteration (108 cards)");
       }

        /** JSON serialise/deserialise round-trip for representative card types. */
    static void testJsonSerialisation() {
        UnoCardJsonSerialiser serialiser = new UnoCardJsonSerialiser();
        UnoDeck deck = new UnoDeck();

        int[] testOrdinals = {0, 1, 2, 15, 16, 19, 20, 99, 100, 104, 107};
        for (int ordinal : testOrdinals) {
            UnoCard original = deck.getCardFromOrdinal(ordinal);
            var json = serialiser.serialise(original);
            logger.debug("Serialised ordinal {} → {}", ordinal, json);

            UnoCard deserialised = serialiser.deserialise(json);
            assert deserialised.equals(original) :
                   "Deserialise mismatch for ordinal " + ordinal +
                   " original=" + original + " got=" + deserialised;
            assert deserialised.getOrdinal() == ordinal :
                   "Ordinal mismatch: expected " + ordinal + " got " + deserialised.getOrdinal();
          }

        pass("JSON serialise/deserialise round-trip");
       }

        /** Constructor must reject invalid card combinations. */
    static void testConstructorValidation() {
           // WILD with non-null rank → should throw
        boolean threw1 = false;
        try {
            new UnoCard(UnoCard.Colour.WILD, UnoCard.Rank.ZERO, 100);
          } catch (IllegalArgumentException e) {
            threw1 = true;
          }
        assert threw1 : "WILD + non-null rank should throw";

           // WILD_DRAW_FOUR with non-null rank → should throw
        boolean threw2 = false;
        try {
            new UnoCard(UnoCard.Colour.WILD_DRAW_FOUR, UnoCard.Rank.ONE, 104);
          } catch (IllegalArgumentException e) {
            threw2 = true;
          }
        assert threw2 : "WILD_DRAW_FOUR + non-null rank should throw";

           // Non-wild colour with null rank → should throw
        boolean threw3 = false;
        try {
            new UnoCard(UnoCard.Colour.RED, null, 0);
          } catch (IllegalArgumentException e) {
            threw3 = true;
          }
        assert threw3 : "Non-wild colour + null rank should throw";

           // Out of range ordinal → should throw
        boolean threw4 = false;
        try {
            new UnoCard(UnoCard.Colour.RED, UnoCard.Rank.ZERO, 108);
          } catch (IllegalArgumentException e) {
            threw4 = true;
          }
        assert threw4 : "Ordinal >= 108 should throw";

           // Valid constructions should NOT throw
        new UnoCard(UnoCard.Colour.WILD, null, 100);
        new UnoCard(UnoCard.Colour.WILD_DRAW_FOUR, null, 104);
        new UnoCard(UnoCard.Colour.RED, UnoCard.Rank.EIGHT, 16);

        pass("Constructor validation");
       }

        // --- helpers ---
    private static void pass(String testName) {
        passed++;
        logger.info("PASS: {}", testName);
        System.out.println("PASS: " + testName);
    }

}
