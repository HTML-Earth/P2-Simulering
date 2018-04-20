package dk.aau.cs.a310a.Simulation;

public class Clock {
    int currentTick;
    Time currentTime;

    public Clock() {
        this.currentTick = 0;
        this.currentTime = new Time(0,0,0);
    }

    public void resetTime() {
        currentTick = 0;
        currentTime.reset();
    }

    public void tick() {
        currentTick++;
        currentTime.addMinutes(10);
    }

    public int getGraphTime() {
        return currentTime.days;
    }

    public String getTimeString() {
        return "Day " + currentTime.dayString() + " [" + currentTime.hourString() + ":" + currentTime.minuteString() + "]";
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
