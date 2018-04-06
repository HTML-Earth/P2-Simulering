package dk.aau.cs.a310a;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class GUI extends Application {

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

        stage.setTitle("WindowTest");

        HBox simWindow = new HBox();
        StackPane root = new StackPane();
        GridPane info = new GridPane();


        // Rektangel hvor menuens knapper placeres og blurring
        Rectangle menuRec = new Rectangle(900, 600, Color.rgb(50, 50, 50, 0.95));
        BoxBlur boxblur = new BoxBlur();
        boxblur.setHeight(5);
        boxblur.setWidth(5);
        boxblur.setIterations(3);


        //Menu - Tilføj knapper
        //Combobox af typen ComboboxItem, objekter af ComboboxItem tilføjes til menuen
        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0)
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

        TextField infectedAmount = new TextField("100");
        infectedAmount.setMaxWidth(80);
        infectedAmount.setTranslateX(-220);
        infectedAmount.setTranslateY(-200+100);

        TextField recoveredAmount = new TextField("100");
        recoveredAmount.setMaxWidth(80);
        recoveredAmount.setTranslateX(-220);
        recoveredAmount.setTranslateY(-200+150);


        // Menu - labels til beskrivelse
        Label susceptibleLabel = new Label("Susceptible:");
        Label infectedLabel = new Label("Infected:");
        Label recoveredLabel = new Label("Recovered:");
        Label appliedLabel = new Label("Population applied!");
        Label resetLabel = new Label("Simulation reset!");

        susceptibleLabel.setTextFill(Color.WHITE);
        infectedLabel.setTextFill(Color.WHITE);
        recoveredLabel.setTextFill(Color.WHITE);
        appliedLabel.setTextFill(Color.LIGHTGREEN);
        resetLabel.setTextFill(Color.ORANGE);

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


        //Button factory
        Button showMenu = new Button("Menu");
        showMenu.setFont(Font.font(20));
        StackPane.setAlignment(showMenu, Pos.TOP_LEFT);

        Button resetSim = new Button("Reset");
        resetSim.setFont(Font.font(20));
        resetSim.setTranslateX(200);
        resetSim.setTranslateY(260);
        resetSim.setDisable(true);

        Button applySettings = new Button("Apply");
        applySettings.setFont(Font.font(20));
        applySettings.setTranslateX(300);
        applySettings.setTranslateY(260);

        Button runButton = new Button("Start");
        runButton.setDisable(true);
        runButton.setFont(Font.font(20));
        runButton.setTranslateX(400);
        runButton.setTranslateY(260);


        // Event til at fjerne menu og blur og derefter starte simulationen
        runButton.setOnMouseClicked(event -> {
            root.getChildren().removeAll(runButton, menuRec, comboBox, susceptibleAmount, recoveredAmount, infectedAmount, applySettings, resetSim, susceptibleLabel, recoveredLabel, infectedLabel, appliedLabel, resetLabel);
            root.getChildren().add(showMenu);
            resetSim.setDisable(false);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();
        });

        // Event til at anvende og checke indtastede værdier
        applySettings.setOnMouseClicked(event -> {
            int populationSize = Integer.parseInt(susceptibleAmount.getText());
            boolean alreadyApplied = true;
            if (populationSize > 0 && populationSize < 1000) {
                runButton.setDisable(false);
            }

            if (alreadyApplied == true) {
                root.getChildren().remove(resetLabel);
                root.getChildren().add(appliedLabel);
            }

        });

        //Events til menuknap
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().addAll(menuRec, runButton, comboBox, susceptibleAmount, recoveredAmount, infectedAmount, susceptibleLabel, recoveredLabel, infectedLabel, applySettings, resetSim);
            root.getChildren().remove(showMenu);
            runButton.setDisable(true);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            sim.pauseSimulation();
        });

        //Event til at genstarte simulation
        resetSim.setOnMouseClicked(event -> {
            sim.stopSimulation();
            resetSim.setDisable(true);
            root.getChildren().remove(appliedLabel);
            root.getChildren().add(resetLabel);
        });


        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();

        //Livestatistikker
        //Label
        Label stringSusceptible = new Label("Susceptible: " + sim.healthCount(Person.health.Susceptible));
        Label stringInfected = new Label("Infected: " + sim.healthCount(Person.health.Infected));
        Label stringRecovered = new Label("Recovered: " + sim.healthCount(Person.health.Recovered));
        Label stringDead = new Label("Dead: " + sim.healthCount(Person.health.Dead));
        Label stringEpidemic = new Label("Chance of epidemic" + "\n" + influenzaA.calculateR0(1,1.0));

        //Grid
        info.setTranslateX(820);
        info.setTranslateY(20);
        info.add(stringSusceptible, 0, 0);
        info.add(stringInfected, 0, 1);
        info.add(stringRecovered, 0, 2);
        info.add(stringDead, 0, 3);
        info.add(stringEpidemic, 0, 4);

        //Information om personer
        //TextArea til at udskrive data fra people listen.
        TextArea personData = new TextArea();
        personData.setFont(Font.font(12));
        personData.setTranslateY(300);

        //Tilføj personer fra starten
        personData.setEditable(false);
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        // HBox, canvas og stackpane tilføjes til programvinduet.
        simWindow.getChildren().addAll(canvas, personData);
        info.setEffect(boxblur);
        simWindow.setEffect(boxblur);

        root.getChildren().addAll(info, simWindow, menuRec, runButton, susceptibleAmount,
                infectedAmount, recoveredAmount, applySettings, resetSim,
                susceptibleLabel, recoveredLabel, infectedLabel, comboBox);

        //Load og vis baggrundsbilledet
        visualMap = new Image("city_upscaled.png");
        gc.drawImage(visualMap,0,0,800,600);

        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            double updateTime = 0;
            Random rand;

            public void handle(long currentNanoTime) {
                rand = new Random();
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                if (!sim.isSimulationActive())
                    return;

                if (t >= updateTime) {
                    //Opdater simulering
                    sim.simulate(t);

                    //Vis baggrund (hvilket overskriver forrige frame
                    gc.drawImage(visualMap,0,0,800,600);

                    //Tegn alle personer
                    for (Person p : sim.getPeople()) {
                        bob.drawPerson(p.getPosition(),p.getCurrentHealth(),gc);
                    }

                    //Reset personData tekst
                    personData.setText("");

                    //Skriv antal personer i hver gruppe
                    for (Person p : sim.getPeople()) {
                        personData.setText(personData.getText() + "\n " + p);
                        stringSusceptible.setText("Susceptibles: " + sim.healthCount(Person.health.Susceptible));
                        stringInfected.setText("Infected: " + sim.healthCount(Person.health.Infected));
                        stringRecovered.setText("Recovered: " + sim.healthCount(Person.health.Recovered));
                        stringDead.setText("Dead: " + sim.healthCount(Person.health.Dead));
                    }

                    //60 fps
                    updateTime += 0.017;
                }
            }
        }.start();

        Scene scene = new Scene(root, 1200, 750);

        stage.setScene(scene);
        stage.show();
    }

}
