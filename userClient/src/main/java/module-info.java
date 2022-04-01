module at.fhv.teamd.userclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens at.fhv.teamd.musicshop.userclient to javafx.fxml;
    exports at.fhv.teamd.musicshop.userclient;
}