package ie.kambur.Cards.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class Health {
    @GET
    public Response health() {
        return Response.ok("UP").build();
    }
}