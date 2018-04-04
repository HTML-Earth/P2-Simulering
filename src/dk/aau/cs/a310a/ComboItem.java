package dk.aau.cs.a310a;

public class ComboItem {
    private String key;
    private double value;

    public ComboItem(String key, double value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public String toString() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }

}