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
    int tickInfected = 0;

    int ticksBeforeRecover = 0;

    private boolean hasDestination;

    private int workHour = 8;
    private int homeHour = 15;
    private int distanceToWork;

    private String debugText = "";

    //midlertidige positioner
    private GridPosition position;
    private GridPosition destination;

    private ArrayDeque<GridPosition> currentPath;

    //permanente positioner
    private GridPosition home;
    private GridPosition work;

    //display positioner
    private Vector screenPosition;
    private Vector nextScreenPosition;

    //RNG
    private Random rand;

    public Person(GridPosition homePosition, GridPosition workPosition) {
        rand = new Random();

        initPosition(homePosition);
        this.work = workPosition;

        distanceToWork = GridPosition.getPath(homePosition, workPosition).size();

        this.age = rand.nextInt(80) + 20;
    }

    void initPosition(GridPosition homePosition) {
        this.position = homePosition;
        this.destination = homePosition;

        this.home = homePosition;

        this.screenPosition = Vector.gridToScreen(homePosition);
        this.nextScreenPosition = this.screenPosition;
    }

    public void updateDisease(double currentTime) {
        switch (currentHealth) {
            case Susceptible:
                break;
            case Infected:
                for (Person p : Simulator.theSimulator.people) {
                    //Tjek om personen er susceptible
                    if (p.getCurrentHealth() == health.Susceptible){
                        //Tjek om personerne er tæt på hinanden
                        if (GridPosition.distance(this.position,p.getGridPosition()) < disease.getInfectionRange()){
                            //Prøv at inficere personen
                            tryInfect(p);
                        }
                    }
                }

                influenzaRecover(Simulator.clock.getCurrentTick());
                break;
            case Recovered:
                break;
            case Dead:
                break;
        }
    }

    void tryInfect(Person p) {
        //hvis personen bevæger sig, så er der mindre infektionsrisiko
        double movingPenalty = (!isMoving() && !p.isMoving()) ? 1 : disease.getMovingMultiplier();

        if (rand.nextDouble() < disease.getInfectionRisk() * Simulator.theSimulator.getTickTime() * movingPenalty)
            p.infect(disease);
    }

    public void updateDestination() {
        if (currentHealth == health.Dead)
            return;

        if (hasDestination)
            return;

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

        if (position == home) {
            if (Simulator.clock.ticksUntil(workHour) + distanceToWork == 0) {
                setDestination(work);
            }
        }

        if (position == work) {
            if (Simulator.clock.ticksUntil(homeHour) + distanceToWork == 0) {
                setDestination(home);
            }
        }

        /*
        if (rand.nextDouble() < goToWorkChance) {
            setDestination(work);
        }
        else if (rand.nextDouble() < stayHomeChance) {
            setDestination(home);
        }
        else if (rand.nextDouble() < goToHospitalChance) {
            setDestination(Simulator.placeType.Hospital);
        }
        else {
            setDestination(Simulator.placeType.House);
        }*/
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
        }
    }

    public void updateScreenPosition(double t) {
        screenPosition = Vector.lerp(screenPosition, nextScreenPosition, t);
    }

    public Vector getPosition() {
        return screenPosition;
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

        currentPath = GridPosition.getPath(position, destination);

        if (currentPath.size() > 1) {
            this.destination = destination;
            hasDestination = true;

        }
    }

    public boolean isMoving() {
        return !(position.equals(destination));
    }

    public health getCurrentHealth() {
        return currentHealth;
    }

    public void infect(Influenza disease) {
        this.currentHealth = health.Infected;
        this.disease = disease;
        this.tickInfected = Simulator.clock.getCurrentTick();
        this.ticksBeforeRecover = rand.nextInt(430) + 144;
    }

    public void influenzaRecover(int currentTick) {
        if(currentTick - tickInfected > ticksBeforeRecover) {
            //Risiko for at dø
            if (rand.nextDouble() < disease.getDeathRisk(age))
                currentHealth = health.Dead;
            else
                currentHealth = health.Recovered;
        }
    }

    public String getDebugText() {
        /*if (hasDestination && currentPath != null)
            return "" + currentPath.size() + " " + debugText;
        else return debugText;*/
        return "" + distanceToWork;
    }

    public ArrayDeque<GridPosition> getCurrentPath() {
        return currentPath;
    }

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return String.format("%1$" + -20 + "s", getCurrentHealth()) + String.format("%1$" + -20 + "s", "\t Age:" + age) + "\t X:" + position.x + "\t Y:" + position.y;
    }
}
