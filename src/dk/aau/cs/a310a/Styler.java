package dk.aau.cs.a310a;

import javafx.scene.control.Label;

public class Styler {
    public void StyleLabel(Label x) {
        x.getStylesheets().add(getClass().getResource("LabelStyle.css").toExternalForm());
        x.getStyleClass().add("label");
    }
}
