package dk.aau.cs.a310a;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class BobRoss {
    public void drawCircle(Vector center, int radius, Color col, PixelWriter pw) {
        int x, y, r2;

        r2 = radius * radius;
        for (x = -radius; x <= radius; x++) {
            y = (int) (Math.sqrt(r2 - x * x) + 0.5);

            for (int yi = -y; yi <= y; yi++) {
                pw.setColor((int) center.x + x, (int) center.y + yi, col);
            }
        }
    }

    public void drawRectangle(int x1, int x2, int y1, int y2, Color col, PixelWriter pw) {
        int xlow = (x1 < x2) ? x1 : x2;
        int xhigh = (x1 > x2) ? x1 : x2;
        int ylow = (y1 < y2) ? y1 : y2;
        int yhigh = (y1 > y2) ? y1 : y2;

        for (int x = xlow; x <= xhigh; x++) {
            for (int y = ylow; y <= yhigh; y++) {
                pw.setColor(x, y, col);
            }
        }
    }
}
