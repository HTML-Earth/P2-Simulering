package dk.aau.cs.a310a.Simulation;

public class Clock {
    Time currentTime;

    public Clock() {
        this.currentTime = new Time(0,0,0);
    }

    public void resetTime() {
        currentTime.reset();
    }

    public void tick() {
        currentTime.addMinutes(10);
    }

    public String getTimeString() {
        return "Day " + currentTime.dayString() + " [" + currentTime.hourString() + ":" + currentTime.minuteString() + "]";
    }
}
