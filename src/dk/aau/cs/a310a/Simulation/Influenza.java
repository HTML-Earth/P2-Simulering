package dk.aau.cs.a310a.Simulation;

public class Influenza {
    private int baseSpread;  //Beta
    private int amountCured; //Gamma
    private double infectionRange;
    private double infectionRisk;
    private double baseDeathRisk;
    private double movingMultiplier;

    public enum influenzaType {A, B}

    private influenzaType type;

    //Gør det muligt at bede om en Influenza af typen A eller B
    public Influenza(influenzaType type) {
        switch (type) {
            case A:
                this.baseSpread = 4;
                this.amountCured = 2;
                this.infectionRange = 2;
                this.infectionRisk = 0.3;
                this.baseDeathRisk = 0.001;
                this.movingMultiplier = 0.1;
                break;
            case B:
                this.baseSpread = 3;
                this.amountCured = 1;
                this.infectionRange = 1;
                this.infectionRisk = 0.2;
                this.baseDeathRisk = 0.001;
                this.movingMultiplier = 0.1;
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

    public double getMovingMultiplier() {
        return movingMultiplier;
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
