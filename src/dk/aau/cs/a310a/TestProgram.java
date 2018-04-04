package dk.aau.cs.a310a;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProgram {

    //Test om R0 beregningen bliver foretaget korrekt
    @Test
    public void isEpidemic01() {
        Influenza influenzaA = new Influenza(Influenza.influenzaType.A);
        double R0 = influenzaA.calculateR0(influenzaA.getBaseSpread(),influenzaA.getAmountCured());
        assertEquals(2, R0);
    }

    //Test om person/objektet bliver inficeret
    @Test
    public void infectPerson01() {
        Influenza influenzaA = new Influenza(Influenza.influenzaType.A);
        Person person = new Person(25, Person.health.Susceptible, new Vector(0,0));

        influenzaA.infectPerson(person);
        assertEquals(Person.health.Infected, person.getCurrentHealth());
    }

}
