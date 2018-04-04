package dk.aau.cs.a310a;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator
{
    Influenza influenzaA;
    List<Person> people;
    Random rand;
    int i;
    int start;
    int end;

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
        people = addSusceptible(people, 1000);


        influenzaA = new Influenza(Influenza.influenzaType.A);

        i = 0;
        start = 0;
        end = 1;
    }

    public void simulate()
    {
        for (Person p : people)
        {
            p.setTarget(new Vector(p.getPosition().x + rand.nextDouble()*400-200,p.getPosition().y + rand.nextDouble()*400-200));
            p.updateMovement();
        }

        influenzaA.infectPerson(people, start, end);

        if (i < people.size()) {
            i++;
            start++;
            end *= influenzaA.getBaseSpread() + 1;
        }
    }

    public List<Person> getPeople()
    {
        return people;
    }
}
