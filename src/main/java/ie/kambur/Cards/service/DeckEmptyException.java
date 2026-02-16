package ie.kambur.Cards.service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class DeckEmptyException extends WebApplicationException {
    public DeckEmptyException(String message) {
        super(Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"" + message + "\"}")
                .build());
    }
}
