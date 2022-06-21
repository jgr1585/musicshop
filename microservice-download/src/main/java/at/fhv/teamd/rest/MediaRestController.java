package at.fhv.teamd.rest;

import at.fhv.teamd.application.services.MediaService;
import at.fhv.teamd.application.services.PlaylistService;
import at.fhv.teamd.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.dto.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedMediaException;
import at.fhv.teamd.rest.auth.AuthenticatedUser;
import at.fhv.teamd.rest.auth.Secured;
import at.fhv.teamd.rest.auth.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.*;
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
    @Produces("application/zip")
    @Operation(summary = "Retrieve the album download which comes as zipped archive")
    @ApiResponse(responseCode = "200", description = "Returns binary zip of album", useReturnTypeSchema = true, content = @Content(mediaType = "application/zip", schema = @Schema(type = "string", format = "binary")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Server Error")
    public Response downloadAlbum(@PathParam("albumId") int albumId) {

        try {
            List<AlbumDTO> playlist = playlistService.getUserPlaylist(authenticatedUser.authToken());
            ByteArrayOutputStream baos = mediaService.getPlaylistAlbumBinaryStream(playlist, albumId);

            return Response.ok(baos.toByteArray()).type("application/zip").header("Content-Disposition", "attachment; filename=" + mediaService.getAlbumDownloadFilename(playlist, albumId)).build();

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
    @Produces("application/mp3")
    @Operation(summary = "Retrieve the song download which comes as audio filed")
    @ApiResponse(responseCode = "200", description = "Returns binary song file", useReturnTypeSchema = true, content = @Content(mediaType = "application/mp3", schema = @Schema(type = "string", format = "binary")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Server Error")
    public Response downloadSong(@PathParam("songId") int songId) {

        try {
            List<AlbumDTO> playlist = playlistService.getUserPlaylist(authenticatedUser.authToken());
            ByteArrayOutputStream baos = mediaService.getPlaylistSongBinaryStream(playlist, songId);

            return Response.ok(baos.toByteArray()).type("application/mp3").header("Content-Disposition", "attachment; filename=" + mediaService.getSongDownloadFilename(playlist, songId)).build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }
}
