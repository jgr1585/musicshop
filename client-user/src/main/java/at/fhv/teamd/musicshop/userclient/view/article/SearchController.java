package at.fhv.teamd.musicshop.userclient.view.article;

import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.DTO.SongDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.library.exceptions.NotAuthorizedException;
import at.fhv.teamd.musicshop.userclient.Tabs;
import at.fhv.teamd.musicshop.userclient.communication.RemoteFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SearchController {

    @FXML
    private TextField searchByTitle;
    @FXML
    private TextField searchByArtist;
    @FXML
    private VBox searchPane;

    public void searchAlbum(SongDTO song) {
        this.searchPane.getChildren().clear();

        this.searchByTitle.setText(song.title());
        this.searchByArtist.setText(song.artists().iterator().next().name());

        try {
            this.insertResults(new HashSet<>(song.albums()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertResults(Set<ArticleDTO> results) throws IOException {
        for (var article : results) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/templates/article.fxml"));
            Parent medium = fxmlLoader.load();
            ArticleController controller = fxmlLoader.getController();
            controller.setSearchController(this);
            controller.addMediumTypes(article, Tabs.SEARCH);
            this.searchPane.getChildren().add(medium);
        }
    }

    @FXML
    private void searchArticles(ActionEvent actionEvent) throws ApplicationClientException, IOException, NotAuthorizedException {
        this.searchPane.getChildren().clear();
        if (!(this.searchByTitle.getText().isEmpty() && this.searchByArtist.getText().isEmpty())) {
            Set<ArticleDTO> result = RemoteFacade.getInstance().searchArticlesByAttributes(this.searchByTitle.getText(), this.searchByArtist.getText());
            if (!(result.isEmpty())) {
                this.insertResults(result);
            } else {
                new Alert(Alert.AlertType.NONE, "No articles found", ButtonType.CLOSE).show();
            }
        } else {
            new Alert(Alert.AlertType.NONE, "No parameter for search provided", ButtonType.CLOSE).show();
        }
    }

    @FXML
    private void resetSearch(ActionEvent actionEvent) {
        this.searchPane.getChildren().clear();
        this.searchByTitle.setText("");
        this.searchByArtist.setText("");
    }

}