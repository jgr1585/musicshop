package at.fhv.teamd.playlist.rest;

import at.fhv.teamd.playlist.application.services.MediaService;
import at.fhv.teamd.playlist.application.services.PlaylistService;
import at.fhv.teamd.playlist.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.dto.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedMediaException;
import at.fhv.teamd.playlist.rest.auth.Secured;
import at.fhv.teamd.playlist.rest.auth.Tokenholder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Secured
@Path("/media")
@SecurityRequirement(name = "Authentication")
@NoArgsConstructor
public class MediaRestController {

    private final MediaService mediaService = ServiceFactory.getMediaServiceInstance();
    private final PlaylistService playlistService = ServiceFactory.getPlaylistServiceInstance();

    @Inject
    Tokenholder tokenholder;

    @GET
    @Path("/stream/song/{songId}")
    @Produces("application/mp3")
    @Operation(summary = "Retrieve a song stream")
    @ApiResponse(responseCode = "200", description = "Returns binary song stream", useReturnTypeSchema = true, content = @Content(mediaType = "application/mp3", schema = @Schema(type = "string", format = "binary")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Server Error")
    public Response streamSong(@PathParam("songId") int songId) {
        if (tokenholder == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            List<AlbumDTO> playlist = playlistService.getUserPlaylist(tokenholder.authToken());
            ByteArrayOutputStream baos = mediaService.getPlaylistSongBinaryStream(playlist, songId);

            return Response.ok(baos.toByteArray())
                    .type("application/mp3")
                    .build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }
}
