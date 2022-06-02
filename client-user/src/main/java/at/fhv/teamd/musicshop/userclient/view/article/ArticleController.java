package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.dto.*;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.view.GenericArticleController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
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

    private SearchController searchController;

    // select cover art for album
    private final Consumer<ArticleDTO> loadCoverArtConsumer = articleDTO -> {
        try {
            this.loadCoverArt(articleDTO);
        } catch (IOException ignored) {
            //Nothing to do
        }
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
                controller.setMediumType(mediumDTO);
                this.mediumTypeList.getChildren().add(root);
            }
        } else if (articleDTO instanceof SongDTO && this.searchController != null) {
            this.mediumTypeList.getChildren().add(this.createFindAlbumButton((SongDTO) articleDTO));
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

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
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

    private Button createFindAlbumButton(SongDTO songDTO) {
        FontAwesomeIconView findIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH, "18");
        Button findButton = new Button("", findIcon);

        VBox.setMargin(findButton, new Insets(0, 75, 0, 0));
        findButton.setMinWidth(36);
        findButton.setMaxWidth(36);
        findButton.setMinHeight(26);
        findButton.setMaxHeight(26);
        findButton.setStyle("-fx-background-color: #547af9;");

        findButton.setOnAction(event -> this.searchController.searchAlbum(songDTO));

        return findButton;
    }
}
