package dk.aau.cs.a310a.GUI;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class Styler {
    public void StyleLabel(Label x) {
        x.getStylesheets().add(getClass().getResource("Styles/LabelStyle.css").toExternalForm());
        x.getStyleClass().add("label");
    }

    public void StyleGrid(GridPane y) {
        y.getStylesheets().add(getClass().getResource("Styles/GridStyle.css").toExternalForm());
        y.getStyleClass().add("grid");
    }

}
