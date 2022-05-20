package at.fhv.teamd.musicshop.backend.application.services.rest.auth;

import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import javax.ws.rs.core.HttpHeaders;
import javax.annotation.Priority;
import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
@SecurityRequirement(name = "Authentication")
public class AuthenticationFilter implements ContainerResponseFilter {

    @Inject
    @AuthenticatedUser
    private Event<String> userAuthenticatedEvent;

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBased(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            validateToken(token);

            String username = Jwts.parserBuilder()
                    .setSigningKey(AuthRestService.KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            userAuthenticatedEvent.fire(username);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }

    private boolean isTokenBased(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(AuthRestService.KEY)
                .build()
                .parseClaimsJws(token);
    }

}
