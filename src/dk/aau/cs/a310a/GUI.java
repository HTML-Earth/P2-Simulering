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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class GUI extends Application {


    //booleans
    boolean isApplyLabelRemoved = false;

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

        //Infoboks med Livestatistikker i hjørnet
        GridPane info = new GridPane();

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

        //Information om personer
        Label personData = new Label();
        personData.setFont(Font.font(12));
        personData.setTranslateY(300);

        //Tilføj personer fra starten
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        //info og persondata tilføjes til sidepanel
        sidePanel.getChildren().addAll(info,personData);

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
        TextField susceptibleAmount = new TextField("100");
        susceptibleAmount.setMaxWidth(80);
        susceptibleAmount.setTranslateX(-220);
        susceptibleAmount.setTranslateY(-200+50);

        TextField infectedAmount = new TextField("1");
        infectedAmount.setMaxWidth(80);
        infectedAmount.setTranslateX(-220);
        infectedAmount.setTranslateY(-200+100);

        TextField recoveredAmount = new TextField("0");
        recoveredAmount.setMaxWidth(80);
        recoveredAmount.setTranslateX(-220);
        recoveredAmount.setTranslateY(-200+150);

        // Menu - labels til beskrivelse
        Label susceptibleLabel = new Label("Susceptible:");
        Label infectedLabel = new Label("Infected:");
        Label recoveredLabel = new Label("Recovered:");
        Label appliedLabel = new Label("Population applied!");
        Label resetLabel = new Label("Simulation reset!");
        Label titleLabel = new Label("Super Awesome Title");

        susceptibleLabel.setTextFill(Color.WHITE);
        infectedLabel.setTextFill(Color.WHITE);
        recoveredLabel.setTextFill(Color.WHITE);
        appliedLabel.setTextFill(Color.LIGHTGREEN);
        resetLabel.setTextFill(Color.ORANGE);
        titleLabel.setTextFill(Color.WHITE);

        susceptibleLabel.setTranslateX(-300);
        susceptibleLabel.setTranslateY(-197+50);

        infectedLabel.setTranslateX(-300);
        infectedLabel.setTranslateY(-197+100);

        recoveredLabel.setTranslateX(-300);
        recoveredLabel.setTranslateY(-197+150);

        appliedLabel.setTranslateX(200);
        appliedLabel.setTranslateY(150);
        appliedLabel.setFont(Font.font(20));

        resetLabel.setTranslateX(200);
        resetLabel.setTranslateY(150);
        resetLabel.setFont(Font.font(20));

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTranslateY(-240);
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 70));

        DropShadow dropShadow = new DropShadow();

        titleLabel.setEffect(dropShadow);

        //Button factory
        Button showMenu = new Button("Menu");
        showMenu.setFont(Font.font(20));
        StackPane.setAlignment(showMenu, Pos.TOP_LEFT);

        Button resetSim = new Button("Reset");
        resetSim.setFont(Font.font(20));
        resetSim.setDisable(true);

        Button applySettings = new Button("Apply");
        applySettings.setFont(Font.font(20));

        Button runButton = new Button("Start");
        runButton.setDisable(true);
        runButton.setFont(Font.font(20));

        // Event til starte simulering og fjerne menu og blur
        runButton.setOnMouseClicked(event -> {
            root.getChildren().remove(menu);
            root.getChildren().add(showMenu);
            resetSim.setDisable(false);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();
            isApplyLabelRemoved = false;
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
            }
            if (!isApplyLabelRemoved) {
                menu.getChildren().add(appliedLabel);
                isApplyLabelRemoved = true;
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

        //Event til at genstarte simulation
        resetSim.setOnMouseClicked(event -> {
            sim.stopSimulation();
            gc.drawImage(visualMap,0,0,800,600);
            resetSim.setDisable(true);
            menu.getChildren().remove(appliedLabel);
            menu.getChildren().add(resetLabel);
            runButton.setText("Start");
        });

        //Grids
        GridPane menuButttonsBottomRight = new GridPane();
        menuButttonsBottomRight.add(applySettings, 0, 0);
        menuButttonsBottomRight.add(runButton, 1, 0);
        menuButttonsBottomRight.setTranslateX(800);
        menuButttonsBottomRight.setTranslateY(600);

        menu.getChildren().addAll(menuBackground, susceptibleAmount, infectedAmount, recoveredAmount,susceptibleLabel, infectedLabel, recoveredLabel, comboBox, titleLabel, menuButttonsBottomRight);

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
                        bob.drawPerson(p.getPosition(),p.getCurrentHealth(),gc);
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
