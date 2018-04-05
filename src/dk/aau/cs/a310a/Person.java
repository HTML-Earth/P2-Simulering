package dk.aau.cs.a310a;

import java.util.Random;

public class Person {
    private int age;
    public double timeInfected = 0;

    public enum health {Susceptible, Infected, Recovered};
    private health currentHealth;
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
                for (Person p : Simulator.theSimulator.susceptible) {
                    if (Vector.distance(this.position,p.getPosition()) < 5){
                        //Chance of infection
                    }
                }
                break;
            case Recovered:
                break;
        }
    }

    public void updateMovement() {
        position = Vector.lerp(position, target, 0.01);
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
        this.currentHealth = currentHealth;
    }

    public void influenzaRecover(double timer) {
        Random rand = new Random();
        int timeBeforeRecover = rand.nextInt(3) + 2;

        if(timer - timeInfected > timeBeforeRecover) {
            setCurrentHealth(health.Recovered);
        }
    }

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return "Person: " + "Age: " + getAge() + "\t\t Health: " + getCurrentHealth();
    }
}
