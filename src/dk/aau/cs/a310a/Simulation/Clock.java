package dk.aau.cs.a310a.Simulation;

public class Clock {
    int minsPerTick = 1;

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
        currentTime.addMinutes(minsPerTick);
    }

    public int ticksUntil(int hour) {
        int totalMinutes = currentTime.hours * 60 + currentTime.minutes;
        return (totalMinutes - (hour * 60)) / minsPerTick;
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
