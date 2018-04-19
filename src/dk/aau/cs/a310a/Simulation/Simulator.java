package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.Grid.GridPosition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Simulator {
    public static Simulator theSimulator;

    public enum placeType {House, Work, Hospital, Road, Grass};

    //Området der simuleres på
    private HashMap<GridPosition, placeType> grid;

    //Billede der definerer området
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

        //Opret simuleringsområdet
        grid = new HashMap<GridPosition, placeType>();

        //Load billede der bruges til simulering
        pixelMap = ImageIO.read(getClass().getResource("/city.png"));

        //Opret tomme lister af 'Person'
        people = new ArrayList<>();

        //Find alle huse på kortet
        scanMap();

        simulationHasBeenInitialised = false;
        simulationIsActive = false;
    }

    void scanMap() {
        houses = new ArrayList<>();
        workplaces = new ArrayList<>();
        hospitals = new ArrayList<>();

        for (int x = 0; x < pixelMap.getWidth(); x++) {
            for (int y = 0; y < pixelMap.getHeight(); y++) {
                int argb = pixelMap.getRGB(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb >> 0) & 0xFF;

                GridPosition pos = new GridPosition(x, y);

                //Grass
                if (r == 192 && g == 224 && b == 150) {
                    grid.put(pos, placeType.Grass);
                }

                //Road
                if (r == 162 && g == 160 && b == 160) {
                    grid.put(pos, placeType.Road);
                }

                //House
                if (r == 87 && g == 87 && b == 87) {
                    grid.put(pos, placeType.House);
                    houses.add(pos);
                }

                //Work
                if (r == 116 && g == 165 && b == 255) {
                    grid.put(pos, placeType.Work);
                    workplaces.add(pos);
                }

                //Hospital
                if (r == 255 && g == 255 && b == 255) {
                    grid.put(pos, placeType.Hospital);
                    hospitals.add(pos);
                }
            }
        }
    }

    public void initialiseSimulation(int susceptibleAmount, int infectedAmount) {
        //ny influenza
        influenzaA = new Influenza(Influenza.influenzaType.A);

        //opret personer
        addPeople(susceptibleAmount, infectedAmount);

        simulationHasBeenInitialised = true;
    }

    public void addPeople(int susceptibles, int infecteds) {
        for (int i = 0; i < susceptibles + infecteds; i++) {
            int randomHouse = rand.nextInt(houses.size());

            Person person = new Person(houses.get(randomHouse));

            if (i < infecteds)
                person.infect(influenzaA);

            //Tilføj person til liste
            people.add(person);
        }
    }

    public void startSimulation() {
        if (!simulationHasBeenInitialised) {
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
        for (Person p : people) {
            p = null;
        }
        people.clear();
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

    public boolean isSimulationActive() {
        return simulationIsActive;
    }

    public double getTickTime() {
        return tickTime;
    }

    public String getR0(double beta, double gamma) {
        if (influenzaA != null)
            return influenzaA.calculateR0(beta, gamma);
        else
            return "";
    }

    public List<Person> getPeople() {
        return people;
    }

    //Antal personer i helbredsgruppen
    public String healthCount(Person.health health) {
        int count = 0;
        for (Person p : people) {
            if (p.getCurrentHealth() == health) {
                count += 1;
            }
        }
        String value = String.valueOf(count);
        return value;
    }

    //Er det græs, hus, arbejde eller hospital
    public placeType getPlaceType(GridPosition pos) {
        return grid.get(pos);
    }

    public List<GridPosition> getHouses() {
        return houses;
    }

    public List<GridPosition> getHospitals() {
        return hospitals;
    }

    public List<GridPosition> getWorkplaces() {
        return workplaces;
    }
}
