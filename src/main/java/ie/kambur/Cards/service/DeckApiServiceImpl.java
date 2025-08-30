package ie.kambur.Cards.service;
import ie.kambur.Cards.DeckSingletonRegistry;
import ie.kambur.Cards.ShuffledDeck;
import ie.kambur.Cards.core.interfaces.Card;
import ie.kambur.Cards.generated.*;


import ie.kambur.Cards.generated.model.CreateDeckRequest;
import ie.kambur.Cards.generated.model.DeckState;
import ie.kambur.Cards.generated.model.DrawCard200Response;
import ie.kambur.Cards.generated.model.ReturnCard200Response;
import ie.kambur.Cards.generated.model.ReturnCardRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Random;

import ie.kambur.Cards.service.std.SuffledDeckJsonSerialiser;
import ie.kambur.Cards.service.std.StandardCardJsonSerialiser;
import jakarta.enterprise.context.RequestScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class DeckApiServiceImpl implements DeckApi {

    protected static final Logger logger = LogManager.getLogger(DeckApiServiceImpl.class);

    // TODO: On a stupid place - move elsewhere
    static {
        CardSerializerRegistry.register(new StandardCardJsonSerialiser());
    }

    @Override
    public DeckState createDeck(CreateDeckRequest createDeckRequest) {
        final String deckType = createDeckRequest.getDeckType().toString();

        try {
            var randomisedDeck = new ShuffledDeck<> (DeckSingletonRegistry.getInstance().getDeck(deckType), new Random());

            byte[] serialisedDeck = SuffledDeckJsonSerialiser.serialise(randomisedDeck);

            // TODO: This is shit.
            return new DeckState(Base64.getEncoder().encodeToString(serialisedDeck), deckType);
        } catch (IOException e) {
            throw new IllegalStateException("Deck creation failed", e);
        }
    }

    @Override
    public DrawCard200Response drawCard(DeckState deck) {
        try {
            final String deckType = deck.getDeckType();

            byte[] decodedState = Base64.getDecoder().decode(deck.getDeckState());
            ShuffledDeck<? extends Card, ?> theDeck = new ShuffledDeck<>();
            theDeck.readExternal(new ObjectInputStream(new ByteArrayInputStream(decodedState)));

            Card c = theDeck.iterator().next();

            DrawCard200Response theResponse = new DrawCard200Response();

            theResponse.deck(new DeckState(Base64.getEncoder().encodeToString(SuffledDeckJsonSerialiser.serialise(theDeck)), deckType));
            theResponse.card(CardSerializerRegistry.getSerializer(c).serialise(c));
            return theResponse;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Card draw failed", e);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ReturnCard200Response returnCard(ReturnCardRequest returnCardRequest) {

        try {
            final String deckType = returnCardRequest.getDeck().getDeckType();

            byte[] decodedState = Base64.getDecoder().decode( returnCardRequest.getDeck().getDeckState());
            ShuffledDeck<? extends Card, ?> theDeck = new ShuffledDeck<>();
            theDeck.readExternal(new ObjectInputStream(new ByteArrayInputStream(decodedState)));

            // FIXME: Hack below - need to rethink how to organise generic code
            Card returnedCard = (CardSerializerRegistry.getSerializer(theDeck.getTheDeck().getCardFromOrdinal(0)).deserialise(returnCardRequest.getCard()));
            // TODO: Below is to fix type generics issues
            ((ShuffledDeck) theDeck).returnCard(returnedCard);

            ReturnCard200Response theResponse = new ReturnCard200Response();

            theResponse.deck(new DeckState(Base64.getEncoder().encodeToString(SuffledDeckJsonSerialiser.serialise(theDeck)), deckType));
            return theResponse;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Card draw failed", e);
        }
    }
}