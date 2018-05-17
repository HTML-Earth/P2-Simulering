package dk.aau.cs.a310a.Simulation;

public class Snapshot {
    public int susceptible;
    public int infected;
    public int recovered;
    public int dead;

    public Snapshot(int susceptible, int infected, int recovered, int dead) {
        this.susceptible = susceptible;
        this.infected = infected;
        this.recovered = recovered;
        this.dead = dead;
    }

    @Override
    public String toString() {
        return "S:" + susceptible + " I:" + infected + " R:" + recovered + " D:" + dead;
    }
}
