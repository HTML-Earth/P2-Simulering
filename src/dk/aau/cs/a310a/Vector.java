package dk.aau.cs.a310a;

public class Vector {
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
}
