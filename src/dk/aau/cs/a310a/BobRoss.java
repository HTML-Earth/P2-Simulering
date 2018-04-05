package dk.aau.cs.a310a;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class BobRoss {
    public void drawBufferedImage(BufferedImage img, int x, int y, int w, int h, PixelWriter pw) {
        int imgW = img.getWidth();
        int imgH = img.getHeight();
        for (int ix = x; ix < x + w; ix++) {
            for (int iy = y; iy < y + h; iy++) {
                int argb = img.getRGB((ix - x) * imgW/w,(iy - y) * imgH/h);
                int r = (argb>>16)&0xFF;
                int g = (argb>>8)&0xFF;
                int b = (argb>>0)&0xFF;
                Color color = Color.rgb(r,g,b);
                if (color.isOpaque()) {
                    pw.setColor(x + ix, y + iy, color);
                }
            }
        }
    }

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

    public void drawCross(Vector center, int size, Color col, PixelWriter pw) {
        for (int j = -1; j <= 1; j++) {
            for (int i = -size; i <= size; i++) {
                pw.setColor((int)center.x + i + j, (int)center.y + i, col);
                pw.setColor((int)center.x - i + j, (int)center.y + i, col);
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
