package net.eugenpaul.jlexi.draw;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class DrawableSketchImpl implements DrawableSketch {

    private DrawableStorage data;

    @Getter
    @Setter
    private Color background;

    private int maxX;
    private int maxY;

    private Size size;

    public DrawableSketchImpl(Color background) {
        this(background, null);
    }

    public DrawableSketchImpl(Color background, Size size) {
        this.data = new DrawableStorageImpl();

        this.size = size;

        this.maxX = 0;
        this.maxY = 0;

        this.background = background;
    }

    @Override
    public void addDrawable(Drawable drawable, int x, int y, int z) {
        this.data.add(drawable, x, y, z);

        if (size == null) {
            this.maxX = Math.max(this.maxX, x + drawable.getSize().getWidth() - 1);
            this.maxY = Math.max(this.maxY, y + drawable.getSize().getHeight() - 1);
        }
    }

    @Override
    public Drawable draw() {
        return new DrawableAreasImpl(//
                this.data.copy(), //
                new Area(//
                        Vector2d.zero(), //
                        getSize() //
                ), //
                this.background //
        );
    }

    @Override
    public Drawable draw(Area area) {
        // TODO edit size
        return new DrawableAreasImpl(//
                this.data.copy(), //
                area, //
                this.background //
        );
    }

    @Override
    public DrawableSketch sketchAt(Area area) {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    @Override
    public DrawableSketch copy() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    @Override
    public Size getSize() {
        if (size != null) {
            return size;
        }
        return new Size(this.maxX + 1, this.maxY + 1);
    }

}
