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



        // Rectangle og blur til menu
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
        Button resetSim = new Button("Reset");
        Button applySettings = new Button("Apply");
        Button runButton = new Button("Start");

        // Tilføj knap til at starte program
        runButton.setDisable(true);
        runButton.setFont(Font.font(20));
        runButton.setTranslateX(400);
        runButton.setTranslateY(260);
        runButton.setOnMouseClicked(event -> {
            root.getChildren().remove(runButton);
            root.getChildren().remove(menuRec);
            root.getChildren().remove(comboBox);
            root.getChildren().remove(susceptibleAmount);
            root.getChildren().remove(applySettings);
            root.getChildren().remove(resetSim);
            root.getChildren().add(showMenu);
            simWindow.setEffect(null);
            info.setEffect(null);
            sim.startSimulation();
        });

        // event til Apply og Check
        applySettings.setFont(Font.font(20));
        applySettings.setTranslateX(300);
        applySettings.setTranslateY(260);
        applySettings.setOnMouseClicked(event -> {
            
            int populationSize = Integer.parseInt(susceptibleAmount.getText());
            if (populationSize > 0 && populationSize < 1000) {
                runButton.setDisable(false);

            }
        });

        //Events til menuknap
        showMenu.setFont(Font.font(20));
        StackPane.setAlignment(showMenu, Pos.TOP_LEFT);
        showMenu.setOnMouseClicked(event -> {
            root.getChildren().addAll(menuRec, runButton, comboBox, susceptibleAmount, applySettings, resetSim);
            root.getChildren().remove(showMenu);
            runButton.setDisable(true);
            simWindow.setEffect(boxblur);
            info.setEffect(boxblur);
            sim.pauseSimulation();
        });

        //Knap til at genstarte simulation
        resetSim.setFont(Font.font(20));
        resetSim.setTranslateX(200);
        resetSim.setTranslateY(260);
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
        countSusceptible.setText(sim.healthCount(Person.health.Susceptible));

        //Grid
        info.setTranslateX(920);
        info.setTranslateY(20);
        info.add(countSusceptible, 0, 0);

        //Tilføj personer fra starten
        personData.setEditable(false);
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        // HBox, canvas og stackpane tilføjes til programvinduet.
        simWindow.getChildren().add(canvas);
        simWindow.getChildren().add(personData);
        info.setEffect(boxblur);

        simWindow.setEffect(boxblur);

        root.getChildren().add(info);
        root.getChildren().add(simWindow);
        root.getChildren().add(menuRec);
        root.getChildren().add(runButton);
        root.getChildren().add(susceptibleAmount);
        root.getChildren().add(infectedAmount);
        root.getChildren().add(recoveredAmount);
        root.getChildren().add(applySettings);
        root.getChildren().add(resetSim);
        root.getChildren().add(susceptibleLabel);
        root.getChildren().add(infectedLabel);
        root.getChildren().add(recoveredLabel);
        root.getChildren().add(comboBox);

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
