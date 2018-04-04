package dk.aau.cs.a310a;

public class Vector {
    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector Add(Vector a, Vector b) {
        Vector result = new Vector(a.x + b.x, a.y + b.y);
        return result;
    }

    public static Vector Subtract(Vector a, Vector b) {
        Vector result = new Vector(a.x - b.x, a.y - b.y);
        return result;
    }

    public static Vector Multiply(Vector v, double a) {
        Vector result = new Vector(v.x * a, v.y * a);
        return result;
    }

    public static Vector lerp(Vector a, Vector b, double t) {
        return Vector.Add(Vector.Multiply(a, (1 - t)), Vector.Multiply(b, t));
    }
}
