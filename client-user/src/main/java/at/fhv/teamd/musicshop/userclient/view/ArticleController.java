package at.fhv.teamd.musicshop.userclient.view;

import at.fhv.teamd.musicshop.library.DTO.AlbumDTO;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.ArtistDTO;
import at.fhv.teamd.musicshop.library.DTO.LineItemDTO;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.view.generic.GenericArticleController;
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
import java.util.Optional;
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

    public void addMediumTypes(ArticleDTO articleDTO,  Tabs tabs) {
        this.addMediumTypes(articleDTO, Optional.empty(), tabs);
    }

    public void addMediumTypes(ArticleDTO articleDTO, Optional<LineItemDTO> lineItemDTO, Tabs tab) {

        this.title.setText(articleDTO.title());
        this.genre.setText(articleDTO.genre());
        this.artist.setText(articleDTO.artists().stream().map(ArtistDTO::name).collect(Collectors.joining(", ")));

        //Select Cover Art for Album
        new Thread(() -> this.loadCoverArt(articleDTO)).start();

        articleDTO.mediums().forEach(mediumDTO -> {

            FXMLLoader fxmlLoader = switch (tab) {
                case SEARCH -> new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/searchArticle/search-article.fxml"));
                case RETURN -> new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/returnArticle/return-article.fxml"));
                case SHOPPINGCART -> new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/shoppingCart/shoppingCart-article.fxml"));
            };

            try {
                Parent root = fxmlLoader.load();
                GenericArticleController controller = fxmlLoader.getController();
                controller.setMediumType(articleDTO, mediumDTO, lineItemDTO);
                this.mediumTypeList.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCoverArt(ArticleDTO articleDTO) {
        if (articleDTO instanceof AlbumDTO albumDTO) {

            try {
                System.out.println("looking for coverid: " + albumDTO.musicbrainzId());
                String url = getImageURL("http://coverartarchive.org/release/" + albumDTO.musicbrainzId() + "/front");

                Image coverArt = new Image(url);
                if (coverArt.getProgress() == 1 && !coverArt.isError()) {
                    this.cover.setImage(coverArt);
                    System.out.println("coverid: " + albumDTO.musicbrainzId() + " found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        String location = con.getHeaderField( "Location" );

        return switch (responseCode) {
            case 200 -> url;
            case 404, 405 -> throw new FileNotFoundException();
            default -> this.getImageURL(location, depth + 1);
        };

    }

}
