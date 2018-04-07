package dk.aau.cs.a310a;

import java.util.Random;

public class Person {
    public enum health {Susceptible, Infected, Recovered, Dead};

    private health currentHealth;
    private Influenza disease;
    private int age;
    public double timeInfected = 0;
    private Vector position;
    private Vector destination;

    private Vector homePosition;

    private double destinationReachedRange = 10;

    public Person(int age, health currentHealth, Vector position, Vector homePosition) {
        this.age = age;
        this.currentHealth = currentHealth;
        this.position = position;
        this.homePosition = homePosition;
        this.destination = homePosition;
    }

    public void updateDisease(double currentTime, double deltaTime) {
        switch (currentHealth) {
            case Susceptible:
                break;
            case Infected:
                //Check for recovery
                if(timeInfected == 0)
                    timeInfected = currentTime;

                influenzaRecover(currentTime);
                for (Person p : Simulator.theSimulator.people) {
                    //Tjek om personen er susceptible
                    if (p.getCurrentHealth() == health.Susceptible){
                        //Tjek om personerne er tæt på hinanden
                        if (Vector.distance(this.position,p.getPosition()) < disease.getInfectionRange()){
                            //Risiko for infektion
                            if (Simulator.theSimulator.rand.nextDouble() < disease.getInfectionRisk() * deltaTime){
                                //Inficer den anden person
                                disease.infectPerson(p);
                            }
                        }
                    }
                }
                break;
            case Recovered:
                break;
            case Dead:
                break;
        }
    }

    public void updateMovement(double deltaTime) {
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
                double targetX = position.x + Simulator.theSimulator.rand.nextDouble() * 100 - 50;
                double targetY = position.y + Simulator.theSimulator.rand.nextDouble() * 100 - 50;
                setDestination(new Vector(targetX, targetY));
            }
        }
        position = Vector.lerp(position, destination, deltaTime);
    }

    public int getAge() {
        return age;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
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
            //Chance for at dø
            if (rand.nextDouble() < 0.01)
                setCurrentHealth(health.Dead);
            else
                setCurrentHealth(health.Recovered);
        }
    }

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return getCurrentHealth() + "\t Age:" + age + "\t X:" + (int)position.x + "\t Y:" + (int)position.y;
    }
}
