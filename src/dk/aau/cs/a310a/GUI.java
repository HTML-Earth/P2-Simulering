package dk.aau.cs.a310a;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class GUI extends Application {

    Simulator sim;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() {
        sim = new Simulator();
    }

    public void start(Stage stage) {

        stage.setTitle("WindowTest");

        HBox simWindow = new HBox();
        StackPane root = new StackPane();

        // Rectangle til menu
        Rectangle menuRec = new Rectangle(900, 600, Color.rgb(50, 50, 50, 0.95));


        // Tilføj knapper til menu
        Button runButton = new Button("Start");
        runButton.setFont(Font.font(20));
        runButton.setTranslateX(400);
        runButton.setTranslateY(260);
        runButton.setOnMouseClicked(event -> {
            root.getChildren().remove(runButton);
            root.getChildren().remove(menuRec);
        });


        //Combobox af typen ComboboxItem, objekter af ComboboxItem tilføjes til menuen
        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0)
        );
        //Standard value til combobox
        comboBox.setValue(new ComboItem("Alle hoster i ærmet", 0.0));

        // Billedet i canvas
        Image DKmap = new Image("DKmap.png");

        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(900, 750);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();

        // TextArea til at udskrive data fra people listen.
        TextArea personData = new TextArea();
        personData.setFont(Font.font(12));

        //Tilføj personer fra starten
        personData.setEditable(false);
        for (Person p : sim.getPeople()) {
            personData.setText(personData.getText() + "\n " + p);
        }

        // HBox, canvas og stackpane tilføjes til programvinduet.
        simWindow.getChildren().add(canvas);
        simWindow.getChildren().add(personData);
        root.getChildren().add(simWindow);
        root.getChildren().add(menuRec);
        root.getChildren().add(runButton);


        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            double updateTime = 0;
            Random rand;

            public void handle(long currentNanoTime) {
                rand = new Random();
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                if (t >= updateTime) {
                    gc.drawImage(DKmap, 0, 0, 900, 750);
                    sim.simulate();
                    for (Person p : sim.getPeople()) {
                        Color color = Color.BLACK;
                        switch (p.getCurrentHealth()) {
                            case Susceptible:
                                color = Color.YELLOW;
                                break;
                            case Infected:
                                color = Color.LIGHTGREEN;
                                break;
                            case Recovered:
                                color = Color.RED;
                                break;
                        }
                        bob.drawCircle(p.getPosition(), 10, color, pw);
                    }
                    personData.setText("");
                    for (Person p : sim.getPeople()) {
                        personData.setText(personData.getText() + "\n " + p);
                    }
                    updateTime += 5;
                }
            }
        }.start();

        Scene scene = new Scene(root, 1200, 725);

        stage.setScene(scene);
        stage.show();
    }

}
