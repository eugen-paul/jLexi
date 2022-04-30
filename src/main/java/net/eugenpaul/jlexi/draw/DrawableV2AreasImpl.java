package net.eugenpaul.jlexi.draw;

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

    private int[] argbPixels = null;
    private int[] rgbaPixels = null;

    private Area area;

    public DrawableV2AreasImpl(DrawableV2Storage data, Area area) {
        this.area = area;
        this.data = data;
    }

    @Override
    public int[] asArgbPixels() {
        if (this.argbPixels != null) {
            return this.argbPixels;
        }

        this.argbPixels = new int[(int) this.area.getSize().compArea()];

        data.forEach((d, v) -> ImageArrayHelper.copyRectangle(//
                d.asArgbPixels(), //
                d.getSize(), //
                Vector2d.zero(), //
                d.getSize(), //
                this.argbPixels, //
                this.area.getSize(), //
                v //
        ));

        return this.argbPixels.clone();
    }

    @Override
    public int[] asRgbaPixels() {
        if (this.rgbaPixels != null) {
            return this.rgbaPixels;
        }

        this.rgbaPixels = new int[(int) this.area.getSize().compArea()];

        data.forEach((d, v) -> ImageArrayHelper.copyRectangle(//
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
