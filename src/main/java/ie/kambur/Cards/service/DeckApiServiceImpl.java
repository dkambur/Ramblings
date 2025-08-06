package ie.kambur.Cards.service;
import ie.kambur.Cards.ShuffledDeck;
import ie.kambur.Cards.generated.*;
import ie.kambur.Cards.generated.model.*;


import ie.kambur.Cards.generated.model.CreateDeckRequest;
import ie.kambur.Cards.generated.model.DeckState;
import ie.kambur.Cards.generated.model.DrawCard200Response;
import ie.kambur.Cards.generated.model.DrawCardRequest;
import ie.kambur.Cards.generated.model.Error;
import ie.kambur.Cards.generated.model.ReturnCard200Response;
import ie.kambur.Cards.generated.model.ReturnCardRequest;

import java.util.List;

import java.io.InputStream;

import ie.kambur.Cards.std.StandardDeck;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
public class DeckApiServiceImpl implements DeckApi {

    @Override
    public DeckState createDeck(CreateDeckRequest createDeckRequest) {
        //createDeckRequest.getDeckType()  new ShuffledDeck (new StandardDeck()), 1  );
        //    }
        return new DeckState("abcd", "aa");
    }

    @Override
    public DrawCard200Response drawCard(DrawCardRequest drawCardRequest) {
        return null;
    }

    @Override
    public ReturnCard200Response returnCard(ReturnCardRequest returnCardRequest) {
        return null;
    }
}