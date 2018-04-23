package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.GUI.GUI;
import dk.aau.cs.a310a.Grid.GridPosition;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Simulator {
    public static Simulator theSimulator;

    //Tid
    public static Clock clock;

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

    private int lastGraphUpdate;

    private ArrayList<GridPosition> houses;
    private ArrayList<GridPosition> workplaces;
    private ArrayList<GridPosition> hospitals;

    public Simulator() {
        //Gør denne simulator globalt tilgængelig
        theSimulator = this;

        //Random number generator
        rand = new Random();

        //Tid
        clock = new Clock();

        //Opret tomme lister af 'Person'
        people = new ArrayList<>();

        simulationHasBeenInitialised = false;
        simulationIsActive = false;
    }

    public boolean importMap(String resourceUrl) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceUrl).getFile());
        return importMap(file);
    }

    public boolean importMap(File map) throws IOException {
        //Load billede der bruges til simulering
        pixelMap = ImageIO.read(map);

        //Opret simuleringsområdet
        HashMap<GridPosition, placeType> oldGrid = grid;
        grid = new HashMap<GridPosition, placeType>();

        ArrayList<GridPosition> oldHouses = houses;
        houses = new ArrayList<>();

        ArrayList<GridPosition> oldWorkplaces = workplaces;
        workplaces = new ArrayList<>();

        ArrayList<GridPosition> oldHospitals = hospitals;
        hospitals = new ArrayList<>();

        for (int x = 0; x < pixelMap.getWidth(); x++) {
            for (int y = 0; y < pixelMap.getHeight(); y++) {
                int argb = pixelMap.getRGB(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb >> 0) & 0xFF;

                GridPosition pos = new GridPosition(x, y);

                //Grass
                if (r == 255 && g == 255 && b == 255) {
                    grid.put(pos, placeType.Grass);
                }

                //Road
                if (r == 128 && g == 128 && b == 128) {
                    grid.put(pos, placeType.Road);
                }

                //House
                if (r == 0 && g == 0 && b == 0) {
                    grid.put(pos, placeType.House);
                    houses.add(pos);
                }

                //Work
                if (r == 0 && g == 0 && b > 240) {
                    grid.put(pos, placeType.Work);
                    workplaces.add(pos);
                }

                //Hospital
                if (r > 240 && g == 0 && b == 0) {
                    grid.put(pos, placeType.Hospital);
                    hospitals.add(pos);
                }
            }
        }

        if (houses.size() < 1) {
            GUI.theGUI.displayMessage(Alert.AlertType.ERROR, "YIKES", "Invalid image", "Image is missing houses.\n" +
                    "Houses are rgb(0,0,0)");
            grid = oldGrid;
            houses = oldHouses;
            workplaces = oldWorkplaces;
            hospitals = oldHospitals;
            return false;
        }

        if (workplaces.size() < 1) {
            GUI.theGUI.displayMessage(Alert.AlertType.ERROR, "YIKES", "Invalid image", "Image is missing workplaces\n" +
                    "Workplaces are rgb(0,0,255)");
            grid = oldGrid;
            houses = oldHouses;
            workplaces = oldWorkplaces;
            hospitals = oldHospitals;
            return false;
        }

        return true;
    }

    public void initialiseSimulation(int susceptibleAmount, int infectedAmount) {
        //ny influenza
        influenzaA = new Influenza(Influenza.influenzaType.A);

        //Nulstil tiden
        clock.resetTime();

        //Nulstril Graf
        GUI.lineChart.resetLineChart();
        lastGraphUpdate = -GUI.lineChart.ticksPerPoint;

        //opret personer
        addPeople(susceptibleAmount, infectedAmount);

        simulationHasBeenInitialised = true;
    }

    public void addPeople(int susceptibles, int infecteds) {
        for (int i = 0; i < susceptibles + infecteds; i++) {
            int randomHouse = rand.nextInt(houses.size());
            int randomWork = rand.nextInt(workplaces.size());

            Person person = new Person(houses.get(randomHouse), workplaces.get(randomWork));

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

            if (clock.getCurrentTick() > lastGraphUpdate + GUI.lineChart.ticksPerPoint) {
                GUI.lineChart.updateLineChart();
                lastGraphUpdate = clock.getCurrentTick();
            }

            clock.tick();
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

    public String getSimulationTime() {
        return clock.getTimeString();
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
    public int healthCount(Person.health health) {
        int count = 0;
        for (Person p : people) {
            if (p.getCurrentHealth() == health) {
                count += 1;
            }
        }
        return count;
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
