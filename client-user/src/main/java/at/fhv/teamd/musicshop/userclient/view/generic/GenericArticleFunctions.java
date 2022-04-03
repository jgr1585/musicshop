package at.fhv.teamd.musicshop.userclient.view.generic;

import javafx.scene.control.TextField;

public abstract class GenericArticleFunctions {

    public static void numberOnly(TextField mediumAmount) {
        mediumAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                mediumAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
