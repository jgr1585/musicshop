package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.MediaService;
import at.fhv.teamd.musicshop.backend.application.services.PlaylistService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedMediaException;
import io.swagger.v3.oas.annotations.Operation;
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

    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    private final PlaylistService playlistService = ServiceFactory.getPlaylistServiceInstance();
    private final MediaService mediaService = ServiceFactory.getMediaServiceInstance();

    @GET
    @Path("/download/album/{albumId}")
    @Operation(summary = "Retrieve the album download which comes as zipped archive")
    @ApiResponse(responseCode = "200", description = "Returns binary zip of album")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response downloadAlbum(@PathParam("albumId") int albumId) {
        if (authenticatedUser == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            List<AlbumDTO> playlist = playlistService.getPlaylist(authenticatedUser.name());
            ByteArrayOutputStream baos = mediaService.getPlaylistAlbumBinaryStream(playlist, albumId);

            return Response.ok(baos.toByteArray())
                    .type("application/zip")
                    .header("Content-Disposition", "attachment; filename=" + mediaService.getAlbumDownloadFilename(playlist, albumId))
                    .build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException | CustomerNotFoundException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }

    @GET
    @Path("/download/song/{songId}")
    @Operation(summary = "Retrieve the song download which comes as audio filed")
    @ApiResponse(responseCode = "200", description = "Returns binary song file")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response downloadSong(@PathParam("songId") int songId) {
        if (authenticatedUser == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            List<AlbumDTO> playlist = playlistService.getPlaylist(authenticatedUser.name());
            ByteArrayOutputStream baos = mediaService.getPlaylistSongBinaryStream(playlist, songId);

            return Response.ok(baos.toByteArray())
                    .type("application/mp3")
                    .header("Content-Disposition", "attachment; filename="+ mediaService.getSongDownloadFilename(playlist, songId))
                    .build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException | CustomerNotFoundException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }

    @GET
    @Path("/stream/song/{songId}")
    @Operation(summary = "Retrieve a song stream")
    @ApiResponse(responseCode = "200", description = "Returns binary song stream")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Response streamSong(@PathParam("songId") int songId) {
        if (authenticatedUser == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            List<AlbumDTO> playlist = playlistService.getPlaylist(authenticatedUser.name());
            ByteArrayOutputStream baos = mediaService.getPlaylistSongBinaryStream(playlist, songId);

            return Response.ok(baos.toByteArray())
                    .type("application/mp3")
                    .build();

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (UnauthorizedMediaException | CustomerNotFoundException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }
}
