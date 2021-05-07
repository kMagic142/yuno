package ro.kmagic.utils.profile;

import java.awt.*;

public class GFX {

    public static void addCenterText(String text, Font font, int x, int y, Graphics g, Color color) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, Math.max(0, x - ((int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2)), y);
    }

    public static void addText(String text, Font font, int x, int y, Graphics g, Color color) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, x, y);
    }

}
