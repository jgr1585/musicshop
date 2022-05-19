package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import java.util.Set;

@Path("/article")
@Produces("application/json")
@Consumes("application/json")
public class ArticleRestService {
    public ArticleRestService() {}

    @GET
    @Path("/search")
    @Operation( summary = "Search Articles",
                description = "Search Articles by Attributes (Title, Artist)")
    //@ApiResponse(responseCode = "200", description = "Articles found")
    //@ApiResponse(responseCode = "404", description = "No Articles found")
    public Set<ArticleDTO> searchArticlesByAttributes(@QueryParam("title") @DefaultValue("") String title, @QueryParam("artist") @DefaultValue("") String artist) throws ApplicationClientException {
        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }
}
