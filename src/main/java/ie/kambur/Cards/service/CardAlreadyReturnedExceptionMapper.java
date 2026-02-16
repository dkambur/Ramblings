package ie.kambur.Cards.service;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CardAlreadyReturnedExceptionMapper implements ExceptionMapper<CardAlreadyReturnedException> {
    private static final Logger logger = LoggerFactory.getLogger(CardAlreadyReturnedExceptionMapper.class);

    @Override
    public Response toResponse(CardAlreadyReturnedException exception) {
        logger.warn("Card already returned: {}", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"" + exception.getMessage() + "\"}")
                .build();
    }
}
