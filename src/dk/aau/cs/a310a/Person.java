package dk.aau.cs.a310a;

import javax.sound.midi.Soundbank;
import java.util.Random;

public class Person {
    public enum health {Susceptible, Infected, Recovered, Dead};

    private health currentHealth;
    private Influenza disease;
    private int age;

    //tidspunkt hvor personen blev inficeret
    public double timeInfected = 0;

    //positioner
    private Vector position;
    private Vector destination;
    private Vector homePosition;

    private Vector screenPosition;
    private Vector nextScreenPosition;

    private double destinationReachedRange = 1;

    public Person(int age, health currentHealth, Vector homePosition) {
        this.age = age;
        this.currentHealth = currentHealth;
        this.position = homePosition;
        this.homePosition = homePosition;
        this.destination = homePosition;

        this.screenPosition = gridToScreen(homePosition);
        this.nextScreenPosition = this.screenPosition;
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
                        if (Vector.distance(this.position,p.getGridPosition()) < disease.getInfectionRange()){
                            //Risiko for infektion
                            if (Simulator.theSimulator.rand.nextDouble() < disease.getInfectionRisk() * Simulator.theSimulator.getTickTime()){
                                //Inficer den anden person
                                disease.infectPerson(p);
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

        if (Vector.distance(position, destination) < destinationReachedRange) {

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

            if (Simulator.theSimulator.rand.nextDouble() < goToWorkChance) {
                setDestination(Simulator.theSimulator.getWorkPosition());
            }
            else if (Simulator.theSimulator.rand.nextDouble() < stayHomeChance) {
                setDestination(homePosition);
            }
            else if (Simulator.theSimulator.rand.nextDouble() < goToHospitalChance) {
                setDestination(Simulator.theSimulator.getHospitalPosition());
            }
            else {
                setDestination(Simulator.theSimulator.getHomePosition());
            }
        }
    }

    public void updateMovement() {
        if (currentHealth == health.Dead)
            return;

        if (position.x < destination.x)
            position.x++;
        else if (position.x > destination.x)
            position.x--;

        if (position.y < destination.y)
            position.y++;
        else if (position.y > destination.y)
            position.y--;

        nextScreenPosition = gridToScreen(position);
    }

    public void updateScreenPosition(double t) {
        screenPosition = Vector.lerp(screenPosition, nextScreenPosition, t);
    }

    public int getAge() {
        return age;
    }

    public Vector getPosition() {
        return screenPosition;
    }

    public Vector getDestination() {
        return gridToScreen(destination);
    }

    public Vector getGridPosition() {
        return position;
    }

    public void setDestination(Vector destination) {
        this.destination = destination;
    }

    public health getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(health currentHealth) {
        if (this.currentHealth == currentHealth)
            return;
        this.currentHealth = currentHealth;
    }

    public Influenza getDisease() {
        return disease;
    }

    public void setDisease(Influenza disease) {
        this.disease = disease;
    }

    public void influenzaRecover(double timer) {
        Random rand = new Random();
        int timeBeforeRecover = rand.nextInt(6) + 4;

        if(timer - timeInfected > timeBeforeRecover) {
            //Risiko for at dø
            if (rand.nextDouble() < disease.getDeathRisk(age))
                setCurrentHealth(health.Dead);
            else
                setCurrentHealth(health.Recovered);
        }
    }

    public static Vector gridToScreen(Vector gridPosition) {
        return new Vector(gridPosition.x * 20 + 10, gridPosition.y * 20 + 10);
    }

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return getCurrentHealth() + "\t Age:" + age + "\t X:" + (int)screenPosition.x + "\t Y:" + (int)screenPosition.y;
    }
}
