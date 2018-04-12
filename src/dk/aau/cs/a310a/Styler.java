package dk.aau.cs.a310a;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Styler {
    public void StyleLabel(Label x) {
        x.getStylesheets().add(getClass().getResource("LabelStyle.css").toExternalForm());
        x.getStyleClass().add("label");
    }

    public void StyleGrid(GridPane y) {
        y.getStylesheets().add(getClass().getResource("GridStyle.css").toExternalForm());
        y.getStyleClass().add("grid");
    }
}
