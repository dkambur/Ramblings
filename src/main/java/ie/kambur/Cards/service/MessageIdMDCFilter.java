package ie.kambur.Cards.service;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.MDC;
@Provider
public class MessageIdMDCFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext ctx) {
        MDC.put("messageId", ctx.getHeaderString("Message-Id"));
    }

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext respCtx) {
        MDC.remove("messageId");
    }
}
