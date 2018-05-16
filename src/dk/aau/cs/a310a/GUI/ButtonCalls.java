package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Simulator;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ButtonCalls {

    void removeLabels (Button runButton, StackPane menu, Label appliedLabel) {
        runButton.setDisable(true);

        if (menu.getChildren().contains(appliedLabel)) {
            menu.getChildren().remove(appliedLabel);
        }
    }

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

    void applyVariable(Button applySettings, Button runButton,
                       NumberTextField peopleAmount, NumberTextField infectedAmount,
                       Simulator sim, StackPane menu,
                       Label tooBigPopulationLabel, Label population0Label, Label infectedOverPopLabel, Label appliedLabel, Draw bob,
                       NumberTextField vaccinePercent, NumberTextField sanitizerPercent,
                       NumberTextField stayHomePercent, NumberTextField coverMouthPercent,
                       GUI gui, Styler styler, VBox mainPanel) {
        applySettings.setOnMouseClicked(event -> {
            int people = Integer.parseInt(peopleAmount.getText());
            int infected = Integer.parseInt(infectedAmount.getText());
            int vaccinatedPercent = Integer.parseInt(vaccinePercent.getText());
            int handsanitizedPercent = Integer.parseInt(sanitizerPercent.getText());
            int staysHomePercent = Integer.parseInt(stayHomePercent.getText());
            int coverCoughPercent = Integer.parseInt(coverMouthPercent.getText());
            if (people > 0 && infected > 0 && people <= 1000 && infected < people) {
                runButton.setDisable(false);
                sim.stopSimulation();

                //Graf med statistikker
                gui.lineChart = new LiveLineChart();
                LineChart chart = gui.lineChart.createLineChart(people);
                styler.StyleChart(chart);

                //Graf og canvas tilføjes til main panel
                mainPanel.getChildren().set(0, chart);

                //Opretter personer
                sim.initialiseSimulation(people, infected);
                //sætter personer til at være vaccineret
                sim.vaccinatePeople(vaccinatedPercent, infected);
                sim.handsanitizePeople(handsanitizedPercent);
                sim.coverCoughPeople(coverCoughPercent);
                sim.stayHomePeople(staysHomePercent);
                runButton.setText("Start");
                bob.drawBackground();
                menu.getChildren().removeAll(tooBigPopulationLabel, population0Label);
                if (menu.getChildren().contains(appliedLabel)) {
                    menu.getChildren().remove(appliedLabel);
                }
                menu.getChildren().add(appliedLabel);

            } else if (people > 0 && infected > 0 && people > 1000 || infected > people) {
                if (menu.getChildren().contains(tooBigPopulationLabel)) {
                    menu.getChildren().remove(tooBigPopulationLabel);
                }
                menu.getChildren().remove(population0Label);
                menu.getChildren().add(tooBigPopulationLabel);

                removeLabels(runButton, menu, appliedLabel);

                runButton.setDisable(true);
            } else {
                if (menu.getChildren().contains(population0Label)) {
                    menu.getChildren().remove(population0Label);
                }
                menu.getChildren().remove(tooBigPopulationLabel);
                menu.getChildren().add(population0Label);

                removeLabels(runButton, menu, appliedLabel);
            }
        });

    }

    void pauseSimMenu(Button showMenu, Button runButton, StackPane root, StackPane menu, HBox simWindow, GridPane info, Simulator sim, BoxBlur boxblur) {
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().add(menu);
            root.getChildren().remove(showMenu);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            sim.pauseSimulation();
            runButton.setText("Continue");
        });
    }

}
