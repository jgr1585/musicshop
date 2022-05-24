package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.backend.domain.article.Album;
import at.fhv.teamd.musicshop.backend.domain.article.Song;
import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.SongDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet("/media/*")
public class MediaController extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Check if user is logged in and has permission to access this resource
        if (req.getPathInfo().equals("/songs")) {
            int songId = Integer.parseInt(req.getParameter("id"));

            ServletOutputStream out = resp.getOutputStream();
            InputStream in = getClass().getResourceAsStream("/WEB-INF/songs/" + songId + ".mp3");
            assert in != null;
            in.transferTo(out);

            in.close();
            out.close();
        } else if (req.getPathInfo().equals("/albums")) {
            long albumId = Integer.parseInt(req.getParameter("id"));
            AlbumDTO album = null;
            try {
                album = ServiceFactory.getArticleServiceInstance().getAlbumById(albumId).orElse(null);
            } catch (ApplicationClientException e) {
                e.printStackTrace();
            }

            if (album != null) {
                Set<Long> songIds = album.songs().stream().map(SongDTO::id).collect(Collectors.toSet());

                ServletOutputStream out = resp.getOutputStream();
                ZipOutputStream zip = new ZipOutputStream(out);

                songIds.forEach(songId -> {
                    try {
                        FileInputStream in = new FileInputStream("/WEB-INF/songs/" + songId + ".mp3");
                        zip.putNextEntry(new ZipEntry(songId + ".mp3"));

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            zip.write(buffer, 0, length);
                        }
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                zip.close();
                out.close();
            } else {
                resp.sendError(404);
            }
        }
    }
}
