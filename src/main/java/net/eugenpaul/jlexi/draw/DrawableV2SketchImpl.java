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

    public DrawableV2SketchImpl() {
        data = new DrawableV2StorageImpl();

        minX = 0;
        maxX = 0;
        minY = 0;
        maxY = 0;
    }

    @Override
    public void addDrawable(DrawableV2 drawable, int x, int y, int z) {
        this.data.add(drawable, x, y, z);

        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x + drawable.getSize().getWidth());

        minY = Math.min(minY, y);
        maxY = Math.max(maxY, y + drawable.getSize().getHeight());
    }

    @Override
    public DrawableV2 draw() {
        return new DrawableV2AreasImpl(//
                data.copy(), //
                new Area(//
                        new Vector2d(minX, minY), //
                        new Size(maxX - minX + 1, maxY - minY + 1)//
                )//
        );
    }

    @Override
    public DrawableV2 draw(Area area) {
        return new DrawableV2AreasImpl(//
                data.copy(), //
                area//
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
        return new Size(maxX - minX, maxY - minY);
    }

}
