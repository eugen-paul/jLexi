package net.eugenpaul.jlexi.data.window;

public abstract class Windowlmp implements Window {

    private Windowlmp impl;

    protected Windowlmp(Windowlmp impl) {
        this.impl = impl;
    }

    public abstract void deviceRedraw();

    public abstract void deviceRaise();

    public abstract void deviceLower();

    public abstract void deviceIconify();

    public abstract void deviceDeiconify();

    public abstract void deviceDrawLine();

    public abstract void deviceDrawRect();

    public abstract void deviceDrawPolygon();

    public abstract void deviceDrawText();

    @Override
    public void redraw() {
        impl.deviceRedraw();
    }

    @Override
    public void raise() {
        impl.deviceRaise();
    }

    @Override
    public void lower() {
        impl.deviceLower();
    }

    @Override
    public void iconify() {
        impl.deviceIconify();
    }

    @Override
    public void deiconify() {
        impl.deviceDeiconify();
    }

    @Override
    public void drawLine() {
        impl.deviceDrawLine();
    }

    @Override
    public void drawRect() {
        impl.deviceDrawRect();
    }

    @Override
    public void drawPolygon() {
        impl.deviceDrawPolygon();
    }

    @Override
    public void drawText() {
        impl.deviceDrawText();
    }
}
