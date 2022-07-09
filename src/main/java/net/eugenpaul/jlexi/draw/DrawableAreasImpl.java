package net.eugenpaul.jlexi.draw;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.ColorType;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.CollisionHelper;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class DrawableAreasImpl implements Drawable {

    private DrawableStorage data;
    @Getter
    @Setter
    private Color background;

    private int[] argbPixels;
    private int[] rgbaPixels;

    private Area area;

    public DrawableAreasImpl(DrawableStorage data, Area area, Color background) {
        this.area = area;
        this.data = data;
        this.background = background;

        this.argbPixels = null;
        this.rgbaPixels = null;
    }

    @Override
    public int[] asArgbPixels() {
        if (this.argbPixels != null) {
            return this.argbPixels;
        }

        this.argbPixels = new int[(int) this.area.getSize().compArea()];
        Arrays.fill(this.argbPixels, this.background.getArgb());

        this.data.forEach((d, v) -> d.toArgbPixels(//
                this.argbPixels, //
                getSize(), //
                this.area, //
                v //
        ));

        return this.argbPixels.clone();
    }

    @Override
    public void toArgbPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        toPixels(ColorType.ARGB, dest, destSize, drawArea, relativePos);
    }

    @Override
    public int[] asRgbaPixels() {
        if (this.rgbaPixels != null) {
            return this.rgbaPixels;
        }

        this.rgbaPixels = new int[(int) this.area.getSize().compArea()];
        Arrays.fill(this.rgbaPixels, this.background.getArgb());

        this.data.forEach((d, v) -> d.toRgbaPixels(//
                this.rgbaPixels, //
                getSize(), //
                this.area, //
                v //
        ));

        return this.rgbaPixels.clone();
    }

    @Override
    public void toRgbaPixels(int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        toPixels(ColorType.RBGA, dest, destSize, drawArea, relativePos);
    }

    @Override
    public void toPixels(ColorType colorType, int[] dest, Size destSize, Area drawArea, Vector2d relativePos) {
        Vector2d absolutDrawPosition = drawArea.getPosition().addNew(relativePos);

        Area finalDrawArea = CollisionHelper.getOverlapping(//
                drawArea.getPosition(), //
                drawArea.getSize(), //
                absolutDrawPosition, //
                getSize() //
        );

        if (!background.equals(Color.INVISIBLE)) {
            ImageArrayHelper.fillRectangle(//
                    background, //
                    colorType, //
                    dest, //
                    destSize, //
                    finalDrawArea.getSize(), //
                    finalDrawArea.getPosition() //
            );
        }

        this.data.forEach((d, v) -> d.toPixels(//
                colorType, //
                dest, //
                destSize, //
                finalDrawArea, //
                new Vector2d( //
                        Math.min(v.getX(), v.getX() + relativePos.getX()), //
                        Math.min(v.getY(), v.getY() + relativePos.getY()) //
                )//
        ));
    }

    @Override
    public Color[] asColorPixels() {
        // TODO
        throw new NotYetImplementedException();
    }

    @Override
    public Size getSize() {
        return area.getSize();
    }
}
