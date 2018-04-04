package dk.aau.cs.a310a;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class GUI extends Application {

    Simulator sim;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void init()
    {
        sim = new Simulator();
    }

    public void start(Stage stage) {

        stage.setTitle("WindowTest");

        HBox root = new HBox();

        //Combobox af typen ComboboxItem, objekter af ComboboxItem tilføjes til menuen
        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0)
        );
        //Standard value til combobox
        comboBox.setValue(new ComboItem("Alle hoster i ærmet", 0.0));

        Image DKmap = new Image("DKmap.png");

        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(900,750);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();

        // TextArea til at udskrive data fra people listen.
        Label personData = new Label();
        personData.setFont(Font.font(12));

        // Canvas og tekstdata tilføjes til programvinduet.
        root.getChildren().add(canvas);
        root.getChildren().add(personData);

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            double updateTime = 0;
            int i = 0;
            Random rand;
            public void handle(long currentNanoTime) {
                rand = new Random();
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                sim.simulate();

                if (t >= updateTime) {
                    gc.drawImage(DKmap, 0,0,900,750);

                    for (Person p : sim.getPeople())
                    {
                        Color color = Color.BLACK;
                        switch (p.getCurrentHealth())
                        {
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

                    if (i < sim.getPeople().size()) {
                        personData.setText(personData.getText() + "\n " + sim.getPeople().get(i));
                        i++;
                    }

                    updateTime += 0.1;
                }
            }
        }.start();

        Scene scene = new Scene(root, 1200, 725);

        stage.setScene(scene);
        stage.show();
    }

}
