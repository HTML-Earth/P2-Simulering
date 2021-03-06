package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.Grid.GridPosition;
import dk.aau.cs.a310a.Grid.Vector;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

public class Person {
    public enum health {Susceptible, Infected, Recovered, Dead};

    int minDaysBeforeHospital = 1;
    int maxDaysBeforeHospital = 2;

    private health currentHealth = health.Susceptible;
    private Influenza disease;
    private int age;

    //tidspunkt hvor personen blev inficeret
    private int tickInfected = 0;

    private int tickRecovered = 0;

    private int ticksBeforeRecover = 0;

    private int ticksBeforeHospital = 0;

    private int othersInfected = 0;

    private boolean hasDestination;

    private int workHour = 8;
    private int homeHour = 15;
    private int distanceToWork;
    private int distanceToHospital;

    private int departureTimeModifier = 0;

    private boolean beenToHospital = false;
    private boolean goingToHospital;

    private boolean stayingHome;

    private boolean isVaccinated = false;
    private boolean usesHandSanitizer = false;
    private boolean coughsInSleeve = false;
    private boolean staysHome = false;

    private String statusString = "";

    //midlertidige positioner
    private GridPosition position;
    private GridPosition destination;

    private ArrayDeque<GridPosition> currentPath;

    //permanente positioner
    private GridPosition home;
    private GridPosition work;
    private GridPosition hospital;

    //display positioner
    private Vector screenPosition;
    private Vector nextScreenPosition;

    //RNG
    private Random rand;

    public Person(GridPosition homePosition, GridPosition workPosition) {
        rand = new Random();

        initPosition(homePosition);
        this.work = workPosition;

        int randomHospital = rand.nextInt(Simulator.theSimulator.getHospitals().size());
        hospital = Simulator.theSimulator.getHospitals().get(randomHospital);

        distanceToWork = GridPosition.getPath(homePosition, workPosition).size();
        distanceToHospital = GridPosition.getPath(homePosition, hospital).size();

        setDepartureTimeModifier();

        this.age = rand.nextInt(80) + 20;
    }

    public boolean getVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public boolean getUsesHandSanitizer() {
        return usesHandSanitizer;
    }

    public void setUsesHandSanitizer(boolean usesHandSanitizer) {
        this.usesHandSanitizer = usesHandSanitizer;
    }

    public boolean getCoughsInSleeve() {
        return coughsInSleeve;
    }

    public void setCoughsInSleeve(boolean coughsInSleeve) {
        this.coughsInSleeve = coughsInSleeve;
    }

    public boolean getStaysHome() {
        return staysHome;
    }

    public void setStaysHome(boolean staysHome) {
        this.staysHome = staysHome;
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
                    if (p.getCurrentHealth() == health.Susceptible) {
                        //Tjek om personerne er tæt på hinanden
                        if (GridPosition.distance(this.position, p.getGridPosition()) < disease.getInfectionRange()) {
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
        double vaccineReduceRisk = 1;
        double handsanitizerReduceRisk = 1;
        double coverReduceRisk = 1;

        //hvis personen bevæger sig, så er der mindre infektionsrisiko
        double movingPenalty = (!isMoving() && !p.isMoving()) ? 1 : disease.getMovingMultiplier();

        //Hvis personen er vaccineret er der mindre risiko, 0,5 svarer til 50% reduktion i risiko
        if (p.getVaccinated()) {
            vaccineReduceRisk = 0.5;
        }
        //Reduktion på ca. 20%, diminishing returns hvis begge bruger
        if (p.getUsesHandSanitizer() && this.getUsesHandSanitizer()) {
            handsanitizerReduceRisk = 0.70;
        } else if (p.getUsesHandSanitizer()) {
            handsanitizerReduceRisk = 0.8;
        } else if (this.getUsesHandSanitizer()) {
            handsanitizerReduceRisk = 0.8;
        }

        //Reducerer med 7%, udgangspunkt i personen som i forvejen er infected (den som smitter til person p)
        if (getCoughsInSleeve()) {
            coverReduceRisk = 0.93;
        }

        if (rand.nextDouble() < disease.getInfectionRisk() * Simulator.theSimulator.getTickTime() * movingPenalty * vaccineReduceRisk * handsanitizerReduceRisk * coverReduceRisk)
        {
            p.infect(disease);
            othersInfected++;
        }
    }

    public void dailyUpdate() {
        if (beenToHospital) {
            goingToHospital = false;
            return;
        }

        double goToHospitalChance = 0;
        double stayHomeChance = 0;

        switch (currentHealth) {
            case Susceptible:
                goToHospitalChance = 0.005;
                break;
            case Infected:
                if (Simulator.clock.getCurrentTick() >= tickInfected + ticksBeforeHospital)
                    goToHospitalChance = 0.5;
                if (staysHome)
                    stayHomeChance = 1;
                break;
            case Recovered:
                goToHospitalChance = 0.001;
                break;
            case Dead:
                goToHospitalChance = 1;
                break;
        }

        stayingHome = (rand.nextDouble() < stayHomeChance);

        goingToHospital = (rand.nextDouble() < goToHospitalChance);

        if (goingToHospital)
            beenToHospital = true;
    }

    public void updateDestination() {
        if (currentHealth == health.Dead)
            return;

        if (hasDestination){
            statusString = "Moving";
            return;
        }

        if (position == home) {
            statusString = "Home";
            if (!stayingHome) {
                if (goingToHospital) {
                    if (Simulator.clock.ticksUntil(workHour) + distanceToHospital + departureTimeModifier == 0) {
                        setDestination(hospital);
                    }
                } else {
                    if (Simulator.clock.ticksUntil(workHour) + distanceToWork + departureTimeModifier == 0) {
                        setDestination(work);
                    }
                }
            }
        }

        if (position == work) {
            statusString = "Work";
            if (Simulator.clock.ticksUntil(homeHour) + distanceToWork + departureTimeModifier == 0) {
                setDestination(home);
            }
        }

        if (position == hospital) {
            statusString = "Hospital";
            if (currentHealth != health.Infected) {
                if (Simulator.clock.ticksUntil(homeHour) + distanceToHospital + departureTimeModifier == 0) {
                    setDestination(home);
                }
            }
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

    public void setDestination(Simulator.placeType place) {
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
        } else {
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

        setDepartureTimeModifier();
    }

    public boolean isMoving() {
        return !(position.equals(destination));
    }

    void setDepartureTimeModifier() {
        departureTimeModifier = rand.nextInt(10) - 5;
    }

    public health getCurrentHealth() {
        return currentHealth;
    }

    public void infect(Influenza disease) {
        this.currentHealth = health.Infected;
        this.disease = disease;
        this.tickInfected = Simulator.clock.getCurrentTick();
        this.ticksBeforeRecover = disease.getTicksBeforeRecover();
        this.ticksBeforeHospital = getTicksBeforeHospital();
    }

    public double getInfectionDuration() {
        int ticks = 0;

        if (currentHealth == health.Susceptible)
            ticks = 0;
        else {
            if (tickRecovered != 0) {
                ticks = tickRecovered - tickInfected;
            }
            else {
                ticks = Simulator.clock.currentTick - tickInfected;
            }
        }

        return ticks;
    }

    public int getOthersInfected() {
        return othersInfected;
    }

    int getTicksBeforeHospital() {
        int ticksPerDay = 60 * 24;
        int bound = maxDaysBeforeHospital - minDaysBeforeHospital;
        return rand.nextInt(bound * ticksPerDay) + minDaysBeforeHospital * ticksPerDay;
    }

    public void influenzaRecover(int currentTick) {
        if (currentTick - tickInfected > ticksBeforeRecover) {
            //Risiko for at dø
            if (rand.nextDouble() < disease.getDeathRisk(age))
                currentHealth = health.Dead;
            else
                currentHealth = health.Recovered;

            tickRecovered = currentTick;
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
        String health = getCurrentHealth().toString();
        String infectedTime = (getCurrentHealth() == Person.health.Susceptible) ? "\t" : "\t\t " + (tickInfected/1440+1);
        if (getCurrentHealth() == Person.health.Recovered)
            infectedTime = "\t " + (tickInfected/1440+1);
        String ageString = "\t\t " + age;
        String status = "\t\t " + statusString;
        String pos = "\t\t " + position.x + "\t " + position.y;
        return health + infectedTime + ageString + status + pos;
    }
}
