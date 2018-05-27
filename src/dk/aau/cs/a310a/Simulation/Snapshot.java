package dk.aau.cs.a310a.Simulation;

public class Snapshot {
    public int susceptible;
    public int infected;
    public int recovered;
    public int dead;
    public double beta;
    public double gamma;
    public double r0;

    public Snapshot(int susceptible, int infected, int recovered, int dead, double beta, double gamma, double r0) {
        this.susceptible = susceptible;
        this.infected = infected;
        this.recovered = recovered;
        this.dead = dead;
        this.beta = beta;
        this.gamma = gamma;
        this.r0 = r0;
    }

    @Override
    public String toString() {
        return susceptible + ", " + infected + ", " + recovered + ", " + dead + ", " + beta + ", " + gamma + ", " + r0;
    }
}
