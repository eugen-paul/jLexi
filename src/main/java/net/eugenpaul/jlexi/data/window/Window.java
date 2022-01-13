package net.eugenpaul.jlexi.data.window;

/**
 * Interface for the system-dependent implementation of a window.
 */
public interface Window {
    public void redraw();

    public void raise();

    public void lower();

    public void iconify();

    public void deiconify();

    public void drawLine();

    public void drawRect();

    public void drawPolygon();

    public void drawText();
}
