package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.PlaylistService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Secured
@Path("/playlist")
@Consumes("application/json")
@Produces("application/json")
@SecurityRequirement(name = "Authentication")
@NoArgsConstructor
public class PlaylistRestController {

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    private final PlaylistService playlistService = ServiceFactory.getPlaylistServiceInstance();

    @GET
    @Operation(summary = "Retrieve the user playlist which contains all bought mediums")
    @ApiResponse(responseCode = "200", description = "Returns List<AlbumDTO>")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response getUserPlaylist() {
        if (authenticatedUser == null) {
            return Response.status(401).build();
        }

        try {
            return Response.ok(playlistService.getPlaylist(authenticatedUser.name())).build();
        } catch (CustomerNotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }
}
