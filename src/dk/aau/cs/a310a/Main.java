package dk.aau.cs.a310a;

import java.util.Random;

import static javafx.application.Application.launch;

public class Main
{
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
}
