package at.fhv.teamd.rest;

import at.fhv.teamd.application.Services.MediaService;
import at.fhv.teamd.application.Services.PlaylistService;
import at.fhv.teamd.application.Services.ServiceFactory;
import at.fhv.teamd.musicshop.library.dto.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedMediaException;
import at.fhv.teamd.rest.auth.AuthenticatedUser;
import at.fhv.teamd.rest.auth.Secured;
import at.fhv.teamd.rest.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    @AuthenticatedUser
    User authenticatedUser;

    @GET
    @Path("/download/album/{albumId}")
    @Operation(summary = "Retrieve the album download which comes as zipped archive")
    @ApiResponse(responseCode = "200", description = "Returns binary zip of album")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response downloadAlbum(@HeaderParam ("Authorization") String authorization, @PathParam("albumId") int albumId) {

        try {
            List<AlbumDTO> playlist = playlistService.getUserPlaylist(authenticatedUser.authToken());
            ByteArrayOutputStream baos = mediaService.getPlaylistAlbumBinaryStream(playlist, albumId);

            return Response.ok(baos.toByteArray())
                    .type("application/zip")
                    .header("Content-Disposition", "attachment; filename=" + mediaService.getAlbumDownloadFilename(playlist, albumId))
                    .build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }

    @GET
    @Path("/download/song/{songId}")
    @Operation(summary = "Retrieve the song download which comes as audio filed")
    @ApiResponse(responseCode = "200", description = "Returns binary song file")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response downloadSong(@PathParam("songId") int songId) {

        try {
            List<AlbumDTO> playlist = playlistService.getUserPlaylist(authenticatedUser.authToken());
            ByteArrayOutputStream baos = mediaService.getPlaylistSongBinaryStream(playlist, songId);

            return Response.ok(baos.toByteArray())
                    .type("application/mp3")
                    .header("Content-Disposition", "attachment; filename="+ mediaService.getSongDownloadFilename(playlist, songId))
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
