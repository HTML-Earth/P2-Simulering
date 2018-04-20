package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Clock;
import dk.aau.cs.a310a.Simulation.Person;
import dk.aau.cs.a310a.Simulation.Simulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    //Simulator objekt
    Simulator sim;

    public static LiveLineChart lineChart;

    //Billedet i canvas
    Image visualMap;

    BobRoss bob;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() throws IOException {
        sim = new Simulator();
        bob = new BobRoss();
        visualMap = bob.resizeImage("/city.png", 20);
    }

    public void start(Stage stage) {
        //Vindue titel
        stage.setTitle("Zombe");

        //Vindue ikon
        stage.getIcons().add(new Image("file:resources/zombe.png"));

        //ROOT STACKPANE
        StackPane root = new StackPane();

        //SIMULERINGSVINDUE
        HBox simWindow = new HBox();

        //Canvas objekter
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();

        //SIDEPANEL
        VBox sidePanel = new VBox();
        sidePanel.setMinWidth(400);

        //Infoboks med Livestatistikker i hjørnet
        GridPane info = new GridPane();
        info.setMinHeight(150);

        //Infoboks labels i simWindow
        Label stringSusceptible = new Label("Susceptible: " + sim.healthCount(Person.health.Susceptible));
        Label stringInfected = new Label("Infected: " + sim.healthCount(Person.health.Infected));
        Label stringRecovered = new Label("Recovered: " + sim.healthCount(Person.health.Recovered));
        Label stringDead = new Label("Dead: " + sim.healthCount(Person.health.Dead));
        Label stringEpidemic = new Label("");
        Label stringTimeOfDay = new Label();


        //styling af labels
        Styler styler = new Styler();

        styler.StyleLabel(stringSusceptible);
        styler.StyleLabel(stringRecovered);
        styler.StyleLabel(stringInfected);
        styler.StyleLabel(stringDead);
        styler.StyleLabel(stringTimeOfDay);

        info.add(stringSusceptible, 0, 0);
        info.add(stringInfected, 0, 1);
        info.add(stringRecovered, 0, 2);
        info.add(stringDead, 0, 3);
        info.add(stringEpidemic, 0, 4);
        info.add(stringTimeOfDay, 1, 0);

        lineChart = new LiveLineChart();
        simWindow.getChildren().add(lineChart.createLineChart());

        //Scrollpane med persondata
        ScrollPane scrollPane = new ScrollPane();

        //Information om personer
        Label personData = new Label();
        personData.setFont(Font.font(12));

        //graph med statistikker


        //Tilføj personer fra starten
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        scrollPane.setContent(personData);

        //info og scrollpane tilføjes til sidepanel
        sidePanel.getChildren().addAll(info,scrollPane);

        //Canvas og sidepanel tilføjes til simwindow
        simWindow.getChildren().addAll(canvas, sidePanel);
        simWindow.setAlignment(Pos.BOTTOM_LEFT);

        //Simwindow bliver blurred
        BoxBlur boxblur = new BoxBlur();
        boxblur.setHeight(5);
        boxblur.setWidth(5);
        boxblur.setIterations(3);
        simWindow.setEffect(boxblur);

        //MENU
        StackPane menu = new StackPane();

        //Sort baggrund til menu
        Rectangle menuBackground = new Rectangle(900, 600, Color.rgb(50, 50, 50, 0.95));

        //Metoder til knapper og GUI
        ButtonCalls buttonMethod = new ButtonCalls();

        //Effct til labels
        DropShadow dropShadow = new DropShadow();

        // Textfield til at skrive befolkning
        NumberTextField susceptibleAmount = new NumberTextField("100");
        susceptibleAmount.setMaxWidth(80);
        NumberTextField infectedAmount = new NumberTextField("1");
        infectedAmount.setMaxWidth(80);
        NumberTextField vaccinePercent = new NumberTextField("0");
        vaccinePercent.setMaxWidth(40);
        NumberTextField sanitizerPercent = new NumberTextField("0");
        sanitizerPercent.setMaxWidth(40);
        NumberTextField coverMouthPercent = new NumberTextField("0");
        coverMouthPercent.setMaxWidth(40);
        NumberTextField stayHomePercent = new NumberTextField("0");
        stayHomePercent.setMaxWidth(40);


        // Menu - labels til beskrivelse
        Label susceptibleLabel = new Label("Susceptible:");
        Label infectedLabel = new Label("Infected:");
        Label appliedLabel = new Label("Population applied!");
        Label titleLabel = new Label("Zombe");
        Label sirLabel = new Label("SIR-Options:");
        Label spreadLabel = new Label("Spread-modifiers:");
        Label whatPerLabel = new Label("What percentage:");
        Label isVaccinatedLabel = new Label(" - is vaccinated?");
        Label useSanitizersLabel = new Label(" - use handsanitizers?");
        Label coverMouthLabel = new Label(" - cover mouth when coughing?");
        Label stayAtHomeLabel = new Label(" - stays at home after symptoms?");
        Label tooBigPopulationLabel = new Label("Error: Population can't be more than 1000");
        Label population0Label = new Label("Error: Susceptibles and infected can't be 0");

        appliedLabel.setTextFill(Color.LIGHTGREEN);
        titleLabel.setTextFill(Color.WHITE);
        sirLabel.setTextFill(Color.WHITE);
        spreadLabel.setTextFill(Color.WHITE);
        tooBigPopulationLabel.setTextFill(new Color(1,0.3,0.3,1));
        population0Label.setTextFill(new Color(1,0.3,0.3,1));


        buttonMethod.cuztomizeLabel(appliedLabel, 200, 150, 20);
        buttonMethod.cuztomizeLabel(tooBigPopulationLabel, 200, 150, 20);
        buttonMethod.cuztomizeLabel(population0Label, 200, 150, 20);

        buttonMethod.cuztomizeLabel(sirLabel, -260, -180, "Georgia", 20, dropShadow, FontWeight.BOLD);
        buttonMethod.cuztomizeLabel(spreadLabel, -260 + 400, -180, "Georgia", 20, dropShadow, FontWeight.BOLD);
        buttonMethod.cuztomizeLabel(titleLabel, 0, -240, "Georgia", 70, dropShadow, FontWeight.BOLD);


        //Button factory
        Button showMenu = new Button("Menu");
        showMenu.setFont(Font.font(20));
        StackPane.setAlignment(showMenu, Pos.TOP_LEFT);

        Button applySettings = new Button("Apply");
        applySettings.setFont(Font.font(20));

        Button runButton = new Button("Start");
        runButton.setDisable(true);
        runButton.setFont(Font.font(20));

        //Tooltip for <1000 personer
        Tooltip lessthan1000 = new Tooltip("A number between 1 - 999");

        lessthan1000.setStyle("-fx-background-color: Grey");

        Tooltip.install(susceptibleAmount, lessthan1000);
        Tooltip.install(infectedAmount, lessthan1000);


        // Event til starte simulering og fjerne menu og blur
        buttonMethod.runProgram(runButton, showMenu, root, menu, simWindow, info, sim, appliedLabel);

        // Event til at anvende og checke indtastede værdier
        buttonMethod.applyVariable(applySettings, runButton, susceptibleAmount, infectedAmount, sim, menu,
                tooBigPopulationLabel, population0Label, appliedLabel, gc, visualMap);

        //Events til menuknap
        buttonMethod.pauseSimMenu(showMenu, runButton, root, menu, simWindow, info, sim, boxblur);

        //Grids
        //Grid til textfield variabler i menu
        GridPane menuLabels = new GridPane();
        menuLabels.setTranslateX(260);
        menuLabels.setTranslateY(240);

        menuLabels.add(susceptibleLabel, 0, 0);
        menuLabels.add(susceptibleAmount, 1, 0);
        menuLabels.add(whatPerLabel,3,0);
        menuLabels.add(infectedLabel, 0, 1);
        menuLabels.add(infectedAmount, 1, 1);
        menuLabels.add(isVaccinatedLabel, 3, 1);
        menuLabels.add(useSanitizersLabel, 3, 2);
        menuLabels.add(coverMouthLabel, 3, 3);
        menuLabels.add(stayAtHomeLabel, 3, 4);
        menuLabels.add(vaccinePercent, 4, 1);
        menuLabels.add(sanitizerPercent, 4, 2);
        menuLabels.add(coverMouthPercent, 4, 3);
        menuLabels.add(stayHomePercent, 4, 4);


        styler.StyleGrid(menuLabels);

        //Tilføj tom plads i grid
        Pane emptyCol = new Pane();
        emptyCol.setMinWidth(250);
        menuLabels.add(emptyCol, 2, 1);

        //Tilføj % til flere rækker
        for(int i = 1; i < 5; i++) {
            Label percentLabel = new Label("%");
            menuLabels.add(percentLabel, 5, i);
            percentLabel.setTextFill(Color.WHITE);
        }

        //Apply og start knap
        GridPane menuButttonsBottomRight = new GridPane();
        menuButttonsBottomRight.add(applySettings, 0, 0);
        menuButttonsBottomRight.add(runButton, 1, 0);
        menuButttonsBottomRight.setTranslateX(800);
        menuButttonsBottomRight.setTranslateY(600);

        menu.getChildren().addAll(menuBackground, menuLabels, titleLabel, sirLabel, spreadLabel, menuButttonsBottomRight);

        //Tilføj simwindow og menu til root stackpane
        root.getChildren().addAll(simWindow, menu);

        //Vis baggrundsbilledet
        gc.drawImage(visualMap,0,0,800,600);

        final double targetDelta = 0.0166; /* 16.6ms ~ 60fps */

        new AnimationTimer() {
            double previousTime = System.nanoTime();

            public void handle(long currentNanoTime) {

                double currentTime = currentNanoTime / 1_000_000_000.0;
                double deltaTime = currentTime - previousTime;

                //Opdater simulering
                sim.simulate(currentTime, deltaTime);

                if (sim.isSimulationActive()) {
                    //Vis baggrund (hvilket overskriver forrige frame
                    gc.drawImage(visualMap,0,0,800,600);

                    //Reset personData tekst
                    personData.setText("");

                    //Tegn alle personer og print deres info
                    for (Person p : sim.getPeople()) {
                        bob.drawPerson(p,gc);
                        personData.setText(personData.getText() + "\n " + p);
                    }

                    stringSusceptible.setText("Susceptibles: " + sim.healthCount(Person.health.Susceptible));
                    stringInfected.setText("Infected: " + sim.healthCount(Person.health.Infected));
                    stringRecovered.setText("Recovered: " + sim.healthCount(Person.health.Recovered));
                    stringDead.setText("Dead: " + sim.healthCount(Person.health.Dead));
                    stringEpidemic.setText("Chance of epidemic" + "\n" + sim.getR0(1,1.0));
                    stringTimeOfDay.setText(sim.getSimulationTime());
                }

                previousTime = currentTime;
            }
        }.start();

        Scene scene = new Scene(root, 1200, 750);

        stage.setScene(scene);
        stage.show();
    }

}
