package dk.aau.cs.a310a;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator
{
    List<Person> people;
    Random rand;

    public List<Person> addSusceptible(List<Person> people, int n) {
        Random rand = new Random();
        for(int i = 0; i < n; i++) {
            int randAge = rand.nextInt(80) + 20;
            double randX = rand.nextDouble()*200 + 400;
            double randY = rand.nextDouble()*200 + 400;
            Person person = new Person(randAge, Person.health.Susceptible, new Vector(randX,randY));
            people.add(person);
        }
        return people;
    }

    public Simulator()
    {
        //Random number generator
        rand = new Random();

        //Lav tom liste af 'Person' og lav 100 'Person' med Susceptible
        people = new ArrayList<>();
        people = addSusceptible(people, 100);

        //Løb gennem Listen og print dem
        for(Person elem : people) {
            System.out.println(elem);
        }
    }

    public void simulate()
    {
        for (Person p : people)
        {
            p.setTarget(new Vector(p.getPosition().x + rand.nextDouble()*400-200,p.getPosition().y + rand.nextDouble()*400-200));
            p.updateMovement();
        }
    }

    public List<Person> getPeople()
    {
        return people;
    }
}
