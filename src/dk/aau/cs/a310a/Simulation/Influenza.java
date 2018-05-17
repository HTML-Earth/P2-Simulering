package dk.aau.cs.a310a.Simulation;

import java.util.Random;

public class Influenza {
    private double infectionRisk;
    private double infectionRange;

    private double minDaysBeforeRecover;
    private double maxDaysBeforeRecover;

    private double baseDeathRisk;
    private double movingMultiplier;

    Random rand;

    public Influenza(double infectionRisk, double infectionRange,
                     double minDaysBeforeRecover, double maxDaysBeforeRecover,
                     double baseDeathRisk, double movingMultiplier) {

        rand = new Random();
        this.infectionRisk = infectionRisk;
        this.infectionRange = infectionRange;

        this.minDaysBeforeRecover = minDaysBeforeRecover;
        this.maxDaysBeforeRecover = maxDaysBeforeRecover;

        this.baseDeathRisk = baseDeathRisk;
        this.movingMultiplier = movingMultiplier;
    }

    public double getInfectionRisk() {
        return infectionRisk;
    }

    public double getInfectionRange() {
        return infectionRange;
    }

    public int getTicksBeforeRecover() {
        double ticksPerDay = 60 * 24;
        double bound = maxDaysBeforeRecover - minDaysBeforeRecover;
        return rand.nextInt((int)(bound * ticksPerDay)) + (int)(minDaysBeforeRecover * ticksPerDay);
    }

    public double getDeathRisk(int age) {
        return age * baseDeathRisk;
    }

    public double getMovingMultiplier() {
        return movingMultiplier;
    }

    //Udregn R0 (chancen for epidemi) baseret på beta og gamme
    public String calculateR0(double beta, double gamma) {
        double isEpidemic = beta / gamma; //R0

        if (isEpidemic >= 1)
            return "R0: " + isEpidemic + "\nThere is a chance of an epidemic.";
        else
            return "R0: " + isEpidemic + "\nNo chance of an epidemic. (yet)";
    }
}
