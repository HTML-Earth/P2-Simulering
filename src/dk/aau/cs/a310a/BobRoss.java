package dk.aau.cs.a310a;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class BobRoss
{
    public void drawCircle(int xCenter, int yCenter, int radius, Color col, PixelWriter px)
    {
        int x, y, r2;

        r2 = radius * radius;
        for (x = -radius; x <= radius; x++) {
            y = (int) (Math.sqrt(r2 - x*x) + 0.5);

            for (int yi = -y; yi <= y; yi++)
            {
                px.setColor(xCenter + x, yCenter + yi, col);
            }

            //px.setColor(xCenter + x, yCenter + y, col);
            //px.setColor(xCenter + x, yCenter - y, col);
        }
    }
}
