package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.GUI.GUI;
import dk.aau.cs.a310a.Grid.GridPosition;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    private Influenza influenza;

    //Lister over personer
    public ArrayList<Person> people;

    //RNG
    public Random rand;

    public static int maxDaysToSimulate = 7;

    //Simuleringsvariabler
    private boolean simulationHasBeenInitialised;
    private boolean simulationIsActive;

    private boolean newDay = true;

    private double slowTickTime = 0.05;
    private double fastTickTime = 0.001;
    private double tickTime = slowTickTime;
    private double lastTick = 0;

    private int lastGraphUpdate;

    private ArrayList<GridPosition> houses;
    private ArrayList<GridPosition> workplaces;
    private ArrayList<GridPosition> hospitals;

    private ArrayList<Snapshot> snapshots;

    public Simulator() {
        //Gør denne simulator globalt tilgængelig
        theSimulator = this;

        //Random number generator
        rand = new Random();

        //Tid
        clock = new Clock(this);

        //Opret tomme lister af 'Person'
        people = new ArrayList<>();

        simulationHasBeenInitialised = false;
        simulationIsActive = false;

        snapshots = new ArrayList<>();
    }

    public void vaccinatePeople(int vaccinePercent, int infectedAmount) {
        //Tilføj procent variabler til personer
        //sætter antal procent som er vaccineret, antallet er procent og svarer til antal personer pr. hundrede, personerne er valgt tilfældigt
        //TODO: måske while loop?
        for (int i = 0; i < (vaccinePercent * (people.size() - infectedAmount) / 100); i++) {
            int randPerson = rand.nextInt(people.size());
            if (!people.get(randPerson).getVaccinated() && people.get(randPerson).getCurrentHealth() != Person.health.Infected) {
                people.get(randPerson).setVaccinated(true);
            } else {
                i--;
            }
        }
    }

    public void handsanitizePeople(int useHandsanitizerPercent) {
        for (int i = 0; i < (useHandsanitizerPercent *people.size() / 100); i++) {
            int randPerson = rand.nextInt(people.size());
            if (!people.get(randPerson).getUsesHandSanitizer()) {
                people.get(randPerson).setUsesHandSanitizer(true);
            } else {
                i--;
            }
        }
    }

    public void coverCoughPeople(int coverCoughPercent) {
        for (int i = 0; i < (coverCoughPercent * people.size() / 100); i++) {
            int randPerson = rand.nextInt(people.size());
            if (!people.get(randPerson).getCoughsInSleeve()) {
                people.get(randPerson).setCoughsInSleeve(true);
            } else {
                i--;
            }
        }
    }

    public void stayHomePeople(int stayHomePercent) {
        for (int i = 0; i < (stayHomePercent * people.size() / 100); i++) {
            int randPerson = rand.nextInt(people.size());
            if (!people.get(randPerson).getStaysHome()) {
                people.get(randPerson).setStaysHome(true);
            } else {
                i--;
            }
        }
    }

    public boolean importMap(String resourceUrl) throws IOException {
        String filePath = getClass().getResource("/maps/" + resourceUrl).getFile();
        filePath = filePath.replace("%20", " ");
        File file = new File(filePath);
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

    public void initialiseSimulation(int peopleAmount, int infectedAmount, double infectionRisk, int infectionRange) {
        //ny influenza
        influenza = new Influenza(infectionRisk, infectionRange,
                1, 4,
                0.000002, 0.1);

        //Nulstil tiden
        clock.resetTime();

        //Nulstril Graf
        GUI.lineChart.resetLineChart();
        lastGraphUpdate = -GUI.lineChart.ticksPerPoint;

        //opret personer
        addPeople(peopleAmount, infectedAmount);

        simulationHasBeenInitialised = true;
    }

    public void addPeople(int totalpeople, int infecteds) {
        for (int i = 0; i < totalpeople; i++) {
            int randomHouse = rand.nextInt(houses.size());
            int randomWork = rand.nextInt(workplaces.size());

            Person person = new Person(houses.get(randomHouse), workplaces.get(randomWork));

            if (i < infecteds)
                person.infect(influenza);

            //Tilføj person til liste
            people.add(person);
        }
    }

    public boolean hasBeenInitialised() {
        return simulationHasBeenInitialised;
    }

    public boolean isActive() {
        return simulationIsActive;
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

        GUI.theGUI.onStopSimulation();
    }

    public void clearSimulation() {
        stopSimulation();
        newDay = true;
        for (Person p : people) {
            p = null;
        }
        people.clear();
        snapshots.clear();
    }

    public void exportResults(File file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath())) {
            writer.write("Tick,Susceptible,Infected,Recovered,Dead\n");
            for (int i = 0; i < snapshots.size(); i++) {
                writer.write(i + "," + snapshots.get(i).toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Unable to write file.");
        }
    }

    public void simulate(double currentTime, double deltaTime) {
        if (!simulationIsActive)
            return;

        int hour = clock.currentTime.hours;

        if (newDay && hour == 0) {
            for (Person p : people)
                p.dailyUpdate();

            newDay = false;
        }

        if (currentTime > lastTick + tickTime) {
            int currentTick = clock.getCurrentTick();
            //Opdater positionen og helbredet for personen
            for (Person p : people) {
                p.updateDisease(currentTime);
                p.updateDestination();
                p.updateMovement();
            }

            int day = clock.currentTime.days;
            clock.tick();
            if (day < clock.currentTime.days)
                newDay = true;

            lastTick = currentTime;

            if (currentTick >= lastGraphUpdate + GUI.lineChart.ticksPerPoint) {
                saveTick();
                lastGraphUpdate = currentTick;
            }
        }

        for (Person p : people) {
            //opdater skærmpositioner
            p.updateScreenPosition((currentTime - lastTick) * (1 / tickTime));
        }

        //Kør simulationen langsommere når folk skal bevæge sig

        if (hour > 6 && hour < 9 || hour > 13 && hour < 16)
            tickTime = slowTickTime;
        else
            tickTime = fastTickTime;
    }

    private void saveTick() {
        double tick = clock.getGraphTime();
        int susceptible = healthCount(Person.health.Susceptible);
        int infected = healthCount(Person.health.Infected);
        int recovered = healthCount(Person.health.Recovered);
        int dead = healthCount(Person.health.Dead);

        snapshots.add(new Snapshot(susceptible,infected,recovered,dead));

        GUI.lineChart.updateLineChart(tick, susceptible, infected, recovered);
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
        if (influenza != null)
            return influenza.calculateR0(beta, gamma);
        else
            return "There is no disease.";
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
