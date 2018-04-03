package dk.aau.cs.a310a;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

        HBox root = new HBox();

        Image DKmap = new Image("DKmap.png");

        ImageView imageView = new ImageView(DKmap);
        imageView.setFitHeight(725);
        imageView.setPreserveRatio(true);

        //TODO: If statement der tager værdi ud fra hvad der er valgt i combobox
        //Hashmap til værdier for forbyggelse (1.0 hvor smitten er 100%)
        Map<String, Double> preventionVariable = new HashMap<>();
        preventionVariable.put("Alle hoster i ærmet", 0.0);
        preventionVariable.put("Ingen hoster i ærmet", 1.0);

        Double alleHosterValue = preventionVariable.get("Alle hoster i ærmet");

        final ComboBox<ComboItem> comboBox = new ComboBox<>();

        comboBox.getItems().addAll(
                new ComboItem("Alle hoster i ærmet", 0.0),
                new ComboItem("Ingen hoster i ærmet", 1.0),
                );
        comboBox.setValue(new ComboItem("Alle hoster i ærmet", 0.0));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, comboBox);

        StackPane.setAlignment(comboBox, Pos.TOP_RIGHT);
        StackPane.setAlignment(imageView, Pos.TOP_RIGHT);

        root.getChildren().add(stackPane);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 1200, 725);

        stage.setScene(scene);
        stage.show();

    }

}
