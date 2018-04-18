package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Simulator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ButtonCalls {

    void cuztomizeLabel(Label label, double x, double y, double size) {
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setFont(Font.font(size));
    }

    void cuztomizeLabel(Label label, double x, double y, String fontname, double size, Effect effect, FontWeight fontweight) {
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setFont(Font.font(fontname, fontweight, size));
        label.setEffect(effect);
    }

    void runProgram(Button run, Button showMenu, StackPane root, StackPane menu, HBox simWindow, GridPane info, Simulator sim, Label appliedLabel) {
        run.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            menu.getChildren().remove(appliedLabel);
            root.getChildren().add(showMenu);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();

        });
    }

    void applyVariable(Button applySettings, Button runButton, NumberTextField susceptibleAmount, NumberTextField infectedAmount,
                       NumberTextField recoveredAmount, Simulator sim, StackPane menu, Label tooBigPopulationLabel, Label population0Label, Label appliedLabel, GraphicsContext gc, Image visualMap) {
        applySettings.setOnMouseClicked(event -> {
            int susceptibles = Integer.parseInt(susceptibleAmount.getText());
            int infected = Integer.parseInt(infectedAmount.getText());
            int recovered = Integer.parseInt(recoveredAmount.getText());
            if (susceptibles > 0 && infected > 0 && recovered >= 0 && susceptibles < 1000 && infected < 1000 && recovered < 1000) {
                runButton.setDisable(false);
                sim.stopSimulation();
                sim.initialiseSimulation(susceptibles,infected,recovered);
                runButton.setText("Start");
                gc.drawImage(visualMap,0,0,800,600);
                menu.getChildren().removeAll(tooBigPopulationLabel, population0Label);
                if (menu.getChildren().contains(appliedLabel)) {
                    menu.getChildren().remove(appliedLabel);
                }
                menu.getChildren().add(appliedLabel);

            }
            else if (susceptibles > 0 && infected > 0 && recovered >= 0 && susceptibles >= 1000 || infected >= 1000 || recovered >= 1000) {
                menu.getChildren().remove(population0Label);
                menu.getChildren().add(tooBigPopulationLabel);
            }
            else {
                menu.getChildren().remove(tooBigPopulationLabel);
                menu.getChildren().add(population0Label);
            }
        });

    }

}
