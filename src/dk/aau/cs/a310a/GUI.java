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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GUI extends Application {

    //Simulator objekt
    Simulator sim;

    // Billedet i canvas
    BufferedImage cityMap;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() throws IOException {
        cityMap = ImageIO.read(getClass().getResource("/city.png"));
        sim = new Simulator();
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


        //Tilføj knapper til menuen
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


        //labels til beskrivelse af opsætningsbokse
        Label susceptibleLabel = new Label("Susceptible:");
        Label infectedLabel = new Label("Infected:");
        Label recoveredLabel = new Label("Recovered:");

        susceptibleLabel.setTextFill(Color.WHITE);
        infectedLabel.setTextFill(Color.WHITE);
        recoveredLabel.setTextFill(Color.WHITE);

        susceptibleLabel.setTranslateX(-300);
        susceptibleLabel.setTranslateY(-197+50);

        infectedLabel.setTranslateX(-300);
        infectedLabel.setTranslateY(-197+100);

        recoveredLabel.setTranslateX(-300);
        recoveredLabel.setTranslateY(-197+150);


        //Button factory
        Button showMenu = new Button("Menu");
        showMenu.setFont(Font.font(20));
        StackPane.setAlignment(showMenu, Pos.TOP_LEFT);

        Button resetSim = new Button("Reset");
        resetSim.setFont(Font.font(20));
        resetSim.setTranslateX(200);
        resetSim.setTranslateY(260);

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
            root.getChildren().removeAll(runButton, menuRec, comboBox, susceptibleAmount, recoveredAmount, infectedAmount, applySettings, resetSim, susceptibleLabel, recoveredLabel, infectedLabel);
            root.getChildren().add(showMenu);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();
        });

        // Event til at anvende og checke indtastede værdier
        applySettings.setOnMouseClicked(event -> {
            int populationSize = Integer.parseInt(susceptibleAmount.getText());
            if (populationSize > 0 && populationSize < 1000) {
                runButton.setDisable(false);
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
        });


        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();


        // TextArea til at udskrive data fra people listen.
        TextArea personData = new TextArea();
        personData.setFont(Font.font(12));
        personData.setTranslateY(300);

        //Label
        Label countSusceptible = new Label();
        Label countInfected = new Label();
        Label countRecovered = new Label();
        Label stringSusceptible = new Label("Susceptible   ");
        Label stringInfected = new Label("Infected   ");
        Label stringRecovered = new Label("Recovered   ");
        countSusceptible.setText(sim.healthCount(Person.health.Susceptible));
        countInfected.setText(sim.healthCount(Person.health.Infected));
        countRecovered.setText(sim.healthCount(Person.health.Recovered));

        //Grid
        info.setTranslateX(820);
        info.setTranslateY(20);
        info.add(stringSusceptible, 0, 0);
        info.add(countSusceptible, 0, 1);
        info.add(stringInfected, 1, 0);
        info.add(countInfected,1, 1);
        info.add(stringRecovered, 2, 0);
        info.add(countRecovered, 2, 1);

        //Tilføj personer fra starten
        personData.setEditable(false);
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        // HBox, canvas og stackpane tilføjes til programvinduet.
        simWindow.getChildren().addAll(canvas, personData);
        info.setEffect(boxblur);
        simWindow.setEffect(boxblur);
        root.getChildren().addAll(info, simWindow, menuRec, runButton, susceptibleAmount, infectedAmount, recoveredAmount, applySettings, resetSim, susceptibleLabel, recoveredLabel, infectedLabel, comboBox);
        bob.drawBufferedImage(cityMap,0,0,800,600,pw);

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
                    sim.simulate(t);
                    bob.drawBufferedImage(cityMap,0,0,800,600,pw);

                    for (Person p : sim.getPeople()) {
                        Color color = Color.BLACK;
                        switch (p.getCurrentHealth()) {
                            case Susceptible:
                                color = Color.CYAN;
                                break;
                            case Infected:
                                color = Color.RED;
                                break;
                            case Recovered:
                                color = Color.YELLOW;
                                break;
                            case Dead:
                                color = Color.RED;
                                break;
                        }
                        if (p.getCurrentHealth() == Person.health.Dead)
                            bob.drawCross(p.getPosition(), 8, color, pw);
                        else
                            bob.drawCircle(p.getPosition(), 8, color, pw);
                    }
                    personData.setText("");
                    for (Person p : sim.getPeople()) {
                        personData.setText(personData.getText() + "\n " + p);
                        countSusceptible.setText(sim.healthCount(Person.health.Susceptible));
                        countInfected.setText(sim.healthCount(Person.health.Infected));
                        countRecovered.setText(sim.healthCount(Person.health.Recovered));
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
