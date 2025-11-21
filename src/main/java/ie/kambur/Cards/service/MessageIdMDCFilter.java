package ie.kambur.Cards.service;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
@Provider
public class MessageIdMDCFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger logger = LoggerFactory.getLogger(MessageIdMDCFilter.class);

    @Override
    public void filter(ContainerRequestContext ctx) {
        MDC.put("messageId", ctx.getHeaderString("Message-Id"));
        logger.info ("Received request {} {}", ctx.getMethod(), ctx.getUriInfo().getPath());
    }

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext respCtx) {
        MDC.remove("messageId");
    }
}
