package dk.aau.cs.a310a;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
    
    public void init()
    {
        Random rand = new Random();
        List<Person> people = new ArrayList<>();

        //Tilføj 100 'susceptibles' med en tilfældig alder(20, 100) til Arraylist
        for(int i = 0; i < 100; i ++) {
            int randInt = rand.nextInt(80) + 20;
            Person person = new Person(randInt, Person.health.Susceptible);
            people.add(person);
        }

        //Løb gennem Listen og print dem
        for(Person elem : people) {
            System.out.println(elem);
        }
    }

    public void start(Stage stage) {
        stage.setTitle("WindowTest");

        HBox root = new HBox();

        Scene scene = new Scene(root, 800, 400);

        final ComboBox comboBox = new ComboBox();

        comboBox.getItems().addAll(
                "Alle hoster i ærmet",
                "Ingen hoster i ærmet",
                "Custom value"
        );
        root.getChildren().add(comboBox);

        stage.setScene(scene);
        stage.show();

    }

}
