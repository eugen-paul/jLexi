package net.eugenpaul.jlexi.draw;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class DrawableV2SketchImpl implements DrawableV2Sketch {

    private DrawableV2Storage data;

    @Getter
    @Setter
    private Color background;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private Size size;

    public DrawableV2SketchImpl(Color background) {
        this.data = new DrawableV2StorageImpl();

        this.size = null;

        this.minX = 0;
        this.maxX = 0;
        this.minY = 0;
        this.maxY = 0;

        this.background = background;
    }

    public DrawableV2SketchImpl(Color background, Size size) {
        this.data = new DrawableV2StorageImpl();

        this.size = size;

        this.minX = 0;
        this.maxX = 0;
        this.minY = 0;
        this.maxY = 0;

        this.background = background;
    }

    @Override
    public void addDrawable(DrawableV2 drawable, int x, int y, int z) {
        this.data.add(drawable, x, y, z);

        if (size != null) {
            this.minX = Math.min(this.minX, x);
            this.maxX = Math.max(this.maxX, x + drawable.getSize().getWidth() - 1);

            this.minY = Math.min(this.minY, y);
            this.maxY = Math.max(this.maxY, y + drawable.getSize().getHeight() - 1);
        }
    }

    @Override
    public DrawableV2 draw() {
        if (size != null) {
            return new DrawableV2AreasImpl(//
                    this.data.copy(), //
                    new Area(//
                            new Vector2d(this.minX, this.minY), //
                            size //
                    ), //
                    this.background //
            );
        } else {
            return new DrawableV2AreasImpl(//
                    this.data.copy(), //
                    new Area(//
                            new Vector2d(this.minX, this.minY), //
                            new Size(this.maxX - this.minX + 1, this.maxY - this.minY + 1)//
                    ), //
                    this.background //
            );
        }
    }

    @Override
    public DrawableV2 draw(Area area) {
        // TODO edit size
        return new DrawableV2AreasImpl(//
                this.data.copy(), //
                area, //
                this.background //
        );
    }

    @Override
    public DrawableV2Sketch sketchAt(Area area) {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    @Override
    public DrawableV2Sketch copy() {
        // TODO Auto-generated method stub
        throw new NotYetImplementedException();
    }

    @Override
    public Size getSize() {
        if (size != null) {
            return size;
        }
        return new Size(this.maxX - this.minX + 1, this.maxY - this.minY + 1);
    }

}
