package at.fhv.teamd.musicshop.backend.application.services.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import java.util.Set;

@Path("/article")
public class ArticleRestService {
    public ArticleRestService() {}

    @GET
    @Path("/search")
    @Produces("text/json")
    @Consumes("text/json")
    @Operation( summary = "Search Articles",
                description = "Search Articles by Attributes (Title, Artist)")
    @ApiResponse(responseCode = "200", description = "Articles found")
    @ApiResponse(responseCode = "200", description = "No Articles found")
    @ApiResponse(responseCode = "500", description = "No search params provided")
    public Set<ArticleDTO> searchArticlesByAttributes(@QueryParam("title") @DefaultValue("") String title, @QueryParam("artist") @DefaultValue("") String artist) throws ApplicationClientException {
        return ServiceFactory.getArticleServiceInstance().searchArticlesByAttributes(title, artist);
    }
}
