package net.eugenpaul.jlexi.resourcesmanager.textformat.params;

import java.util.Arrays;

import lombok.Getter;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

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
    public void doFormat(DrawableSketch draw) {
        Size size;
        int[] pixels;

        if (type == FormatUnderlineType.SINGLE) {
            size = new Size(draw.getSize().getWidth(), 1);
            pixels = new int[(int) size.compArea()];
            Arrays.fill(pixels, color.getArgb());
            DrawablePixelsImpl underline = DrawablePixelsImpl.builderArgb().argbPixels(pixels).size(size).build();
            draw.addDrawable(underline, 0, draw.getSize().getHeight() - 1);
        } else if (type == FormatUnderlineType.DOUBLE) {
            size = new Size(draw.getSize().getWidth(), 3);
            pixels = new int[(int) size.compArea()];
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
            DrawablePixelsImpl underline = DrawablePixelsImpl.builderArgb().argbPixels(pixels).size(size).build();
            draw.addDrawable(underline, 0, draw.getSize().getHeight() - 3);
        }
    }

}
