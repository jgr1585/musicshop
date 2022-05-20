package at.fhv.teamd.musicshop.backend.application.services.rest.auth;

import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Path("/authentication")
public class AuthRestService {


    public AuthRestService() {
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successed"),
            @ApiResponse(responseCode = "403", description = "Wrong credentials")
    })

    public Response authenticateUser(Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            authenticate(username, password);

            String token = issueToken(username);

            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private void authenticate(String username, String password) {

    }

    private String issueToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuer()
                .setExpiration(Date.from(LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.UTC)))
                .signWith()
                .compact();
    }
}
