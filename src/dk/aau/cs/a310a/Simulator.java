package dk.aau.cs.a310a;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    public static Simulator theSimulator;

    //Området der simuleres på
    BufferedImage pixelMap;

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

    List<Vector> homePositions;
    List<Vector> workPositions;
    List<Vector> hospitalPositions;

    public Simulator() throws IOException {
        //Gør denne simulator globalt tilgængelig
        theSimulator = this;

        //Random number generator
        rand = new Random();

        //Load billede der bruges til simulering
        pixelMap = ImageIO.read(getClass().getResource("/city.png"));

        //Lav tomme lister af 'Person'
        people = new ArrayList<>();

        //Find alle huse på kortet
        findBuildings();

        simulationHasBeenInitialised = true;
        simulationIsActive = false;
    }

    void findBuildings() {
        homePositions = new ArrayList<>();
        workPositions = new ArrayList<>();
        hospitalPositions = new ArrayList<>();

        for (int x = 0; x < pixelMap.getWidth(); x++) {
            for (int y = 0; y < pixelMap.getHeight(); y++) {
                int argb = pixelMap.getRGB(x,y);
                int r = (argb>>16)&0xFF;
                int g = (argb>>8)&0xFF;
                int b = (argb>>0)&0xFF;

                Vector housePos = new Vector(x * 20 + 10,y * 20 + 10);

                if (r == 87 && g == 87 && b == 87) {
                    homePositions.add(housePos);
                }

                if (r == 116 && g == 165 && b == 255) {
                    workPositions.add(housePos);
                }

                if (r == 255 && g == 255 && b == 255) {
                    hospitalPositions.add(housePos);
                }
            }
        }
    }

    public void addPeople(int amount, Person.health health) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            int randAge = rand.nextInt(80) + 20;
            int randomHouse = rand.nextInt(homePositions.size());

            Person person = new Person(randAge, health, homePositions.get(randomHouse), homePositions.get(randomHouse));

            //Tilføj person til liste
            people.add(person);
        }
    }

    public String healthCount(Person.health health) {
        int count = 0;
        for(Person p : people) {
            if(p.getCurrentHealth() == health) {
                count += 1;
            }
        }
        String value = String.valueOf(count);
        return value;
    }

    void initialiseSimulation(int susceptibleAmount, int infectedAmount, int recoveredAmount) {
        //lav personer
        addPeople(susceptibleAmount + infectedAmount + recoveredAmount, Person.health.Susceptible);

        influenzaA = new Influenza(Influenza.influenzaType.A);

        //inficer personer
        if (infectedAmount > 0) {
            for (int infected = 0; infected < infectedAmount; infected++) {
                influenzaA.infectPerson(people.get(infected));
            }
        }

        if (recoveredAmount > 0) {
            for (int recovered = infectedAmount; recovered < infectedAmount + susceptibleAmount; recovered++) {
                people.get(recovered).setCurrentHealth(Person.health.Recovered);
            }
        }

        i = 0;
        start = 0;
        end = 1;
    }

    public void startSimulation(){
        if (!simulationHasBeenInitialised){
            //Error message
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

        /*Inficer og opdater variabler til inficering af personer
        //influenzaA.infectPerson(people, start, end);
        if (i < people.size()) {
            i++;
            start++;
            end *= influenzaA.getBaseSpread() + 1;
        }
        */

        //Opdater positionen og helbredet for personen
        for (Person p : people) {
            p.updateMovement();
            p.updateDisease(time);
        }
    }

    public Vector getHospitalPosition()
    {
        if (hospitalPositions.size() > 0) {
            return hospitalPositions.get(rand.nextInt(hospitalPositions.size()));
        }
        else
            return new Vector(0,0);
    }

    public Vector getWorkPosition()
    {
        if (workPositions.size() > 0) {
            return workPositions.get(rand.nextInt(workPositions.size()));
        }
        else
            return new Vector(0,0);
    }

    public List<Person> getPeople() {
        return people;
    }
}
