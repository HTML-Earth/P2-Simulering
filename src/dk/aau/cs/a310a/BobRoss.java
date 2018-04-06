package dk.aau.cs.a310a;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class BobRoss {
    double[] crossPointsX;
    double[] crossPointsY;

    public BobRoss() {
        crossPointsX = new double[12];
        crossPointsY = new double[12];

        //Top Left
        crossPointsX[0] = -1.0;
        crossPointsY[0] = -1.0;

        crossPointsX[1] = -0.8;
        crossPointsY[1] = -1.0;

        //Middle up
        crossPointsX[2] = 0.0;
        crossPointsY[2] = -0.2;

        //Top right
        crossPointsX[3] = 0.8;
        crossPointsY[3] = -1.0;

        crossPointsX[4] = 1.0;
        crossPointsY[4] = -1.0;

        //Middle right
        crossPointsX[5] = 0.2;
        crossPointsY[5] = 0.0;

        //Bottom right
        crossPointsX[6] = 1.0;
        crossPointsY[6] = 1.0;

        crossPointsX[7] = 0.8;
        crossPointsY[7] = 1.0;

        //Middle down
        crossPointsX[8] = 0.0;
        crossPointsY[8] = 0.2;

        //Bottom left
        crossPointsX[9] = -0.8;
        crossPointsY[9] = 1.0;

        crossPointsX[10] = -1.0;
        crossPointsY[10] = 1.0;

        //Middle left
        crossPointsX[11] = -0.2;
        crossPointsY[11] = 0.0;
    }

    public void drawPerson(Vector position, Person.health health, GraphicsContext gc) {
        Color color = Color.BLACK;
        switch (health) {
            case Susceptible:
                color = Color.CYAN;
                break;
            case Infected:
                color = Color.RED;
                break;
            case Recovered:
                color = Color.YELLOW;
                break;
            case Dead:
                color = Color.RED;
                break;
        }

        gc.setFill(color);
        if (health != Person.health.Dead) {
            //Tegn cirkel
            gc.fillOval(position.x-8,position.y-8,16,16);
        }
        else {
            double[] xPoints = new double[12];
            double[] yPoints = new double[12];

            for (int i = 0; i < 12; i++) {
                xPoints[i] = position.x + crossPointsX[i] * 8;
                yPoints[i] = position.y + crossPointsY[i] * 8;
            }

            //Tegn kryds
            gc.fillPolygon(xPoints,yPoints,12);
        }
    }
}
