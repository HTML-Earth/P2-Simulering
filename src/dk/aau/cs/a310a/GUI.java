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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class GUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    public List<Person> addSusceptible(List<Person> people, int n) {
        Random rand = new Random();
        for(int i = 0; i < n; i++) {
            int randInt = rand.nextInt(80) + 20;
            Person person = new Person(randInt, Person.health.Susceptible);
            people.add(person);
        }
        return people;
    }

    List<Person> people;

    public void init()
    {
        //Lav tom liste af 'Person' og lav 100 'Person' med Susceptible
        people = new ArrayList<>();
        people = addSusceptible(people, 100);
        Person infected = new Person(25, Person.health.Infected);
        people.add(infected);

        //Løb gennem Listen og print dem
        for(Person elem : people) {
            System.out.println(elem);
        }
    }

    public void start(Stage stage) {

        stage.setTitle("WindowTest");

        HBox root = new HBox();

        //Combobox med options
        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0)
        );
        comboBox.setValue(new ComboItem("Alle hoster i ærmet", 0.0));

        Image DKmap = new Image("DKmap.png");

        //Canvas og Bob Ross til at tegne på det
        Canvas canvas = new Canvas(900,750);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pw = gc.getPixelWriter();
        BobRoss bob = new BobRoss();

        // TextArea til at udskrive data fra people listen.
        Label personData = new Label();

        // Canvas og tekstdata tilføjes til programvinduet.
        root.getChildren().add(canvas);
        root.getChildren().add(personData);
        root.setAlignment(Pos.CENTER);

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            double f = 0.2;
            int i = 0;
            int circleX = 100;
            int circleY = 100;
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                gc.drawImage(DKmap, 0,0,900,750);
                bob.drawCircle(circleX*2,circleY*2, 10 + i, Color.RED, pw);

                if (t >= f && i < people.size()) {
                    circleX++;
                    circleY++;

                    if (i < people.size()) {
                        personData.setText(personData.getText() + "\n" + people.get(i));
                        f += 0.2;
                        i++;
                    }
                }
            }
        }.start();

        Scene scene = new Scene(root, 1200, 725);

        stage.setScene(scene);
        stage.show();


    }

}
