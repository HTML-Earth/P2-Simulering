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

    public int getAge() {
        return age;
    }

    public Vector getPosition()
    {
        return position;
    }

    public void setPosition(Vector position)
    {
        this.position = position;
    }

    public void setTarget(Vector target)
    {
        this.target = target;
    }

    public void updateMovement()
    {
        position = Vector.lerp(position,target,0.01);
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
