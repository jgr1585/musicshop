package at.fhv.teamd.musicshop.userclient.view.searchArticle;

import at.fhv.teamd.musicshop.library.ApplicationClient;
import at.fhv.teamd.musicshop.library.DTO.ArticleDTO;
import at.fhv.teamd.musicshop.library.exceptions.ApplicationClientException;
import at.fhv.teamd.musicshop.userclient.view.ArticleController;
import at.fhv.teamd.musicshop.userclient.connection.RMIconnection;
import at.fhv.teamd.musicshop.userclient.Tabs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class SearchController {

    @FXML
    private TextField searchByTitle;
    @FXML
    private TextField searchByArtist;
    @FXML
    private MenuButton searchByMedium;

    @FXML
    private VBox searchPane;

    public void insertResults(List<ArticleDTO> results) {
        results.forEach(article -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/fhv/teamd/musicshop/userclient/view/article.fxml"));
                Parent medium = fxmlLoader.load();
                ArticleController controller = fxmlLoader.getController();
                controller.addMediumTypes(article, Tabs.SEARCH);
                this.searchPane.getChildren().add(medium);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void searchArticle(ActionEvent actionEvent) throws ApplicationClientException, RemoteException {
        this.searchPane.getChildren().clear();
        if (!(this.searchByTitle.getText().isEmpty() && this.searchByArtist.getText().isEmpty())) {
            ApplicationClient client = RMIconnection.getApplicationClient();
            List<ArticleDTO> result = client.searchArticlesByAttributes(this.searchByTitle.getText(), this.searchByArtist.getText());
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
        this.searchByMedium.setText("");
    }

}
