package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.ArticleService;
import at.fhv.teamd.musicshop.backend.application.services.InvoiceService;
import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.backend.rest.auth.AuthenticatedUser;
import at.fhv.teamd.musicshop.backend.rest.auth.Secured;
import at.fhv.teamd.musicshop.backend.rest.auth.User;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.SongDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.InvoiceException;
import at.fhv.teamd.musicshop.library.exceptions.UnauthorizedInvoiceException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.*;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet("/media/*")
@Secured
@SecurityRequirement(name = "Authentication")
public class MediaController extends HttpServlet {
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;

    private InvoiceService invoiceService = ServiceFactory.getInvoiceServiceInstance();
    private ArticleService articleService = ServiceFactory.getArticleServiceInstance();

    private void serveSong(int invoiceId, int songId, HttpServletResponse resp) throws IOException, InvoiceException, UnauthorizedInvoiceException {
        // check if is allowed to access
        SongDTO songDTO = invoiceService.getSongDTO(authenticatedUser.name(), invoiceId, songId);

        resp.setContentType("application/mp3");
        resp.addHeader("Content-Disposition", "attachment; filename="+ songDTO.title() + ".mp3");

        ServletOutputStream out = resp.getOutputStream();
        FileInputStream in = new FileInputStream("/WEB-INF/songs/" + songId + ".mp3");
        in.transferTo(out);

        in.close();
        out.close();
    }

    private void serveAlbum(int invoiceId, int albumId, HttpServletResponse resp) throws ApplicationClientException, IOException, InvoiceException, UnauthorizedInvoiceException {
        // check if is allowed to access
        AlbumDTO album = invoiceService.getAlbumDTO(authenticatedUser.name(), invoiceId, albumId);

        ServletOutputStream out = resp.getOutputStream();
        ZipOutputStream zip = new ZipOutputStream(out);

        resp.setContentType("application/zip");
        resp.addHeader("Content-Disposition", "attachment; filename="+ album.title() + ".zip");

        for (SongDTO song : album.songs()) {
            try {
                InputStream fis = getClass().getResourceAsStream("/WEB-INF/songs/" + song.id() + ".mp3");
                zip.putNextEntry(new ZipEntry(song.title() + ".mp3"));

                byte[] buffer = new byte[1024];
                int length;
                assert fis != null;
                while ((length = fis.read(buffer)) > 0) {
                    zip.write(buffer, 0, length);
                }

                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        zip.close();
        out.close();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int invoiceId = Integer.parseInt(req.getParameter("invoiceId"));

            if (req.getPathInfo().equals("/song")) {
                int songId = Integer.parseInt(req.getParameter("id"));

                serveSong(invoiceId, songId, resp);
            } else if (req.getPathInfo().equals("/album")) {
                int albumId = Integer.parseInt(req.getParameter("id"));

                serveAlbum(invoiceId, albumId, resp);
            }
        } catch (NumberFormatException | ApplicationClientException e) {
            resp.sendError(400, "Invalid id.");
        } catch (FileNotFoundException e) {
            resp.sendError(404, "Requested file not found.");
        } catch (IOException e) {
            resp.sendError(500);
        } catch (InvoiceException e) {
            resp.sendError(404, "Invoice not found.");
        } catch (UnauthorizedInvoiceException e) {
            resp.sendError(401, "Unauthorized.");
        }
    }
}
