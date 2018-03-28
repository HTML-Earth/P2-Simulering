package dk.aau.cs.a310a;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    
    public void init()
    {
        //Lav tom liste af 'Person' og lav 100 'Person' med Susceptible
        List<Person> people = new ArrayList<>();
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

        Image DKmap = new Image("DKmap.png");

        ImageView imageView = new ImageView(DKmap);
        imageView.setX(50);
        imageView.setY(25);
        imageView.setFitHeight(725);
        imageView.setFitWidth(900);
        imageView.setPreserveRatio(true);

        HBox root = new HBox();

        root.getChildren().add(imageView);

        Scene scene = new Scene(root, 800, 400);

        final ComboBox comboBox = new ComboBox();

        comboBox.getItems().addAll(
                "Alle hoster i ærmet",
                "Ingen hoster i ærmet",
                "Custom value"
        );
        comboBox.setValue("Alle hoster i ærmet");
        root.getChildren().add(comboBox);

        stage.setScene(scene);
        stage.show();

    }

}
