package dk.aau.cs.a310a;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

        Image DKmap = new Image("DKmap.png");

        ImageView imageView = new ImageView(DKmap);
        imageView.setFitHeight(725);
        imageView.setPreserveRatio(true);

        

        // Der laves layers med stackpane til at placere en knap på billedet.
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(comboBox, imageView);

        StackPane.setAlignment(comboBox, Pos.TOP_RIGHT);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

        /*
        if (t <= 2) {
            t = 0;
        } */

        // TextArea til at udskrive data fra people listen.
        Label personData = new Label();



        // Billedet, knappen og tekstdata tilføjes til programvinduet.
        root.getChildren().add(stackPane);
        root.getChildren().add(personData);
        root.setAlignment(Pos.CENTER);

        final long startNanoTime = System.nanoTime();

        new AnimationTimer() {
            double f = 0.2;
            int i = 0;
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                if (t >= f && i < people.size()) {
                    /* for (Person elem : people) {
                        personData.setText(personData.getText() + "\n" + elem.toString());
                    } */
                    personData.setText(personData.getText() + "\n" + people.get(i));
                    f += 0.2;
                    i += 1;
                }
            }
        }.start();

        Scene scene = new Scene(root, 1200, 725);

        stage.setScene(scene);
        stage.show();


    }

}
