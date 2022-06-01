package at.fhv.teamd.musicshop.userclient.view;

import javafx.beans.property.ReadOnlyBooleanProperty;

public interface ActivePropertyBindable {
    void bindActiveProperty(ReadOnlyBooleanProperty activeProp);
}
