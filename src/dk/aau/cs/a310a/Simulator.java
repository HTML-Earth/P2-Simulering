package dk.aau.cs.a310a;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    public static Simulator theSimulator;

    //Sygdom
    Influenza influenzaA;

    //Lister over personer
    public List<Person> people;

    //RNG
    public Random rand;

    //Simuleringsvariabler
    int i;
    int start;
    int end;
    boolean simulationHasBeenInitialised;
    boolean simulationIsActive;

    public Simulator() {
        //Gør denne simulator globalt tilgængelig
        theSimulator = this;

        //Random number generator
        rand = new Random();

        //Lav tomme lister af 'Person'
        people = new ArrayList<>();

        initialiseSimulation();

        simulationHasBeenInitialised = true;
        simulationIsActive = false;
    }

    public void addPeople(int amount, Person.health health) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            int randAge = rand.nextInt(80) + 20;
            double randX = rand.nextDouble() * 800 + 50;
            double randY = rand.nextDouble() * 650 + 50;
            Person person = new Person(randAge, health, new Vector(randX, randY));

            //Tilføj person til liste
            people.add(person);
        }
    }

    void initialiseSimulation() {
        //lav 100 'Person' med Susceptible
        addPeople(300, Person.health.Susceptible);

        influenzaA = new Influenza(Influenza.influenzaType.A);

        //Inficer 1 person
        influenzaA.infectPerson(people.get(0));

        i = 0;
        start = 0;
        end = 1;
    }

    public void startSimulation(){
        if (!simulationHasBeenInitialised){
            initialiseSimulation();
        }
        simulationIsActive = true;
    }

    public void pauseSimulation() {
        simulationIsActive = false;
    }

    public void stopSimulation() {
        simulationIsActive = false;
        simulationHasBeenInitialised = false;
        for (Person p : people){
            p = null;
        }
        people.clear();
    }

    public boolean isSimulationActive () {
        return simulationIsActive;
    }

    public void simulate(double time) {
        if (!simulationIsActive)
            return;

        //Inficer og opdater variabler til inficering af personer
        //influenzaA.infectPerson(people, start, end);

        if (i < people.size()) {
            i++;
            start++;
            end *= influenzaA.getBaseSpread() + 1;
        }

        //Opdater positionen og sygdommen for personen
        for (Person p : people) {
            p.updateMovement();
            p.updateDisease(time);
        }
    }

    public List<Person> getPeople() {
        return people;
    }
}
