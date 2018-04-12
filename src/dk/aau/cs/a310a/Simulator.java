package dk.aau.cs.a310a;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulator {
    public static Simulator theSimulator;

    public enum placeType {House, Work, Hospital};

    //Området der simuleres på
    private BufferedImage pixelMap;

    //Sygdom
    private Influenza influenzaA;

    //Lister over personer
    public ArrayList<Person> people;

    //RNG
    public Random rand;

    //Simuleringsvariabler
    private boolean simulationHasBeenInitialised;
    private boolean simulationIsActive;

    private double tickTime = 0.2;
    private double lastTick = 0;

    private ArrayList<GridPosition> houses;
    private ArrayList<GridPosition> workplaces;
    private ArrayList<GridPosition> hospitals;

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

        simulationHasBeenInitialised = false;
        simulationIsActive = false;
    }

    void findBuildings() {
        houses = new ArrayList<>();
        workplaces = new ArrayList<>();
        hospitals = new ArrayList<>();

        for (int x = 0; x < pixelMap.getWidth(); x++) {
            for (int y = 0; y < pixelMap.getHeight(); y++) {
                int argb = pixelMap.getRGB(x,y);
                int r = (argb>>16)&0xFF;
                int g = (argb>>8)&0xFF;
                int b = (argb>>0)&0xFF;

                GridPosition pos = new GridPosition(x,y);

                if (r == 87 && g == 87 && b == 87) {
                    houses.add(pos);
                }

                if (r == 116 && g == 165 && b == 255) {
                    workplaces.add(pos);
                }

                if (r == 255 && g == 255 && b == 255) {
                    hospitals.add(pos);
                }
            }
        }
    }

    public void addPeople(int amount, Person.health health) {
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            int randAge = rand.nextInt(80) + 20;
            int randomHouse = rand.nextInt(houses.size());

            Person person = new Person(randAge, health, houses.get(randomHouse));

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
            for (int recovered = infectedAmount; recovered < infectedAmount + recoveredAmount; recovered++) {
                people.get(recovered).setCurrentHealth(Person.health.Recovered);
            }
        }

        simulationHasBeenInitialised = true;
    }

    public void startSimulation(){
        if (!simulationHasBeenInitialised){
            System.out.println("Please initialise first");
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

    public double getTickTime () {
        return tickTime;
    }

    public void simulate(double currentTime, double deltaTime) {
        if (!simulationIsActive)
            return;

        if (currentTime > lastTick + tickTime) {
            //Opdater positionen og helbredet for personen
            for (Person p : people) {
                p.updateDisease(currentTime);
                p.updateDestination();
                p.updateMovement();
            }
            lastTick = currentTime;
        }

        for (Person p : people) {
            //opdater skærmpositioner
            p.updateScreenPosition(currentTime - lastTick);
        }
    }

    public List<GridPosition> getHouses()
    {
        return houses;
    }

    public List<GridPosition> getHospitals()
    {
        return hospitals;
    }

    public List<GridPosition> getWorkplaces()
    {
        return workplaces;
    }

    public List<Person> getPeople() {
        return people;
    }
}
