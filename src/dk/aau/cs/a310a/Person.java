package dk.aau.cs.a310a;

public class Person {
    private int age;
    public enum health {Susceptible, Infected, Recovered};
    private health currentHealth;

    public Person(int age, health currentHealth) {
        this.age = age;
        this.currentHealth = currentHealth;
    }

    public int getAge() {
        return age;
    }

    public health getCurrentHealth() {
        return currentHealth;
    }

    public String toString() {
        return "Person: " + "Age: " + getAge() + "\t\t Health: " + getCurrentHealth();
    }
}
