package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.Simulation.Person;

import java.util.List;

public class Influenza {
    private int baseSpread;  //Beta
    private int amountCured; //Gamma
    private double infectionRange;
    private double infectionRisk;
    private double baseDeathRisk;

    public enum influenzaType {A, B}

    private influenzaType type;

    //Gør det muligt at bede om en Influenza af typen A eller B
    public Influenza(influenzaType type) {
        switch (type) {
            case A:
                this.baseSpread = 4;
                this.amountCured = 2;
                this.infectionRange = 3;
                this.infectionRisk = 0.5;
                this.baseDeathRisk = 0.001;
                break;
            case B:
                this.baseSpread = 3;
                this.amountCured = 1;
                this.infectionRange = 2;
                this.infectionRisk = 0.4;
                this.baseDeathRisk = 0.001;
                break;
        }
    }

    public int getBaseSpread() {
        return baseSpread;
    }

    public int getAmountCured() {
        return amountCured;
    }

    public double getInfectionRange() {
        return infectionRange;
    }

    public double getInfectionRisk() {
        return infectionRisk;
    }

    public double getDeathRisk(int age) {
        if (age < 80)
            return baseDeathRisk;
        else
            return baseDeathRisk * (double)age * 0.5;
    }

    //Sætter personen/objektets 'health' til at være 'infected'
    public void infectPerson(List<Person> people, int start, int end) {
        for (int i = start; i < end; i++) {
            if (i < people.size())
                people.get(i).setCurrentHealth(Person.health.Infected);
            else
                break;
        }
    }

    public void infectPerson(Person person){
        person.setDisease(this);
        person.setCurrentHealth(Person.health.Infected);
    }

    //Udregn R0 (chancen for epidemi) baseret på beta og gamme
    public String calculateR0(double beta, double gamma) {
        double isEpidemic = beta / gamma; //R0

        if (isEpidemic >= 1)
            return "There is a chance of an epidemic.";
        else
            return "No chance of an epidemic. (yet)   ";
    }
}
