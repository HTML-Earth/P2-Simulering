package dk.aau.cs.a310a.Simulation;

public class Time {
    public int days;
    public int hours;
    public int minutes;

    public Time(int days, int hours, int minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    public void reset() {
        days = 0;
        hours = 0;
        minutes = 0;
    }

    public void addMinutes(int mins) {
        minutes += mins;
        if (minutes > 59) {
            minutes -= 60;
            hours++;
            if (hours > 23) {
                hours = 0;
                days++;
            }
        }
    }

    public String dayString() {
        return "" + (days + 1);
    }

    public String hourString() {
        return (hours < 10) ? "0" + hours : "" + hours;
    }

    public String minuteString() {
        return (minutes < 10) ? "0" + minutes : "" + minutes;
    }
}
