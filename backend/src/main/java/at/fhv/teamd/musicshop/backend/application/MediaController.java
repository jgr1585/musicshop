package at.fhv.teamd.musicshop.backend.application;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/media/*")
public class MediaController extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo().equals("/songs")) {
            int songId = Integer.parseInt(req.getParameter("id"));

            ServletOutputStream out = resp.getOutputStream();
            InputStream in = getClass().getResourceAsStream("/WEB-INF/songs/" + songId + ".mp3");
            assert in != null;
            in.transferTo(out);

            in.close();
            out.close();
        }
    }
}
