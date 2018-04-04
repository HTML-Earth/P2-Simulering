package dk.aau.cs.a310a;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Menu {
    boolean showMenu = true;

    void changeLayer(StackPane root, Node x, Button y) {
        if (!showMenu) {
            root.getChildren().add(x);
        } else {
            root.getChildren().add(new Rectangle(900, 600, Color.rgb(50, 50, 50, 0.95)));
            root.getChildren().add(y);
        }
    }

}
