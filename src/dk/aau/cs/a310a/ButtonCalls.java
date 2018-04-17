package dk.aau.cs.a310a;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ButtonCalls {

    void runProgram(Button run, Button showMenu, StackPane root, StackPane menu, HBox simWindow, GridPane info, Simulator sim, boolean applyLabelIsActive, Label appliedLabel) {
        run.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            menu.getChildren().remove(appliedLabel);
            root.getChildren().add(showMenu);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();

        });
    }
}
