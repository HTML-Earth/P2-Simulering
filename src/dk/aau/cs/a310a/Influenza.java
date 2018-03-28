package dk.aau.cs.a310a;

public class Influenza {
    private double isEpidemic; //R0
    private int baseSpread;  //Beta
    private int amountCured; //Gamma

    private enum influenzaType {A, B};
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

    public double calculateR0(int spread, int amountCured) {
        isEpidemic = (double)spread / amountCured;

        if(isEpidemic >= 1)
            System.out.println("There is a chance of an epidemic.");
        else
            System.out.println("No chance of an epidemic. (yet)");

        return isEpidemic;
    }
}
