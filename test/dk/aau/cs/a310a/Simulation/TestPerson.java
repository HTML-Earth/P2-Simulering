package dk.aau.cs.a310a.Simulation;

import dk.aau.cs.a310a.Grid.GridPosition;
import dk.aau.cs.a310a.Grid.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPerson {
    Person person;

    @BeforeEach
    public void setupPerson()
    {
        person = new Person(new GridPosition(0,0), new GridPosition(10,10));
    }

    @Test
    void TestPosition01() {
        assertEquals(new GridPosition(0,0),person.getGridPosition());
    }

    @Test
    void TestPosition02() {
        assertEquals(new Vector(Vector.gridOffset,Vector.gridOffset),person.getPosition());
    }
}
