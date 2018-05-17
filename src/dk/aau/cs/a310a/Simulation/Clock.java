package dk.aau.cs.a310a.Simulation;

public class Clock {
    int minsPerTick = 1;

    int currentTick;
    Time currentTime;
    Simulator sim;

    public Clock(Simulator sim) {
        this.currentTick = 0;
        this.currentTime = new Time(0,0,0);
        this.sim = sim;
    }

    public void resetTime() {
        currentTick = 0;
        currentTime.reset();
    }

    public void tick() {
        currentTick++;
        currentTime.addMinutes(minsPerTick);
        if (currentTime.days > Simulator.maxDaysToSimulate - 1 && currentTime.minutes > 0)
            sim.stopSimulation();
    }

    public int ticksUntil(int hour) {
        int totalMinutes = currentTime.hours * 60 + currentTime.minutes;
        return (totalMinutes - (hour * 60)) / minsPerTick;
    }

    public double getGraphTime() {
        return currentTime.days + (currentTime.hours / 24.0);
    }

    public String getTimeString() {
        return "Day " + currentTime.dayString() + " [" + currentTime.hourString() + ":" + currentTime.minuteString() + "]";
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
