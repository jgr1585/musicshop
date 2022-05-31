package at.fhv.teamd.musicshop.backend.rest.forms;

import at.fhv.teamd.musicshop.backend.application.services.ArticleService;
import at.fhv.teamd.musicshop.backend.application.services.InvoiceService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.SongDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedInvoiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Secured
@Path("/media")
@SecurityRequirement(name = "Authentication")
public class MediaRestController {
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    private final InvoiceService invoiceService = ServiceFactory.getInvoiceServiceInstance();
    private final ArticleService articleService = ServiceFactory.getArticleServiceInstance();

    public MediaRestController() {}

    @GET
    @Operation(summary = "Download song")
    @ApiResponse(responseCode = "200", description = "Returns binary song file")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Path("/song")
    public Response downloadSong(@QueryParam("invoiceId") int invoiceId, @QueryParam("id") int songId) {
        if (authenticatedUser == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            return serveSong(invoiceId, songId);

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (InvoiceException e) {
            return Response.status(404, "Invoice not found.").build();
        } catch (UnauthorizedInvoiceException | CustomerNotFoundException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }

    @GET
    @Operation(summary = "Download album")
    @ApiResponse(responseCode = "200", description = "Returns binary zip of album")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Path("/album")
    public Response downloadAlbum(@QueryParam("invoiceId") int invoiceId, @QueryParam("id") int albumId) {
        if (authenticatedUser == null) {
            return Response.status(401, "Not authenticated.").build();
        }

        try {
            return serveAlbum(invoiceId, albumId);

        } catch (FileNotFoundException e) {
            return Response.status(404, "Requested file not found.").build();
        } catch (IOException e) {
            return Response.status(500).build();
        } catch (InvoiceException e) {
            return Response.status(404, "Invoice not found.").build();
        } catch (UnauthorizedInvoiceException | CustomerNotFoundException e) {
            return Response.status(401, "Unauthorized.").build();
        }
    }

    private Response serveSong(int invoiceId, int songId) throws IOException, InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        // check if is allowed to access
        SongDTO song = invoiceService.getSongDTO(authenticatedUser.name(), invoiceId, songId);

        InputStream in = getClass().getClassLoader().getResourceAsStream("/WEB-INF/songs/" + song.uuid() + ".mp3");

        assert in != null;
        Response resp = Response.ok(in.readAllBytes())
                .type("application/mp3")
                .header("Content-Disposition", "attachment; filename="+ song.title() + ".mp3")
                .build();
        in.close();

        return resp;
    }

    private Response serveAlbum(int invoiceId, int albumId) throws IOException, InvoiceException, UnauthorizedInvoiceException, CustomerNotFoundException {
        // check if is allowed to access
        AlbumDTO album = invoiceService.getAlbumDTO(authenticatedUser.name(), invoiceId, albumId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(baos);

        for (SongDTO song : album.songs()) {
            InputStream fis = getClass().getClassLoader().getResourceAsStream("/WEB-INF/songs/" + song.uuid() + ".mp3");
            zip.putNextEntry(new ZipEntry(song.title() + ".mp3"));

            byte[] buffer = new byte[1024];
            int length;
            assert fis != null;
            while ((length = fis.read(buffer)) > 0) {
                zip.write(buffer, 0, length);
            }

            fis.close();
        }

        zip.close();

        return Response.ok(baos.toByteArray())
                .type("application/zip")
                .header("Content-Disposition", "attachment; filename="+ album.title() + ".zip")
                .build();
    }
}
