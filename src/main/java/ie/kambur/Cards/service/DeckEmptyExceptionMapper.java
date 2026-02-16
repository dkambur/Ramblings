package ie.kambur.Cards.service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class DeckEmptyExceptionMapper implements ExceptionMapper<DeckEmptyException> {
    private static final Logger logger = LoggerFactory.getLogger(DeckEmptyExceptionMapper.class);

    @Override
    public Response toResponse(DeckEmptyException exception) {
        logger.warn("Deck exhausted: {}", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"" + exception.getMessage() + "\"}")
                .build();
    }
}
