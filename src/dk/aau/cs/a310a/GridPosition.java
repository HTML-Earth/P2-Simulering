package dk.aau.cs.a310a;

import java.util.ArrayList;

public class GridPosition {
    public int x, y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static ArrayList<GridPosition> getNeighbours (GridPosition pos) {
        ArrayList<GridPosition> neighbours = new ArrayList<>();

        for (int x = pos.x - 1; x <= pos.x + 1; x++) {
            for (int y = pos.y - 1; y <= pos.y + 1; y++) {
                if (x >= 0 && x <= 40 && y >= 0 && y <= 30 && x != pos.x && y != pos.y) {
                    neighbours.add(new GridPosition(x,y));
                }
            }
        }

        return neighbours;
    }

    public static GridPosition add(GridPosition a, GridPosition b) {
        GridPosition result = new GridPosition(a.x + b.x, a.y + b.y);
        return result;
    }

    public static GridPosition subtract(GridPosition a, GridPosition b) {
        GridPosition result = new GridPosition(a.x - b.x, a.y - b.y);
        return result;
    }

    public static GridPosition multiply(GridPosition v, int a) {
        GridPosition result = new GridPosition(v.x * a, v.y * a);
        return result;
    }

    public static int distance(GridPosition a, GridPosition b) {
        double distance = Math.sqrt(Math.pow(b.x - a.x, 2)+Math.pow(b.y-a.y,2));
        return (int)distance;
    }
}
