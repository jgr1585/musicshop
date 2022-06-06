package at.fhv.teamd.application;

import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.exceptions.CustomerNotFoundException;
import at.fhv.teamd.rest.auth.AuthenticatedUser;
import at.fhv.teamd.rest.auth.Secured;
import at.fhv.teamd.rest.auth.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Stateless
@NoArgsConstructor
public class PlaylistService {

    @Inject
    @AuthenticatedUser
    private User token;

    CloseableHttpClient httpClient = HttpClients.createDefault();

    public List<AlbumDTO> getUserPlaylist() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/backend-1.0-SNAPSHOT/rest/playlist/");
        request.addHeader("Authorization", "Bearer: " + token);
        request.addHeader("content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.
                readValue(response.getEntity().getContent(),
                        objectMapper.getTypeFactory().
                                constructCollectionType(List.class, AlbumDTO.class));
    }
}
