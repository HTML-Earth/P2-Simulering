package dk.aau.cs.a310a;

import java.util.List;
import java.util.Random;

public class Person {
    public enum health {Susceptible, Infected, Recovered, Dead};

    private health currentHealth;
    private Influenza disease;
    private int age;
    public double timeInfected = 0;
    private Vector position;
    private Vector target;

    public Person(int age, health currentHealth, Vector position) {
        this.age = age;
        this.currentHealth = currentHealth;
        this.position = position;
        this.target = this.position;
    }

    public void updateDisease(double time) {
        switch (currentHealth) {
            case Susceptible:
                break;
            case Infected:
                //Check for recovery
                if(timeInfected == 0)
                    timeInfected = time;

                influenzaRecover(time);
                for (Person p : Simulator.theSimulator.people) {
                    //Tjek om personen er susceptible
                    if (p.getCurrentHealth() == health.Susceptible){
                        //Tjek om personerne er tæt på hinanden
                        if (Vector.distance(this.position,p.getPosition()) < 50){
                            //Risiko for infektion
                            if (Simulator.theSimulator.rand.nextDouble() < 0.2){
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

    public void updateMovement() {
        if (currentHealth == health.Dead)
            return;

        double targetX = position.x + Simulator.theSimulator.rand.nextDouble() * 400 - 200;
        double targetY = position.y + Simulator.theSimulator.rand.nextDouble() * 400 - 200;
        setTarget(new Vector(targetX, targetY));
        position = Vector.lerp(position, target, 0.02);
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

    public void setTarget(Vector target) {
        this.target = target;
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
        return "Person: " + "Age: " + getAge() + "\t\t Health: " + getCurrentHealth();
    }
}
