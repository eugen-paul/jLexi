package net.eugenpaul.jlexi.resourcesmanager.textformat.params;

import java.util.Arrays;

import lombok.Getter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class FormatUnderline implements PixelsFormatter {

    @Getter
    private FormatUnderlineType type;

    @Getter
    private Color color;

    public FormatUnderline(FormatUnderlineType type, Color color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public void doFormat(Drawable draw) {
        Size size;
        int[] pixels;
        if (type == FormatUnderlineType.SINGLE) {
            size = new Size(draw.getPixelSize().getWidth(), 1);
            pixels = new int[size.getWidth() * size.getHeight()];
            Arrays.fill(pixels, color.getArgb());
            ImageArrayHelper.copyRectangle(pixels, size, draw, new Vector2d(0, draw.getPixelSize().getHeight() - 1));
        } else if (type == FormatUnderlineType.DOUBLE) {
            size = new Size(draw.getPixelSize().getWidth(), 3);
            pixels = new int[size.getWidth() * size.getHeight()];
            Arrays.fill(//
                    pixels, //
                    pixels.length - 3 * size.getWidth(), //
                    pixels.length - 2 * size.getWidth(), //
                    color.getArgb()//
            );
            Arrays.fill(//
                    pixels, //
                    pixels.length - 1 * size.getWidth(), //
                    pixels.length, //
                    color.getArgb() //
            );
            ImageArrayHelper.copyRectangle(pixels, size, draw, new Vector2d(0, draw.getPixelSize().getHeight() - 3));
        }

    }

}
