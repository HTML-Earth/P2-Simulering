package dk.aau.cs.a310a.GUI;

import dk.aau.cs.a310a.Grid.GridPosition;
import dk.aau.cs.a310a.Grid.Vector;
import dk.aau.cs.a310a.Simulation.Person;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BobRoss {
    Vector[] crossPoints;

    Color susceptibleColor;
    Color infectedColor;
    Color recoveredColor;
    Color deadColor;

    Image background;
    GraphicsContext gc;

    boolean debugMode = false;

    public BobRoss() {
        crossPoints = new Vector[12];

        //Top Left
        crossPoints[0] = new Vector(-1.0,-1.0);
        crossPoints[1] = new Vector(-0.8,-1.0);

        //Middle up
        crossPoints[2] = new Vector(0.0,-0.2);

        //Top right
        crossPoints[3] = new Vector(0.8,-1.0);
        crossPoints[4] = new Vector(1.0,-1.0);

        //Middle right
        crossPoints[5] = new Vector(0.2,0.0);

        //Bottom right
        crossPoints[6] = new Vector(1.0,1.0);
        crossPoints[7] = new Vector(0.8,1.0);

        //Middle down
        crossPoints[8] = new Vector(0.0,0.2);

        //Bottom left
        crossPoints[9] = new Vector(-0.8,1.0);
        crossPoints[10] = new Vector(-1.0,1.0);

        //Middle left
        crossPoints[11] = new Vector(-0.2,0.0);

        susceptibleColor = new Color(0,1,1,0.5);
        infectedColor    = new Color(1,0,0,0.5);
        recoveredColor   = new Color(1,1,0,0.5);
        deadColor        = new Color(1,0,0,0.5);
    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setBackground(Image bg) {
        background = bg;
        drawBackground();
    }

    public void drawBackground() {
        if (gc == null)
            return;
        if (background == null)
            return;

        gc.drawImage(background,0,0,800,600);
    }

    public void drawPerson(Person person) {
        Person.health health = person.getCurrentHealth();
        Vector position = person.getPosition();

        Color color = Color.BLACK;
        switch (health) {
            case Susceptible:
                color = susceptibleColor;
                break;
            case Infected:
                color = infectedColor;
                break;
            case Recovered:
                color = recoveredColor;
                break;
            case Dead:
                color = deadColor;
                break;
        }

        gc.setFill(color);
        if (health != Person.health.Dead) {
            //Tegn cirkel
            gc.fillOval(position.x-8,position.y-8,16,16);

            gc.setFill(Color.BLACK);
            if (debugMode) {
                gc.fillText(person.getDebugText(), position.x - 8, position.y - 8);

                if (person.getCurrentPath() != null && person.getCurrentPath().size() > 0) {
                    for (GridPosition gp: person.getCurrentPath()) {
                        Color c = new Color(1,1,1,0.2);
                        gc.setFill(c);
                        gc.fillOval(Vector.gridToScreen(gp).x-2,Vector.gridToScreen(gp).y-2,4,4);
                    }
                    drawDestination(person.getPosition(), Vector.gridToScreen(person.getCurrentPath().getLast()));
                }
            }


        }
        else {
            double[] xPoints = new double[12];
            double[] yPoints = new double[12];

            for (int i = 0; i < 12; i++) {
                xPoints[i] = position.x + crossPoints[i].x * 8;
                yPoints[i] = position.y + crossPoints[i].y * 8;
            }

            //Tegn kryds
            gc.fillPolygon(xPoints,yPoints,12);
        }
    }

    public void drawDestination(Vector position, Vector destination) {
        gc.setFill(Color.LIGHTGREEN);
        gc.strokeLine(position.x,position.y,destination.x,destination.y);
        gc.fillOval(destination.x-4,destination.y-4,8,8);
    }

    public Image resizeImage(String resourceUrl, int scale) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceUrl).getFile());
        return resizeImage(file, scale);
    }

    public Image resizeImage(File img, int scale) throws IOException {
        BufferedImage pixelMap;
        pixelMap = ImageIO.read(img);

        int width = pixelMap.getWidth();
        int height = pixelMap.getHeight();

        int scaledWidth = width * scale;
        int scaledHeight = height * scale;

        BufferedImage scaledMap = new BufferedImage(scaledWidth, scaledHeight, pixelMap.getType());
        for (int y = 0; y < scaledHeight; y++) {
            for (int x = 0; x < scaledWidth; x++) {
                int color = pixelMap.getRGB(x / scale,y / scale);
                scaledMap.setRGB(x,y,color);
            }
        }

        return SwingFXUtils.toFXImage(scaledMap, null);
    }
}
