package net.eugenpaul.jlexi.draw;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class DrawableV2AreasImpl implements DrawableV2 {

    private DrawableV2Storage data;
    @Getter
    @Setter
    private Color background;

    private int[] argbPixels;
    private int[] rgbaPixels;

    private Area area;

    public DrawableV2AreasImpl(DrawableV2Storage data, Area area, Color background) {
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
                this.area.getSize(), //
                v //
        ));

        return this.argbPixels.clone();
    }

    @Override
    public void toArgbPixels(int[] dest, Size destSize, Vector2d position) {
        if (this.argbPixels != null) {
            ImageArrayHelper.copyRectangle(//
                    this.argbPixels, //
                    this.area.getSize(), //
                    this.area.getPosition(), //
                    this.area.getSize(), //
                    dest, //
                    destSize, //
                    position //
            );
        }

        this.data.forEach((d, v) -> d.toArgbPixels(//
                dest, //
                destSize, //
                position.addNew(v) //
        ));
    }

    @Override
    public int[] asRgbaPixels() {
        if (this.rgbaPixels != null) {
            return this.rgbaPixels;
        }

        this.rgbaPixels = new int[(int) this.area.getSize().compArea()];
        Arrays.fill(this.rgbaPixels, this.background.getRgba());

        this.data.forEach((d, v) -> ImageArrayHelper.copyRectangle(//
                d.asRgbaPixels(), //
                d.getSize(), //
                Vector2d.zero(), //
                d.getSize(), //
                this.rgbaPixels, //
                this.area.getSize(), //
                v //
        ));
        return this.rgbaPixels.clone();
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
