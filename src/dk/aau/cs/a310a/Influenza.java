package dk.aau.cs.a310a;

public class Influenza {
    private int baseSpread;  //Beta
    private int amountCured; //Gamma

    public enum influenzaType {A, B}
    private influenzaType type;

    public Influenza(influenzaType type) {
        switch (type){
            case A:
                this.baseSpread = 4;
                this.amountCured = 2;
                break;
            case B:
                this.baseSpread = 3;
                this.amountCured = 1;
                break;
        }
    }

    public int getBaseSpread() {
        return baseSpread;
    }

    public int getAmountCured() {
        return amountCured;
    }

    public void infectPerson(Person person) {
        person.setCurrentHealth(Person.health.Infected);
    }

    public double calculateR0(int beta, int gamma) {
        double isEpidemic = (double)beta / gamma; //R0

        if(isEpidemic >= 1)
            System.out.println("There is a chance of an epidemic.");
        else
            System.out.println("No chance of an epidemic. (yet)");

        return isEpidemic;
    }
}
