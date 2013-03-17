import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.powerbot.game.api.methods.input.Mouse;

/*
 * Paint API
 * Made By Baseball435 - 2/4/2013
 */

public class PaintAPI {

    /**
     * Draws a rectangle given the provided parameters.
     */
    public static void drawRectangle(Graphics2D g2d, int x, int y, int width, int height, Color color, boolean fill) {
        g2d.setColor(color);
        if (fill)
            g2d.fillRect(x, y, width, height);
        else
            g2d.drawRect(x, y, width, height);
    }

    /**
     * Draws an oval given the provided parameters.
     */
    public static void drawOval(Graphics2D g2d, int x, int y, int width, int height, Color color, boolean fill) {
        g2d.setColor(color);
        if (fill)
            g2d.fillOval(x, y, width, height);
        else
            g2d.drawOval(x, y, width, height);
    }

    /**
     * Draws a transparent rectangle given the provided parameters. The last value, the alpha value, is a float that is from 0.0f to 1.0f.
     */
    public static void drawTransparentRectangle(Graphics2D g2d, int x, int y, int width, int height, Color color, boolean fill, float alpha) {
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        if (fill)
            g2d.fillRect(x, y, width, height);
        else
            g2d.drawRect(x, y, width, height);
        g2d.setComposite(old);
    }

    /**
     * Draws a transparent oval given the provided parameters. The last value, the alpha value, is a float that is from 0.0f to 1.0f.
     */
    public static void drawTransparentOval(Graphics2D g2d, int x, int y, int width, int height, Color color, boolean fill, float alpha) {
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(color);
        if (fill)
            g2d.fillOval(x, y, width, height);
        else
            g2d.drawOval(x, y, width, height);
        g2d.setComposite(old);
    }

    /**
     * Draws a string.
     */
    public static void drawString(Graphics2D g2d, String text, int x, int y, Color color) {
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    /**
     * Draws a string with a specific font.
     */
    public static void drawString(Graphics2D g2d, String text, int x, int y, Font font, Color color) {
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    /**
     * Draws a string with a customizable font.
     */
    public static void drawString(Graphics2D g2d, String text, int x, int y, Color color, String fontName, int type, int size) {
        g2d.setFont(new Font(fontName, type, size));
        g2d.setColor(color);
        g2d.drawString(text, x, y);
    }

    /**
     * Draws a centered string at the coordinates given.
     */
    public static void drawCenteredString(Graphics2D g2d, String text, int x, int y, Color color) {
        FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
        int strWidth = fm.stringWidth(text);
        g2d.setColor(color);
        g2d.drawString(text, x - (strWidth / 2), y);
    }

    /**
     * Draws a centered string at the coordinates given with a specific font.
     */
    public static void drawCenteredString(Graphics2D g2d, String text, int x, int y, Font font, Color color) {
        FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
        int strWidth = fm.stringWidth(text);
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x - (strWidth / 2), y);
    }

    /**
     * Draws a centered string at the coordinates given with a customizable font.
     */
    public static void drawCenteredString(Graphics2D g2d, String text, int x, int y, Color color, String fontName, int type, int size) {
        FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
        int strWidth = fm.stringWidth(text);
        g2d.setFont(new Font(fontName, type, size));
        g2d.setColor(color);
        g2d.drawString(text, x - (strWidth / 2), y);
    }
}

abstract class Clickable {

    private int x, y, width, height;

    public Clickable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Checks if the mouse was clicked and the coordinates are inside
     * @return boolean
     */
    public boolean didClick() {
        if (Mouse.isPressed()) {
            int x = Mouse.getX();
            int y = Mouse.getY();
            return new Rectangle(x, y, 1, 1).intersects(this.x, this.y, this.width, this.height);
        }
        return false;
    }

}

abstract class Screen {

    public abstract void update();
    public abstract void draw(Graphics2D g2d);

}

class ScreenManager {

    private static Screen currentScreen, lastScreen;

    public static void showScreen(Screen screen) {
        lastScreen = currentScreen;
        currentScreen = screen;
    }

    public static void updateScreen() {
        if (currentScreen != null)
            currentScreen.update();
    }

    public static void drawScreen(Graphics2D g2d) {
        if (currentScreen != null)
            currentScreen.draw(g2d);
    }

    public static void showLastScreen() {
        currentScreen = lastScreen;
    }

}