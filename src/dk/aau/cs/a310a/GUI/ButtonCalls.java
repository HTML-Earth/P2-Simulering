package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Simulator;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
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

    void runProgram(Button run, Button showMenu, StackPane root, StackPane menu, HBox simWindow, HBox info, Simulator sim, Label appliedLabel, Button pausePlaySim, Button printSim) {
        run.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            menu.getChildren().remove(appliedLabel);
            showMenu.setDisable(false);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();

            pausePlaySim.setDisable(false);
            printSim.setDisable(true);

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

            //Alertbox hvis procenterne er over 100 eller under 0
            if (!((0 <= vaccinatedPercent && vaccinatedPercent <= 100) && (0 <= handsanitizedPercent && handsanitizedPercent <= 100) && (0 <= staysHomePercent && staysHomePercent <= 100) && (0 <= coverCoughPercent && coverCoughPercent <= 100))) {
                GUI.theGUI.displayMessage(Alert.AlertType.ERROR, "ERROR", "Invalid percentage", "Percentage must be between 0 - 100");
                return;
            }


            if (people > 0 && infected > 0 && people <= 1000 && infected <= people) {
                runButton.setDisable(false);
                sim.clearSimulation();

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
                if (menu.getChildren().contains(infectedOverPopLabel)) {
                    menu.getChildren().remove(infectedOverPopLabel);
                }
                menu.getChildren().add(appliedLabel);

            } else if (infected > 0 && people > 1000) {
                if (menu.getChildren().contains(tooBigPopulationLabel)) {
                    menu.getChildren().remove(tooBigPopulationLabel);
                }
                menu.getChildren().remove(population0Label);
                menu.getChildren().add(tooBigPopulationLabel);

                removeLabels(runButton, menu, appliedLabel);

                runButton.setDisable(true);
            } else if (infected > people) {
                if (menu.getChildren().contains(population0Label)) {
                    menu.getChildren().remove(population0Label);
                }
                if (menu.getChildren().contains(appliedLabel)) {
                    menu.getChildren().remove(appliedLabel);
                }
                if (menu.getChildren().contains(tooBigPopulationLabel)) {
                    menu.getChildren().remove(tooBigPopulationLabel);
                }
                if (menu.getChildren().contains(infectedOverPopLabel)) {
                    menu.getChildren().remove(infectedOverPopLabel);
                }
                menu.getChildren().add(infectedOverPopLabel);

                runButton.setDisable(true);
            }
            else {
                if (menu.getChildren().contains(population0Label)) {
                    menu.getChildren().remove(population0Label);
                }
                if (menu.getChildren().contains(infectedOverPopLabel)) {
                    menu.getChildren().remove(infectedOverPopLabel);
                }
                menu.getChildren().remove(tooBigPopulationLabel);
                menu.getChildren().add(population0Label);

                removeLabels(runButton, menu, appliedLabel);
            }
        });

    }

    void pauseSimMenu(Button showMenu, Button runButton, StackPane root, StackPane menu, HBox simWindow, HBox info, Simulator sim, BoxBlur boxblur) {
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().add(menu);
            showMenu.setDisable(true);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            sim.pauseSimulation();
            runButton.setText("Continue");
        });
    }

    void pausePlaySim(Button pausePlaySim, Simulator simulator) {
        pausePlaySim.setOnMouseClicked(event -> {
            if (simulator.isSimulationActive()) {
                simulator.pauseSimulation();
                pausePlaySim.setText("\u25B6");
                pausePlaySim.setFont(Font.font(20));
            }
            else {
                simulator.startSimulation();
                pausePlaySim.setText("\u23F8");
                pausePlaySim.setFont(Font.font(14));
            }
        });
    }

    void stopSim (Button stopSim, Button printSim, Button pausePlaySim, Simulator simulator) {
        stopSim.setOnMouseClicked(event -> {
                simulator.stopSimulation();
                printSim.setDisable(false);
                pausePlaySim.setDisable(true);

        });
    }

    void printSim (Button printSim, Simulator simulator) {
        printSim.setOnMouseClicked(event -> simulator.printResults());
    }

}
