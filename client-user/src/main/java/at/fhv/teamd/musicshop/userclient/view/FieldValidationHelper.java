package at.fhv.teamd.musicshop.userclient.view;

import javafx.scene.control.TextField;

public class FieldValidationHelper {

    private FieldValidationHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void numberOnly(TextField mediumAmount) {
        mediumAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                mediumAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
