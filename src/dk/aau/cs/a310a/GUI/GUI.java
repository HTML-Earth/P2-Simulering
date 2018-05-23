package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Simulation.Person;
import dk.aau.cs.a310a.Simulation.Simulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class GUI extends Application {

    public static LiveLineChart lineChart;

    public static GUI theGUI;

    //Simulator objekt
    Simulator sim;

    //Billedet i canvas
    Image visualMap;

    Draw picture;

    Button pausePlaySim;
    Button stopSim;
    Button printSim;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() throws IOException {
        theGUI = this;
        sim = new Simulator();
        sim.importMap("map01.png");

        picture = new Draw();
        visualMap = picture.resizeImage("map01.png", 20);
        picture.setBackground(visualMap);
    }

    public void start(Stage stage) {
        //Vindue titel
        stage.setTitle("Zombe");

        //Vindue ikon
        stage.getIcons().add(new Image("file:resources/zombe.png"));

        //ROOT STACKPANE
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #CDD8EC;");

        //SIMULERINGSVINDUE
        HBox simWindow = new HBox();

        // filechooser
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("PNG Files", "*.png");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);

        //Canvas objekter
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        picture.setGraphicsContext(gc);

        //MAINPANEL
        VBox mainPanel = new VBox();
        mainPanel.setMinWidth(400);

        //SIDEPANEL
        VBox sidePanel = new VBox();
        sidePanel.setMinWidth(400);

        // HBOX til toppen af sidepanel
        HBox corner = new HBox();

        //VBOX som indsættes i HBOX i toppen af sidepanel
        VBox interaction = new VBox();
        interaction.setMinWidth(200);
        interaction.setSpacing(10);
        interaction.setAlignment(Pos.CENTER);

        Label stringTimeOfDay = new Label();
        stringTimeOfDay.setMinWidth(100);
        stringTimeOfDay.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        Button showMenu = new Button("Menu");
        showMenu.setFont(Font.font(14));
        showMenu.setDisable(true);

        HBox bottomInteraction = new HBox();
        bottomInteraction.setAlignment(Pos.CENTER);
        bottomInteraction.setSpacing(10);

        pausePlaySim = new Button("\u23F8"); // ⏸
        pausePlaySim.setFont(Font.font(14));
        pausePlaySim.setMinSize(40,40);
        pausePlaySim.setMaxSize(40, 40);


        stopSim = new Button("\u23F9");
        stopSim.setFont(Font.font(14));
        stopSim.setMinSize(40,40);

        printSim = new Button("\uD83D\uDCBE");
        printSim.setFont(Font.font(14));
        printSim.setDisable(true);
        printSim.setMinSize(40,40);

        bottomInteraction.getChildren().addAll(pausePlaySim, stopSim, printSim);
        interaction.getChildren().addAll(stringTimeOfDay, showMenu, bottomInteraction);



        //Infoboks med Livestatistikker i hjørnet
        VBox info = new VBox();
        info.setMinHeight(150);
        info.setAlignment(Pos.CENTER);

        //Infoboks labels i info simWindow
        Label stringSusceptible = new Label("Susceptible: " + sim.healthCount(Person.health.Susceptible));
        Label stringInfected = new Label("Infected: " + sim.healthCount(Person.health.Infected));
        Label stringRecovered = new Label("Recovered: " + sim.healthCount(Person.health.Recovered));
        Label stringDead = new Label("Dead: " + sim.healthCount(Person.health.Dead));
        Label stringEpidemic = new Label("");
        stringEpidemic.setTextAlignment(TextAlignment.CENTER);


        //styling af labels
        Styler styler = new Styler();

        styler.StyleLabel(stringSusceptible);
        styler.StyleLabel(stringRecovered);
        styler.StyleLabel(stringInfected);
        styler.StyleLabel(stringDead);

        info.getChildren().addAll(stringSusceptible, stringInfected, stringRecovered, stringDead, stringEpidemic);

        // Tilføjer til corner
        corner.getChildren().addAll(interaction,info);

        //Scrollpane med persondata
        ScrollPane scrollPane = new ScrollPane();

        //Information om personer
        Label personData = new Label();
        personData.setFont(Font.font(12));

        //Tilføj personer fra starten
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        scrollPane.setContent(personData);

        //PLACEHOLDER GRAF
        lineChart = new LiveLineChart();
        LineChart chart = lineChart.createLineChart(100);
        styler.StyleChart(chart);

        //Graf og canvas tilføjes til main panel
        mainPanel.getChildren().addAll(chart, canvas);

        //corner og scrollpane tilføjes til sidepanel
        sidePanel.getChildren().addAll(corner, scrollPane);

        //Main panel og side panel tilføjes til simwindow
        simWindow.getChildren().addAll(mainPanel, sidePanel);
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
        NumberTextField infectionRisk = new NumberTextField("0.3");
        infectionRisk.setMaxWidth(40);
        NumberTextField infectionRange = new NumberTextField("2");
        infectionRange.setMaxWidth(40);
        NumberTextField deathRisk = new NumberTextField("0.000002");
        deathRisk.setMaxWidth(80);
        NumberTextField minDaysbfRecovered = new NumberTextField("1");
        minDaysbfRecovered.setMaxWidth(40);
        NumberTextField maxDaysbfRecovered = new NumberTextField("4");
        maxDaysbfRecovered.setMaxWidth(40);
        NumberTextField movingRisk = new NumberTextField("0.1");
        movingRisk.setMaxWidth(40);


        // Menu - labels til beskrivelse
        Label susceptibleLabel = new Label("Population:");
        Label infectedLabel = new Label("Infected:");
        Label appliedLabel = new Label("Population applied!");
        Label titleLabel = new Label("Zombe");
        Label sirLabel = new Label("SIR-Options:");
        Label diseaseLabel = new Label("Disease-Options:");
        Label spreadLabel = new Label("Spread-modifiers:");
        Label whatPerLabel = new Label("What percentage:");
        Label isVaccinatedLabel = new Label(" - is vaccinated?");
        Label useSanitizersLabel = new Label(" - use handsanitizers?");
        Label coverMouthLabel = new Label(" - cover mouth when coughing?");
        Label stayAtHomeLabel = new Label(" - stays at home after symptoms?");
        Label tooBigPopulationLabel = new Label("Error: Population can't be more than 1000");
        Label population0Label = new Label("Error: Population or infected can't be 0");
        Label infectedOverPopLabel = new Label("Error: Infected must be less than population");
        Label riskLabel = new Label("Enter infection risk (between 0 and 1): ");
        Label rangeLabel = new Label("Enter infection range: ");
        Label deathLabel = new Label("Enter base risk of dying: ");
        Label minDaysLabel = new Label("Minimum days before recovered: ");
        Label maxDaysLabel = new Label("Maximum days before recovered: ");
        Label movingRiskLabel = new Label("Risk of infection while moving ");

        appliedLabel.setTextFill(Color.LIGHTGREEN);
        titleLabel.setTextFill(Color.WHITE);
        sirLabel.setTextFill(Color.WHITE);
        diseaseLabel.setTextFill(Color.WHITE);
        spreadLabel.setTextFill(Color.WHITE);
        riskLabel.setTextFill(Color.WHITE);
        rangeLabel.setTextFill(Color.WHITE);
        deathLabel.setTextFill(Color.WHITE);
        minDaysLabel.setTextFill(Color.WHITE);
        maxDaysLabel.setTextFill(Color.WHITE);
        movingRiskLabel.setTextFill(Color.WHITE);
        tooBigPopulationLabel.setTextFill(new Color(1,0.3,0.3,1));
        population0Label.setTextFill(new Color(1,0.3,0.3,1));
        infectedOverPopLabel.setTextFill(new Color(1,0.3,0.3,1));


        buttonMethod.cuztomizeLabel(appliedLabel, 0, 245, 20);
        buttonMethod.cuztomizeLabel(tooBigPopulationLabel, 0, 245, 20);
        buttonMethod.cuztomizeLabel(population0Label, 0, 245, 20);
        buttonMethod.cuztomizeLabel(infectedOverPopLabel, 0, 245, 20);

        buttonMethod.cuztomizeLabel(sirLabel, -260, -180, "Georgia", 20, dropShadow, FontWeight.BOLD);
        buttonMethod.cuztomizeLabel(diseaseLabel, -260, -180 + 230, "Georgia", 20, dropShadow, FontWeight.BOLD );
        buttonMethod.cuztomizeLabel(spreadLabel, -260 + 400, -180, "Georgia", 20, dropShadow, FontWeight.BOLD);
        buttonMethod.cuztomizeLabel(titleLabel, 0, -240, "Georgia", 70, dropShadow, FontWeight.BOLD);


        //Button factory

        Button applySettings = new Button("Apply");
        applySettings.setFont(Font.font(20));

        Button runButton = new Button("Start");
        runButton.setDisable(true);
        runButton.setFont(Font.font(20));

        Button imageButton = new Button("Upload map");
        imageButton.setFont(Font.font(20));

        //Tooltip for <1000 personer
        Tooltip lessthan1000 = new Tooltip("A number between 1 - 1000");

        //Tooltip for infected
        Tooltip lessthanpop = new Tooltip("A number over 0 and under population");

        //Tooltip for procenter
        Tooltip percentTip = new Tooltip("A number between 0 - 100");

        lessthan1000.setStyle("-fx-background-color: Grey");
        lessthanpop.setStyle("-fx-background-color: Grey");
        percentTip.setStyle("-fx-background-color: Grey");

        Tooltip.install(susceptibleAmount, lessthan1000);
        Tooltip.install(infectedAmount, lessthanpop);
        Tooltip.install(coverMouthPercent, percentTip);
        Tooltip.install(sanitizerPercent, percentTip);
        Tooltip.install(stayHomePercent, percentTip);
        Tooltip.install(vaccinePercent, percentTip);



        // Event til at anvende og checke indtastede værdier fra menu
        buttonMethod.applyVariable(applySettings, runButton, susceptibleAmount, infectedAmount,
                menu,
                tooBigPopulationLabel, population0Label, infectedOverPopLabel, appliedLabel,
                picture, vaccinePercent, sanitizerPercent, stayHomePercent, coverMouthPercent,
                this, styler, mainPanel, infectionRisk, infectionRange, deathRisk, minDaysbfRecovered,
                maxDaysbfRecovered, movingRisk);

        // Event til starte simulering og fjerne menu og blur
        buttonMethod.runProgram(runButton, showMenu, root, menu, simWindow, corner, appliedLabel);

        //Events til menuknap
        buttonMethod.pauseSimMenu(showMenu, runButton, root, menu, simWindow, corner, boxblur);

        // Event til at pause eller starte simulationen
        buttonMethod.pausePlaySim();

        // Event til at stoppe simulationen
        buttonMethod.stopSim();

        // Event til at printe simulationen
        buttonMethod.printSim(stage);

        // Events til Uploadknap
        imageButton.setOnMouseClicked(event ->{
            runButton.setDisable(true);
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    if (sim.importMap(file)) {
                        visualMap = picture.resizeImage(file, 20);
                        picture.setBackground(visualMap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Grids
        //Grid til textfield variabler i menu
        GridPane menuLabelsTop = new GridPane();
        menuLabelsTop.setTranslateX(260);
        menuLabelsTop.setTranslateY(240);

        menuLabelsTop.add(susceptibleLabel, 0, 0);
        menuLabelsTop.add(susceptibleAmount, 1, 0);
        menuLabelsTop.add(whatPerLabel,3,0);
        menuLabelsTop.add(infectedLabel, 0, 1);
        menuLabelsTop.add(infectedAmount, 1, 1);
        menuLabelsTop.add(isVaccinatedLabel, 3, 1);
        menuLabelsTop.add(useSanitizersLabel, 3, 2);
        menuLabelsTop.add(coverMouthLabel, 3, 3);
        menuLabelsTop.add(stayAtHomeLabel, 3, 4);
        menuLabelsTop.add(vaccinePercent, 4, 1);
        menuLabelsTop.add(sanitizerPercent, 4, 2);
        menuLabelsTop.add(coverMouthPercent, 4, 3);
        menuLabelsTop.add(stayHomePercent, 4, 4);

        GridPane menuLabelsBottom = new GridPane();
        menuLabelsBottom.setTranslateX(260);
        menuLabelsBottom.setTranslateY(450);
        menuLabelsBottom.add(riskLabel, 0,0);
        menuLabelsBottom.add(infectionRisk, 1, 0);
        menuLabelsBottom.add(rangeLabel, 0, 1);
        menuLabelsBottom.add(infectionRange, 1, 1);
        menuLabelsBottom.add(deathLabel, 0, 2);
        menuLabelsBottom.add(deathRisk, 1, 2);
        menuLabelsBottom.add(minDaysLabel, 2, 0);
        menuLabelsBottom.add(minDaysbfRecovered, 3, 0);
        menuLabelsBottom.add(maxDaysLabel, 2, 1);
        menuLabelsBottom.add(maxDaysbfRecovered, 3, 1);
        menuLabelsBottom.add(movingRiskLabel, 2, 2);
        menuLabelsBottom.add(movingRisk, 3, 2);

        styler.StyleGrid(menuLabelsBottom);
        styler.StyleGrid(menuLabelsTop);

        //Tilføj tom plads i grid
        Pane emptyCol = new Pane();
        emptyCol.setMinWidth(175);
        menuLabelsTop.add(emptyCol, 2, 1);

        //Tilføj % til flere rækker
        for(int i = 1; i < 5; i++) {
            Label percentLabel = new Label("%");
            menuLabelsTop.add(percentLabel, 5, i);
            percentLabel.setTextFill(Color.WHITE);
        }

        //Apply og start knap
        GridPane menuButttonsBottomRight = new GridPane();
        Pane emptyCol2 = new Pane();
        emptyCol2.setMinWidth(500);
        menuButttonsBottomRight.add(applySettings, 2, 0);
        menuButttonsBottomRight.add(runButton, 3, 0);
        menuButttonsBottomRight.add(imageButton,0,0);
        menuButttonsBottomRight.add(emptyCol2,1,0);
        menuButttonsBottomRight.setTranslateX(200);
        menuButttonsBottomRight.setTranslateY(600);

        menu.getChildren().addAll(menuBackground, menuLabelsTop, menuLabelsBottom, titleLabel, sirLabel, diseaseLabel, spreadLabel, menuButttonsBottomRight);

        //Tilføj simwindow og menu til root stackpane
        root.getChildren().addAll(simWindow, menu);

        //Vis baggrundsbilledet
        picture.drawBackground();

        new AnimationTimer() {
            double previousTime = System.nanoTime();

            public void handle(long currentNanoTime) {

                double currentTime = currentNanoTime / 1_000_000_000.0;
                double deltaTime = currentTime - previousTime;

                //Opdater simulering
                sim.simulate(currentTime);

                if (sim.isSimulationActive()) {
                    //Vis baggrund (hvilket overskriver forrige frame
                    picture.drawBackground();

                    //Reset personData tekst
                    personData.setText("");

                    //Tegn alle personer og print deres info
                    for (Person p : sim.getPeople()) {
                        picture.drawPerson(p);
                        personData.setText(personData.getText() + "\n " + p);
                    }

                    stringSusceptible.setText("Susceptibles: " + sim.healthCount(Person.health.Susceptible));
                    stringInfected.setText("Infected: " + sim.healthCount(Person.health.Infected));
                    stringRecovered.setText("Recovered: " + sim.healthCount(Person.health.Recovered));
                    stringDead.setText("Dead: " + sim.healthCount(Person.health.Dead));
                    stringEpidemic.setText(sim.getR0(1,1.0));
                    stringTimeOfDay.setText(sim.getSimulationTime());
                }

                previousTime = currentTime;
            }
        }.start();

        Scene scene = new Scene(root, 1200, 750);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void onStopSimulation() {
        pausePlaySim.setDisable(true);
        stopSim.setDisable(true);
        printSim.setDisable(false);
    }

    public void displayMessage(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public void exportFileDialog(Stage stage) {
        if (sim.isSimulationActive() || sim.hasBeenInitialised()) {
            return;
        }

        FileChooser.ExtensionFilter statsFilter = new FileChooser.ExtensionFilter("Comma separated values", "*.csv");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(statsFilter);

        File file = fileChooser.showSaveDialog(stage);
        sim.exportResults(file);
    }
}
