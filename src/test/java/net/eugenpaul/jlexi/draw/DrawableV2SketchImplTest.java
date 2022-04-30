package net.eugenpaul.jlexi.draw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

class DrawableV2SketchImplTest {

    private DrawableV2SketchImpl sketch;

    private DrawableV2 element1;
    private DrawableV2 element2;

    @BeforeEach
    void init() {
        sketch = new DrawableV2SketchImpl(Color.BLUE);

        int[] pixelsElememt1 = new int[] { Color.BLACK.getArgb(), Color.GREEN.getArgb() };
        element1 = DrawableV2PixelsImpl.builderArgb()//
                .argbPixels(pixelsElememt1)//
                .size(new Size(1, 2))//
                .build();

        int[] pixelsElememt2 = new int[] { Color.RED.getArgb() };
        element2 = DrawableV2PixelsImpl.builderArgb()//
                .argbPixels(pixelsElememt2)//
                .size(new Size(1, 1))//
                .build();
    }

    @Test
    void test_draw_argb() {
        sketch.addDrawable(element1, 0, 0);
        sketch.addDrawable(element2, 1, 1);

        DrawableV2 draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getArgb(), Color.BLUE.getArgb(), //
                        Color.GREEN.getArgb(), Color.RED.getArgb() //
                }, //
                draw.asArgbPixels()//
        );
    }

    @Test
    void test_draw_toArgbArray() {
        sketch.addDrawable(element1, 0, 0);
        sketch.addDrawable(element2, 1, 1);

        DrawableV2 draw = sketch.draw();
        int[] output = new int[4];
        Arrays.fill(output, Color.BLUE.getArgb());
        draw.toArgbPixels(output, new Size(2, 2), new Vector2d(0, 0));

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getArgb(), Color.BLUE.getArgb(), //
                        Color.GREEN.getArgb(), Color.RED.getArgb() //
                }, //
                output//
        );
    }

    @Test
    void test_draw_rgba() {
        sketch.addDrawable(element1, 0, 0);
        sketch.addDrawable(element2, 1, 1);

        DrawableV2 draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getRgba(), Color.BLUE.getRgba(), //
                        Color.GREEN.getRgba(), Color.RED.getRgba() //
                }, //
                draw.asRgbaPixels()//
        );
    }
}
