package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Simulator;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class ButtonCalls {

    void removeLabels(Button runButton, StackPane menu, Label appliedLabel) {
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

    void runProgram(Button run, Button showMenu, StackPane root, StackPane menu, HBox simWindow, HBox info, Label appliedLabel) {
        run.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            menu.getChildren().remove(appliedLabel);
            showMenu.setDisable(false);
            simWindow.setEffect(null);
            info.setEffect(null);
            Simulator.theSimulator.startSimulation();

            GUI.theGUI.pausePlaySim.setDisable(false);
            GUI.theGUI.stopSim.setDisable(false);
            GUI.theGUI.printSim.setDisable(true);

        });
    }

    void applyVariable(Button applySettings, Button runButton,
                       NumberTextField peopleAmount, NumberTextField infectedAmount,
                       StackPane menu,
                       Label tooBigPopulationLabel, Label population0Label, Label infectedOverPopLabel, Label appliedLabel, Draw bob,
                       NumberTextField vaccinePercent, NumberTextField sanitizerPercent,
                       NumberTextField stayHomePercent, NumberTextField coverMouthPercent,
                       GUI gui, Styler styler, VBox mainPanel, NumberTextField infectionRisk,
                       NumberTextField infectionRange, NumberTextField deathRisk, NumberTextField minDaysbfRecovered,
                       NumberTextField maxDaysbfRecovered, NumberTextField movingRisk) {
        applySettings.setOnMouseClicked(event -> {

            int people = 0;
            int infected = 0;
            int vaccinatedPercent = 0;
            int handsanitizedPercent = 0;
            int staysHomePercent = 0;
            int coverCoughPercent = 0;
            double risk = 0;
            int range = 0;
            double death = 0;
            int maxDayRecover = 1;
            int minDayRecover = 0;
            double moving = 0;

            //if numberTextField is empty initialize variable to 0

            try {

                if (!peopleAmount.getText().equals("")) {
                    people = Integer.parseInt(peopleAmount.getText());
                }

                if (!infectedAmount.getText().equals("")) {
                    infected = Integer.parseInt(infectedAmount.getText());
                }

                if (!vaccinePercent.getText().equals("")) {
                    vaccinatedPercent = Integer.parseInt(vaccinePercent.getText());
                }

                if (!sanitizerPercent.getText().equals("")) {
                    handsanitizedPercent = Integer.parseInt(sanitizerPercent.getText());
                }

                if (!stayHomePercent.getText().equals("")) {
                    staysHomePercent = Integer.parseInt(stayHomePercent.getText());
                }

                if (!coverMouthPercent.getText().equals("")) {
                    coverCoughPercent = Integer.parseInt(coverMouthPercent.getText());
                }

                if (!infectionRisk.getText().equals("")) {
                    risk = Double.parseDouble(infectionRisk.getText());
                }
                if (!infectionRange.getText().equals("")) {
                    range = Integer.parseInt(infectionRange.getText());
                }
                if (!deathRisk.getText().equals("")) {
                    death = Double.parseDouble(deathRisk.getText());
                }
                if (!maxDaysbfRecovered.getText().equals("")) {
                    maxDayRecover = Integer.parseInt(maxDaysbfRecovered.getText());
                }
                if (!minDaysbfRecovered.getText().equals("")) {
                    minDayRecover = Integer.parseInt(minDaysbfRecovered.getText());
                }
                if (!movingRisk.getText().equals("")) {
                    moving = Double.parseDouble(movingRisk.getText());
                }


                //Alertbox hvis procenterne er over 100 eller under 0
                if (!((0 <= vaccinatedPercent && vaccinatedPercent <= 100) && (0 <= handsanitizedPercent && handsanitizedPercent <= 100) && (0 <= staysHomePercent && staysHomePercent <= 100) && (0 <= coverCoughPercent && coverCoughPercent <= 100))) {
                    GUI.theGUI.displayMessage(Alert.AlertType.ERROR, "ERROR", "Invalid percentage", "Percentage must be between 0 - 100");
                    return;
                }


                if (people > 0 && infected > 0 && people <= 1000 && infected <= people) {
                    runButton.setDisable(false);
                    Simulator.theSimulator.clearSimulation();

                    //Graf med statistikker
                    gui.lineChart = new LiveLineChart();
                    LineChart chart = gui.lineChart.createLineChart(people);
                    styler.StyleChart(chart);

                    //Graf og canvas tilføjes til main panel
                    mainPanel.getChildren().set(0, chart);

                    //Opretter personer og sygdom
                    Simulator.theSimulator.initialiseSimulation(people, infected, risk, range, death, minDayRecover, maxDayRecover, moving);
                    //sætter personer til at være vaccineret
                    Simulator.theSimulator.vaccinatePeople(vaccinatedPercent, infected);
                    Simulator.theSimulator.handsanitizePeople(handsanitizedPercent);
                    Simulator.theSimulator.coverCoughPeople(coverCoughPercent);
                    Simulator.theSimulator.stayHomePeople(staysHomePercent);

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
                } else {
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

            } catch (RuntimeException e) {
                runButton.setDisable(true);
                System.out.println(e);
                GUI.theGUI.displayMessage(Alert.AlertType.ERROR, "ERROR", "Invalid Variables", "Population, range, days and fields with percent symbols \nmust be integers. ");
            }

        });

    }

    void pauseSimMenu(Button showMenu, Button runButton, StackPane root, StackPane menu, HBox simWindow, HBox info, BoxBlur boxblur) {
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().add(menu);
            showMenu.setDisable(true);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            Simulator.theSimulator.pauseSimulation();
            runButton.setText("Continue");
        });
    }

    void pausePlaySim() {
        GUI.theGUI.pausePlaySim.setOnMouseClicked(event -> {
            if (Simulator.theSimulator.isSimulationActive()) {
                Simulator.theSimulator.pauseSimulation();
                GUI.theGUI.pausePlaySim.setText("\u25B6");
                GUI.theGUI.pausePlaySim.setFont(Font.font(20));
            } else {
                Simulator.theSimulator.startSimulation();
                GUI.theGUI.pausePlaySim.setText("\u23F8");
                GUI.theGUI.pausePlaySim.setFont(Font.font(14));
            }
        });
    }

    void stopSim() {
        GUI.theGUI.stopSim.setOnMouseClicked(event -> Simulator.theSimulator.stopSimulation());
    }

    void printSim(Stage stage) {
        GUI.theGUI.printSim.setOnMouseClicked(event -> GUI.theGUI.exportFileDialog(stage));
    }

    public void setImportImage(ImageView imgView, String imageName, Button startButton)
    {
        imgView.setOnMouseClicked(event -> {
            try {
                Simulator.theSimulator.importMap(imageName + ".png");
                GUI.theGUI.visualMap = GUI.theGUI.picture.resizeImage(imageName + ".png", 20);
                GUI.theGUI.picture.setBackground(GUI.theGUI.visualMap);
                startButton.setDisable(true);
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        });
    }
}
