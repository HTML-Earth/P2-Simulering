package dk.aau.cs.a310a;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    //Sygdom
    Influenza influenzaA;

    //Lister over personer
    List<Person> people;
    List<Person> susceptible;
    List<Person> infected;
    List<Person> recovered;

    //RNG
    Random rand;

    //Simuleringsvariabler
    int i;
    int start;
    int end;
    boolean simulationIsActive;

    public Simulator() {
        //Random number generator
        rand = new Random();

        //Lav tomme lister af 'Person'
        people = new ArrayList<>();
        susceptible = new ArrayList<>();
        infected = new ArrayList<>();
        recovered = new ArrayList<>();

        //lav 100 'Person' med Susceptible
        addPeople(100, Person.health.Susceptible);

        influenzaA = new Influenza(Influenza.influenzaType.A);

        i = 0;
        start = 0;
        end = 1;

        simulationIsActive = false;
    }

    public void addPeople(int amount, Person.health health) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            int randAge = rand.nextInt(80) + 20;
            double randX = rand.nextDouble() * 800 + 50;
            double randY = rand.nextDouble() * 650 + 50;
            Person person = new Person(randAge, health, new Vector(randX, randY));

            //Tilføj person til generel liste
            people.add(person);

            //Tilføj person til specifik liste
            switch (health) {
                case Susceptible:
                    susceptible.add(person);
                    break;
                case Infected:
                    infected.add(person);
                    break;
                case Recovered:
                    recovered.add(person);
                    break;
            }
        }
    }

    public void startSimulation(){
        simulationIsActive = true;
    }

    public void simulate() {
        if (!simulationIsActive)
            return;

        //Inficer og opdater variabler til inficering af personer
        influenzaA.infectPerson(people, start, end);

        if (i < people.size()) {
            i++;
            start++;
            end *= influenzaA.getBaseSpread() + 1;
        }

        //Opdater positionen for prikken for personen
        for (Person p : people) {
            p.setTarget(new Vector(p.getPosition().x + rand.nextDouble() * 400 - 200, p.getPosition().y + rand.nextDouble() * 400 - 200));
            p.updateMovement();
        }
    }

    public List<Person> getPeople() {
        return people;
    }
}
