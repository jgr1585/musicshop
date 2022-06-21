package at.fhv.teamd.application.services;

import at.fhv.teamd.musicshop.library.dto.AlbumDTO;
import at.fhv.teamd.musicshop.library.dto.ArticleDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class PlaylistService {

    CloseableHttpClient httpClient = HttpClients.createDefault();
    public List<AlbumDTO> getUserPlaylist(String token) throws IOException {
        String backendUrl = ConfigProvider.getConfig().getOptionalValue("backend.url", String.class)
                .orElse("");

        HttpGet request = new HttpGet(backendUrl + "/rest/playlist/");
        request.addHeader("Authorization", "Bearer " + token);
        request.addHeader("content-type", "application/json");
        CloseableHttpResponse response = httpClient.execute(request);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.
                readValue(response.getEntity().getContent(),
                        objectMapper.getTypeFactory().
                                constructCollectionType(List.class, ArticleDTO.class));
    }
}
