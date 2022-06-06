package at.fhv.teamd.musicshop.backend.rest;

import at.fhv.teamd.musicshop.backend.application.services.ServiceFactory;
import at.fhv.teamd.musicshop.library.dto.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/article")
@Produces("application/json")
@Consumes("application/json")
public class ArticleRestController {
    public ArticleRestController() {}

    @GET
    @Path("/search")
    @Operation( summary = "Search Articles",
                description = "Search Articles by Attributes (Title, Artist)")
    @ApiResponse(responseCode = "200", description = "Articles found")
    @ApiResponse(responseCode = "204", description = "No Articles found")
    @ApiResponse(responseCode = "400", description = "No search params provided")
    public Response searchArticlesByAttributes(@QueryParam("title") @DefaultValue("") String title, @QueryParam("artist") @DefaultValue("") String artist) throws ApplicationClientException {
        if (title.equals("") && artist.equals("")) {
            return Response.status(400).build();
        }
        Set<ArticleDTO> articles = ServiceFactory.getArticleServiceInstance().searchDigitalMediumArticles(title, artist);
        if (!articles.isEmpty()) {
            return Response.ok().entity(articles).build();
        } else {
            return Response.status(204).build();
        }
    }
}
