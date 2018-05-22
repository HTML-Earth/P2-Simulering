package dk.aau.cs.a310a.Grid;

import dk.aau.cs.a310a.Simulation.Simulator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GridPosition {
    public static final int gridWidth = 40;
    public static final int gridHeight = 30;

    public int x, y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static ArrayList<GridPosition> getNeighbours (GridPosition pos) {
        ArrayList<GridPosition> neighbours = new ArrayList<>();

        for (int x = pos.x - 1; x <= pos.x + 1; x++) {
            for (int y = pos.y - 1; y <= pos.y + 1; y++) {
                if (x >= 0 && x <= 40 && y >= 0 && y <= 30 && (x == pos.x || y == pos.y)) {
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

    public boolean isValidGridPosition() {
        if (x < 0)
            return false;
        if (x >= gridWidth)
            return false;
        if (y < 0)
            return false;
        if (y >= gridHeight)
            return false;

        return true;
    }

    public static ArrayDeque<GridPosition> getPath(GridPosition start, GridPosition end) {
        ArrayDeque<GridPosition> path = new ArrayDeque<>();

        if (start != end) {
            //BREADTH FIRST SEARCH
            ArrayDeque<GridPosition> frontier = new ArrayDeque<GridPosition>();
            frontier.add(start);

            HashMap<GridPosition, GridPosition> cameFrom = new HashMap<>();
            cameFrom.put(start, null);

            while (!frontier.isEmpty()) {
                GridPosition current = frontier.removeFirst();
                if (current == end) {
                    break;
                }

                for (GridPosition next : GridPosition.getNeighbours(current)) {
                    if (next.isValidGridPosition() && !cameFrom.containsKey(next)
                            && Simulator.theSimulator.getPlaceType(next) != Simulator.placeType.Grass) {
                        frontier.add(next);
                        cameFrom.put(next, current);
                    }
                }
            }

            //FOLLOW PATH

            GridPosition current = end;

            int i = 0;
            while (current != start && i < 200) {
                i++;
                if (current != null) {
                    path.add(current);
                    current = cameFrom.get(current);
                }
            }
        }

        path.add(start);
        return path;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof GridPosition))
            return false;
        if (obj == this)
            return true;
        return this.x == ((GridPosition)obj).x && this.y == ((GridPosition)obj).y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
