package net.eugenpaul.jlexi.resourcesmanager.textformatter;

import java.util.Arrays;

import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.resourcesmanager.PixelsFormatter;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class Underline implements PixelsFormatter {

    private static final int LINE_COLOR = 0xFF000000;
    private UnderlineType type;

    public Underline(UnderlineType type) {
        this.type = type;
    }

    @Override
    public void doFormat(Drawable draw) {
        Size size;
        int[] pixels;
        if (type == UnderlineType.SINGLE) {
            size = new Size(draw.getPixelSize().getWidth(), 1);
            pixels = new int[size.getWidth() * size.getHeight()];
            Arrays.fill(pixels, LINE_COLOR);
            ImageArrayHelper.copyRectangle(pixels, size, draw, new Vector2d(0, draw.getPixelSize().getHeight() - 1));
        } else if (type == UnderlineType.DOUBLE) {
            size = new Size(draw.getPixelSize().getWidth(), 3);
            pixels = new int[size.getWidth() * size.getHeight()];
            Arrays.fill(pixels, pixels.length - 3 * size.getWidth(), pixels.length - 2 * size.getWidth(), LINE_COLOR);
            Arrays.fill(pixels, pixels.length - 1 * size.getWidth(), pixels.length, LINE_COLOR);
            ImageArrayHelper.copyRectangle(pixels, size, draw, new Vector2d(0, draw.getPixelSize().getHeight() - 3));
        }

    }

}
