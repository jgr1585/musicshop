package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.DTO.*;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ArticleController {

    @FXML
    private ImageView cover;
    @FXML
    private Label title;
    @FXML
    private Label artist;
    @FXML
    private Label genre;
    @FXML
    private VBox mediumTypeList;
    @FXML
    private Label releaseDate;

    // select cover art for album
    private final Consumer<ArticleDTO> loadCoverArtConsumer = articleDTO -> {
        try {
            this.loadCoverArt(articleDTO);
        } catch (IOException ignored) {}
    };

    public void addMediumTypes(ArticleDTO articleDTO, Tabs tab) throws IOException {
        this.title.setText(articleDTO.title());
        this.genre.setText(articleDTO.genre());
        this.artist.setText(articleDTO.artists().stream().map(ArtistDTO::name).collect(Collectors.joining(", ")));
        this.releaseDate.setText(articleDTO.releaseDate().toString());

        new Thread(() -> loadCoverArtConsumer.accept(articleDTO)).start();

        if (articleDTO instanceof AlbumDTO) {
            for (var mediumDTO : ((AlbumDTO) articleDTO).mediums()) {
                FXMLLoader fxmlLoader;
                switch (tab) {
                    case SEARCH:
                        fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/searchArticle/search-article.fxml"));
                        break;
                    case RETURN:
                        fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/returnArticle/return-article.fxml"));
                        break;
                    case SHOPPINGCART:
                        fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/shoppingCart/shoppingCart-article.fxml"));
                        break;
                    default:
                        throw new RuntimeException("invalid tab-parameter for article");
                }

                Parent root = fxmlLoader.load();
                GenericArticleController controller = fxmlLoader.getController();
                controller.setMediumType(articleDTO, mediumDTO);
                this.mediumTypeList.getChildren().add(root);
            }
        }
    }

    public void addMediumTypes(LineItemDTO lineItemDTO, Tabs tab) throws IOException {
        ArticleDTO articleDTO = lineItemDTO.article();

        this.title.setText(articleDTO.title());
        this.genre.setText(articleDTO.genre());
        this.artist.setText(articleDTO.artists().stream().map(ArtistDTO::name).collect(Collectors.joining(", ")));
        this.releaseDate.setText(articleDTO.releaseDate().toString());

        new Thread(() -> loadCoverArtConsumer.accept(articleDTO)).start();

        FXMLLoader fxmlLoader;
        switch (tab) {
            case SEARCH:
                fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/searchArticle/search-article.fxml"));
                break;
            case RETURN:
                fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/returnArticle/return-article.fxml"));
                break;
            case SHOPPINGCART:
                fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/shoppingCart/shoppingCart-article.fxml"));
                break;
            default:
                throw new RuntimeException("invalid tab-parameter for article");
        }

        Parent root = fxmlLoader.load();
        GenericArticleController controller = fxmlLoader.getController();
        controller.setMediumType(lineItemDTO);
        this.mediumTypeList.getChildren().add(root);
    }

    private void loadCoverArt(ArticleDTO articleDTO) throws IOException {
        if (articleDTO instanceof AlbumDTO) {
            AlbumDTO albumDTO = (AlbumDTO) articleDTO;

            String url = getImageURL("http://coverartarchive.org/release/" + albumDTO.musicbrainzId() + "/front");

            Image coverArt = new Image(url);
            if (coverArt.getProgress() == 1 && !coverArt.isError()) {
                this.cover.setImage(coverArt);
            }
        } else if (articleDTO instanceof SongDTO) {
            this.cover.setImage(new Image("/at/fhv/teamd/musicshop/userclient/Song-Cover.png"));
        }
    }

    private String getImageURL(String url) throws IOException {
        return this.getImageURL(url, 0);
    }

    private String getImageURL(String url, int depth) throws IOException {
        if (depth > 25) {
            throw new FileNotFoundException();
        }

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.connect();
        int responseCode = con.getResponseCode();
        String location = con.getHeaderField("Location");

        switch (responseCode) {
            case 200:
                return url;
            case 404:
            case 405:
                throw new FileNotFoundException();
            default:
                return this.getImageURL(location, depth + 1);
        }
    }
}
