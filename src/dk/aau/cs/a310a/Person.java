package dk.aau.cs.a310a;

public class Person {
    private int age;

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

    public void updateDisease() {
        switch (currentHealth) {
            case Susceptible:
                break;
            case Infected:
                //Check for recovery

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
        double targetX = position.x + Simulator.theSimulator.rand.nextDouble() * 400 - 200;
        double targetY = position.y + Simulator.theSimulator.rand.nextDouble() * 400 - 200;
        setTarget(new Vector(targetX, targetY));
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

    //Metoden som kaldes når man printer objektet
    public String toString() {
        return "Person: " + "Age: " + getAge() + "\t\t Health: " + getCurrentHealth();
    }
}
