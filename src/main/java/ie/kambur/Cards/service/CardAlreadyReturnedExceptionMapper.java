package ie.kambur.Cards.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(java.util.Map.of("error", exception.getMessage()));
            return Response.status(Response.Status.CONFLICT).entity(body).build();
        } catch (Exception e) {
            logger.error("Failed to serialise error response", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal server error\"}")
                    .build();
        }
    }
}
