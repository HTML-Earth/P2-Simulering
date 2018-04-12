package dk.aau.cs.a310a;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class GUI extends Application {


    //booleans
    boolean applyLabelIsActive = false;

    //Simulator objekt
    Simulator sim;

    //Influenza objekt
    Influenza influenzaA;

    //Billedet i canvas
    Image visualMap;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() throws IOException {
        sim = new Simulator();
        influenzaA = new Influenza(Influenza.influenzaType.A);
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

        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();

        //SIDEPANEL
        VBox sidePanel = new VBox();
        sidePanel.setMinWidth(400);

        //Infoboks med Livestatistikker i hjørnet
        GridPane info = new GridPane();
        info.setMinHeight(150);

        //Infoboks labels
        Label stringSusceptible = new Label("Susceptible: " + sim.healthCount(Person.health.Susceptible));
        Label stringInfected = new Label("Infected: " + sim.healthCount(Person.health.Infected));
        Label stringRecovered = new Label("Recovered: " + sim.healthCount(Person.health.Recovered));
        Label stringDead = new Label("Dead: " + sim.healthCount(Person.health.Dead));
        Label stringEpidemic = new Label("Chance of epidemic" + "\n" + influenzaA.calculateR0(1,1.0));

        //styling af labels
        Styler styler = new Styler();
        styler.StyleLabel(stringSusceptible);
        styler.StyleLabel(stringRecovered);
        styler.StyleLabel(stringInfected);
        styler.StyleLabel(stringDead);

        info.add(stringSusceptible, 0, 0);
        info.add(stringInfected, 0, 1);
        info.add(stringRecovered, 0, 2);
        info.add(stringDead, 0, 3);
        info.add(stringEpidemic, 0, 4);

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

        //Menu - Tilføj knapper
        //Combobox af typen ComboboxItem, objekter af ComboboxItem tilføjes til menuen
        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0),
                new ComboItem("Folk bliver oftere hjemme", 2.0),
                new ComboItem("Vacciner", 3.0)
        );
        //Standard value til combobox
        comboBox.setValue(new ComboItem("Alle hoster i ærmet", 0.0));
        comboBox.setTranslateX(-300);
        comboBox.setTranslateY(200);

        // Textfield til at skrive befolkning

        NumberTextField susceptibleAmount = new NumberTextField("100");
        susceptibleAmount.setMaxWidth(80);

        NumberTextField infectedAmount = new NumberTextField("1");
        infectedAmount.setMaxWidth(80);

        NumberTextField recoveredAmount = new NumberTextField("0");
        recoveredAmount.setMaxWidth(80);

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
        Label recoveredLabel = new Label("Recovered:");
        Label appliedLabel = new Label("Population applied!");
        Label resetLabel = new Label("Simulation reset!");
        Label titleLabel = new Label("Zombe");
        Label sirLabel = new Label("SIR-Options:");
        Label spreadLabel = new Label("Spread-modifiers:");
        Label whatPerLabel = new Label("What percentage:");
        Label isVaccinatedLabel = new Label(" - is vaccinated?");
        Label useSanitizersLabel = new Label(" - use handsanitizers?");
        Label coverMouthLabel = new Label(" - cover mouth when coughing?");
        Label stayAtHomeLabel = new Label(" - stays at home after symptoms?");
        Label tooBigPopulationLabel = new Label("Error: Population can't be more than 1000");
        Label population0Label = new Label("Error: Susceptibles and infected cam't be 0");

        susceptibleLabel.setTextFill(Color.WHITE);
        infectedLabel.setTextFill(Color.WHITE);
        recoveredLabel.setTextFill(Color.WHITE);
        appliedLabel.setTextFill(Color.LIGHTGREEN);
        resetLabel.setTextFill(Color.ORANGE);
        titleLabel.setTextFill(Color.WHITE);
        sirLabel.setTextFill(Color.WHITE);
        spreadLabel.setTextFill(Color.WHITE);
        whatPerLabel.setTextFill(Color.WHITE);
        isVaccinatedLabel.setTextFill(Color.WHITE);
        useSanitizersLabel.setTextFill(Color.WHITE);
        coverMouthLabel.setTextFill(Color.WHITE);
        stayAtHomeLabel.setTextFill(Color.WHITE);
        tooBigPopulationLabel.setTextFill(Color.RED);
        population0Label.setTextFill(Color.RED);

        appliedLabel.setTranslateX(200);
        appliedLabel.setTranslateY(150);
        appliedLabel.setFont(Font.font(20));

        resetLabel.setTranslateX(200);
        resetLabel.setTranslateY(150);
        resetLabel.setFont(Font.font(20));

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTranslateY(-240);
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 70));

        sirLabel.setTranslateX(-260);
        sirLabel.setTranslateY(-180);
        sirLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        spreadLabel.setTranslateX(-260 + 400);
        spreadLabel.setTranslateY(-180);
        spreadLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        tooBigPopulationLabel.setTranslateX(200);
        tooBigPopulationLabel.setTranslateY(150);
        tooBigPopulationLabel.setFont(Font.font(20));

        population0Label.setTranslateX(200);
        population0Label.setTranslateY(150);
        population0Label.setFont(Font.font(20));

        DropShadow dropShadow = new DropShadow();

        titleLabel.setEffect(dropShadow);
        sirLabel.setEffect(dropShadow);
        spreadLabel.setEffect(dropShadow);

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
        Tooltip lessthan1000 = new Tooltip("Vælg antal mellem 1 - 999");
        Tooltip.install(susceptibleAmount, lessthan1000);
        Tooltip.install(infectedAmount, lessthan1000);
        Tooltip.install(recoveredAmount, lessthan1000);


        // Event til starte simulering og fjerne menu og blur
        runButton.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            root.getChildren().add(showMenu);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();
            if (applyLabelIsActive) {
                menu.getChildren().remove(appliedLabel);
                applyLabelIsActive = false;
            }
        });

        // Event til at anvende og checke indtastede værdier
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
                menu.getChildren().add(appliedLabel);
                applyLabelIsActive = true;
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

        //Events til menuknap
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().add(menu);
            root.getChildren().remove(showMenu);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            sim.pauseSimulation();
            runButton.setText("Continue");
        });

        //Grids
        //Grid til textfield variabler
        GridPane menuLabels = new GridPane();
        menuLabels.setTranslateX(260);
        menuLabels.setTranslateY(240);
        styler.StyleGrid(menuLabels);

        menuLabels.add(susceptibleLabel, 0, 0);
        menuLabels.add(susceptibleAmount, 1, 0);
        menuLabels.add(whatPerLabel,3,0);
        menuLabels.add(infectedLabel, 0, 1);
        menuLabels.add(infectedAmount, 1, 1);
        menuLabels.add(recoveredLabel, 0,2);
        menuLabels.add(recoveredAmount, 1, 2);
        menuLabels.add(isVaccinatedLabel, 3, 1);
        menuLabels.add(useSanitizersLabel, 3, 2);
        menuLabels.add(coverMouthLabel, 3, 3);
        menuLabels.add(stayAtHomeLabel, 3, 4);
        menuLabels.add(vaccinePercent, 4, 1);
        menuLabels.add(sanitizerPercent, 4, 2);
        menuLabels.add(coverMouthPercent, 4, 3);
        menuLabels.add(stayHomePercent, 4, 4);

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

        menu.getChildren().addAll(menuBackground, menuLabels, comboBox, titleLabel, sirLabel, spreadLabel, menuButttonsBottomRight);

        //Tilføj simwindow og menu til root stackpane
        root.getChildren().addAll(simWindow, menu);

        //Load og vis baggrundsbilledet
        visualMap = new Image("city_upscaled.png");
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
                        stringSusceptible.setText("Susceptibles: " + sim.healthCount(Person.health.Susceptible));
                        stringInfected.setText("Infected: " + sim.healthCount(Person.health.Infected));
                        stringRecovered.setText("Recovered: " + sim.healthCount(Person.health.Recovered));
                        stringDead.setText("Dead: " + sim.healthCount(Person.health.Dead));
                    }
                }

                previousTime = currentTime;
            }
        }.start();

        Scene scene = new Scene(root, 1200, 750);

        stage.setScene(scene);
        stage.show();
    }

}
