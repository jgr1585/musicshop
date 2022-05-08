package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;

import javax.ws.rs.*;
import java.util.Set;

@Path("/article")
public class ArticleRestService {
    public ArticleRestService() {}

    @GET
    @Path("/search")
    @Produces("text/json")
    @Consumes("text/json")
    public Set<ArticleDTO> searchArticlesByAttributes(@QueryParam("title") String title, @QueryParam("artist") String artist) throws ApplicationClientException {
        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }
}
