package dk.aau.cs.a310a;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Random;

public class GUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
        Random rand = new Random();
        System.out.println("Hello world!");
        System.out.println("xd");
        System.out.println("Rynke was here");
        System.out.println("lmao");
        System.out.println("hah");
        System.out.println("Hello flu!");

        for(int i = 0; i < 100; i ++) {
            int randInt = rand.nextInt();
            Person person = new Person(randInt, Person.health.isSusceptible);
        }
    }

    public void start(Stage stage) {
        stage.setTitle("WindowTest");

        HBox root = new HBox();

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);

        stage.show();

    }

}
