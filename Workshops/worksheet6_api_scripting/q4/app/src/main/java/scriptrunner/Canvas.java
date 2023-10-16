package scriptrunner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Represents a Swing-based 'drawing surface', on which a client can draw
 * coloured lines. This
 * class provides a simple API consisting of the following methods:
 *
 * colour() - sets the current foreground colour.
 * bgColour() - sets the background colour.
 * moveTo() - sets the current location without drawing anything.
 * lineTo() - draws a line from the current location to a new location, in the
 * current foreground
 * colour. The current location is updated to the new location.
 * registerHandler() - adds an event handler to receive the (x,y) location of
 * mouse clicks.
 * deregisterHandler() - removes a previously-added event handler.
 */
public class Canvas {
    private static class Line {
        int x1, y1, x2, y2;
        Color colour;

        public Line(int x1, int y1, int x2, int y2, Color colour) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.colour = colour;
        }
    }

    private class CanvasPanel extends JPanel {
        public CanvasPanel() {
            // Set up the Swing event handler so that we can, in turn, invoke any scripts'
            // event
            // handlers.
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    int x = event.getX();
                    int y = event.getY();
                    synchronized (mutex) {
                        for (CanvasClickHandler handler : handlers) {
                            handler.onClick(x, y);
                        }
                    }
                    display.repaint();
                }
            });
        }

        // Actually display the lines drawn.
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D gfx = (Graphics2D) g;
            gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            synchronized (mutex) {
                gfx.setBackground(bgColour);
                gfx.clearRect(0, 0, display.getWidth(), display.getHeight());
                for (Line line : lines) {
                    gfx.setColor(line.colour);
                    gfx.drawLine(line.x1, line.y1, line.x2, line.y2);
                }
            }
        }
    }

    private int currentX = 0;
    private int currentY = 0;
    private Color currentColour = Color.WHITE;
    private Color bgColour = Color.BLACK;
    private List<Line> lines = new ArrayList<>();
    private Set<CanvasClickHandler> handlers = new HashSet<>();
    private Object mutex = new Object();
    private JPanel display = new CanvasPanel();

    public Canvas() {
        JFrame window = new JFrame("Canvas");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(800, 800));
        window.getContentPane().add(display);
        window.pack();
        window.setVisible(true);
    }

    /**
     * Use reflection as a short-cut to converting colour names into Color objects,
     * based on the
     * various static fields in the Color class.
     */
    private static Color convertColour(String colourStr, Color defaultValue) {
        try {
            Field f = Color.class.getDeclaredField(colourStr);
            if (f != null && f.getType() == Color.class && Modifier.isStatic(f.getModifiers())) {
                return (Color) f.get(null);
            }
        } catch (ClassCastException | ReflectiveOperationException e) {
            // Fall back to the default value.
        }
        System.out.printf("Unrecognised colour '%s'\n'", colourStr);
        return defaultValue;
    }

    /**
     * API method - sets the foreground colour that will be used next time a line is
     * drawn.
     */
    public void colour(String colour) {
        currentColour = convertColour(colour, currentColour);
    }

    /**
     * API method - sets the background colour.
     */
    public void bgColour(String colour) {
        Color newColour = convertColour(colour, bgColour);
        synchronized (mutex) {
            bgColour = newColour;
        }
        display.repaint();
    }

    /**
     * API method - moves the "current position" without drawing anything.
     */
    public void moveTo(int x, int y) {
        currentX = x;
        currentY = y;
    }

    /**
     * API method - draws a line from the current position to a new point, and
     * updates the
     * current position to that new location.
     */
    public void lineTo(int x, int y) {
        synchronized (mutex) {
            lines.add(new Line(currentX, currentY, x, y, currentColour));
        }
        currentX = x;
        currentY = y;
        display.repaint();
    }

    /**
     * API method - allows a script/plugin to register an event handler for when the
     * mouse is
     * clicked. The handler will receive the (x,y) coordinates of the click.
     */
    public void registerHandler(CanvasClickHandler handler) {
        synchronized (mutex) {
            handlers.add(handler);
        }
    }

    /**
     * API method - allows a script/plugin to un-register a previously-registered
     * event handler.
     */
    public void deregisterHandler(CanvasClickHandler handler) {
        synchronized (mutex) {
            handlers.remove(handler);
        }
    }
}
