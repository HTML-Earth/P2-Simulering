package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.Grid.GridPosition;
import dk.aau.cs.a310a.Grid.Vector;

import java.util.*;

public class Person {
    public enum health {Susceptible, Infected, Recovered, Dead};

    private health currentHealth = health.Susceptible;
    private Influenza disease;
    private int age;

    //tidspunkt hvor personen blev inficeret
    public double timeInfected = 0;

    private boolean hasDestination;

    private int placeTimeLeft = 0;

    private String debugText = "";

    //midlertidige positioner
    private GridPosition position;
    private GridPosition destination;

    private ArrayDeque<GridPosition> currentPath;

    //permanente positioner
    private GridPosition home;

    //display positioner
    private Vector screenPosition;
    private Vector nextScreenPosition;

    //RNG
    private Random rand;

    public Person(GridPosition homePosition) {
        rand = new Random();

        initPosition(homePosition);

        this.age = rand.nextInt(80) + 20;
    }

    public void updateDisease(double currentTime) {
        switch (currentHealth) {
            case Susceptible:
                break;
            case Infected:
                //Check for recovery
                if(timeInfected == 0)
                    timeInfected = currentTime;

                for (Person p : Simulator.theSimulator.people) {
                    //Tjek om personen er susceptible
                    if (p.getCurrentHealth() == health.Susceptible){
                        //Tjek om personerne er tæt på hinanden
                        if (GridPosition.distance(this.position,p.getGridPosition()) < disease.getInfectionRange()){
                            //Risiko for infektion
                            if (rand.nextDouble() < disease.getInfectionRisk() * Simulator.theSimulator.getTickTime()){
                                //Inficer den anden person
                                p.infect(disease);
                            }
                        }
                    }
                }

                influenzaRecover(currentTime);
                break;
            case Recovered:
                break;
            case Dead:
                break;
        }
    }

    public void updateDestination() {
        if (currentHealth == health.Dead)
            return;

        if (hasDestination)
            return;

        if (placeTimeLeft > 0) {
            placeTimeLeft--;
            return;
        }

        double goToWorkChance = 0;
        double stayHomeChance = 0;
        double goToHospitalChance = 0;

        switch (currentHealth) {
            case Susceptible:
                goToWorkChance = 0.4;
                stayHomeChance = 0.1;
                goToHospitalChance = 0.01;
                break;
            case Infected:
                goToWorkChance = 0.3;
                stayHomeChance = 0.8;
                goToHospitalChance = 0.9;
                break;
            case Recovered:
                goToWorkChance = 0.5;
                stayHomeChance = 0.1;
                goToHospitalChance = 0.001;
                break;
        }

        if (rand.nextDouble() < goToWorkChance) {
            setDestination(Simulator.placeType.Work);
        }
        else if (rand.nextDouble() < stayHomeChance) {
            setDestination(home);
        }
        else if (rand.nextDouble() < goToHospitalChance) {
            setDestination(Simulator.placeType.Hospital);
        }
        else {
            setDestination(Simulator.placeType.House);
        }
    }

    public void updateMovement() {
        if (currentHealth == health.Dead)
            return;

        if (!hasDestination)
            return;

        if (currentPath == null || currentPath.size() < 1) {
            System.out.println("NO PATH");
            return;
        }

        position = currentPath.getLast();

        currentPath.remove(currentPath.getLast());

        nextScreenPosition = Vector.gridToScreen(position);

        if (position.equals(destination)) {
            hasDestination = false;
            placeTimeLeft = 5;
        }
    }

    public void updateScreenPosition(double t) {
        screenPosition = Vector.lerp(screenPosition, nextScreenPosition, t);
    }

    public int getAge() {
        return age;
    }

    void initPosition(GridPosition homePosition) {
        this.position = homePosition;
        this.destination = homePosition;

        this.home = homePosition;

        this.screenPosition = Vector.gridToScreen(homePosition);
        this.nextScreenPosition = this.screenPosition;
    }

    public Vector getPosition() {
        return screenPosition;
    }

    public Vector getDestination() {
        return Vector.gridToScreen(destination);
    }

    public GridPosition getGridPosition() {
        return position;
    }

    public void setDestination (Simulator.placeType place) {
        List<GridPosition> places;

        switch (place) {
            case House:
                places = Simulator.theSimulator.getHouses();
                break;
            case Work:
                places = Simulator.theSimulator.getWorkplaces();
                break;
            case Hospital:
                places = Simulator.theSimulator.getHospitals();
                break;
            default:
                System.out.println("invalid place type");
                return;
        }

        if (places.size() < 1) {
            System.out.println("no places found");
        }
        else {
            setDestination(places.get(rand.nextInt(places.size())));
        }
    }

    public void setDestination(GridPosition destination) {
        if (hasDestination) {
            System.out.println("already have destination");
            return;
        }
        if (position == destination) {
            return;
        }

        //BREADTH FIRST SEARCH
        ArrayDeque<GridPosition> frontier = new ArrayDeque<GridPosition>();
        frontier.add(position);

        HashMap<GridPosition, GridPosition> cameFrom = new HashMap<>();
        cameFrom.put(position, null);

        while(!frontier.isEmpty()) {
            GridPosition current = frontier.removeFirst();
            if (current == destination) {
                break;
            }

            for (GridPosition next : GridPosition.getNeighbours(current)) {
                if (!cameFrom.containsKey(next) && Simulator.theSimulator.getPlaceType(next) != Simulator.placeType.Grass) {
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }

        //FOLLOW PATH

        GridPosition current = destination;
        ArrayDeque<GridPosition> path = new ArrayDeque<>();

        int i = 0;
        while (current != position && i < 200) {
            i++;
            if (current != null) {
                path.add(current);
                current = cameFrom.get(current);
            }
        }
        path.add(position);

        currentPath = path;

        this.destination = destination;
        hasDestination = true;

    }

    public boolean hasDestination() {
        return hasDestination;
    }

    public health getCurrentHealth() {
        return currentHealth;
    }

    public void infect(Influenza disease) {
        this.currentHealth = health.Infected;
        this.disease = disease;
    }

    public void influenzaRecover(double timer) {
        int timeBeforeRecover = rand.nextInt(6) + 4;

        if(timer - timeInfected > timeBeforeRecover) {
            //Risiko for at dø
            if (rand.nextDouble() < disease.getDeathRisk(age))
                currentHealth = health.Dead;
            else
                currentHealth = health.Recovered;
        }
    }

    public String getDebugText() {
        if (hasDestination)
            return "" + currentPath.size() + " " + debugText;
        else
            return "[" + placeTimeLeft + "] " + debugText;
    }

    public ArrayDeque<GridPosition> getCurrentPath() {
        return currentPath;
    }

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return getCurrentHealth() + "\t Age:" + age + "\t X:" + position.x + "\t Y:" + position.y;
    }
}
