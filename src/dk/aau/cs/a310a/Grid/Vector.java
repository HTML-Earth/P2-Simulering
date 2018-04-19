package dk.aau.cs.a310a.Grid;

import java.util.Objects;

public class Vector {
    public static final double gridScale = 20;
    public static final double gridOffset = 10;

    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector add(Vector a, Vector b) {
        Vector result = new Vector(a.x + b.x, a.y + b.y);
        return result;
    }

    public static Vector subtract(Vector a, Vector b) {
        Vector result = new Vector(a.x - b.x, a.y - b.y);
        return result;
    }

    public static Vector multiply(Vector v, double a) {
        Vector result = new Vector(v.x * a, v.y * a);
        return result;
    }

    public static Vector lerp(Vector a, Vector b, double t) {
        return Vector.add(Vector.multiply(a, (1 - t)), Vector.multiply(b, t));
    }

    public static double distance(Vector a, Vector b) {
        double distance = Math.sqrt(Math.pow(b.x - a.x, 2)+Math.pow(b.y-a.y,2));
        return distance;
    }

    public static Vector gridToScreen(GridPosition gridPosition) {
        return new Vector(gridPosition.x * gridScale + gridOffset, gridPosition.y * gridScale + gridOffset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
