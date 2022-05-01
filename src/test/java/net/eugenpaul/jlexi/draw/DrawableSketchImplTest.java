package net.eugenpaul.jlexi.draw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

class DrawableSketchImplTest {

    private DrawableSketchImpl sketch;

    private DrawableSketchImpl innerSketch;
    private Drawable blackGreenPipe;
    private Drawable redDot;

    @BeforeEach
    void init() {
        sketch = new DrawableSketchImpl(Color.BLUE);
        innerSketch = new DrawableSketchImpl(Color.WHITE);

        int[] pixelsElememt1 = new int[] { Color.BLACK.getArgb(), Color.GREEN.getArgb() };
        blackGreenPipe = DrawablePixelsImpl.builderArgb()//
                .argbPixels(pixelsElememt1)//
                .size(new Size(1, 2))//
                .build();

        int[] pixelsElememt2 = new int[] { Color.RED.getArgb() };
        redDot = DrawablePixelsImpl.builderArgb()//
                .argbPixels(pixelsElememt2)//
                .size(new Size(1, 1))//
                .build();
    }

    @Test
    void test_draw_argb() {
        sketch.addDrawable(blackGreenPipe, 0, 0);
        sketch.addDrawable(redDot, 1, 1);

        Drawable draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getArgb(), Color.BLUE.getArgb(), //
                        Color.GREEN.getArgb(), Color.RED.getArgb() //
                }, //
                draw.asArgbPixels()//
        );
    }

    @Test
    void test_draw_argb_with_innerSketh() {
        innerSketch.addDrawable(blackGreenPipe, 0, 0);
        innerSketch.addDrawable(redDot, 1, 1);

        sketch.addDrawable(innerSketch.draw(), 0, 0);

        Drawable draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getArgb(), Color.WHITE.getArgb(), //
                        Color.GREEN.getArgb(), Color.RED.getArgb() //
                }, //
                draw.asArgbPixels()//
        );
    }

    @Test
    void test_draw_argb_with_innerSketh_and_bg() {
        innerSketch.addDrawable(blackGreenPipe, 0, 0);
        innerSketch.addDrawable(redDot, 2, 1);

        sketch.addDrawable(innerSketch.draw(), 1, 1);

        Drawable draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLUE.getArgb(), Color.BLUE.getArgb(), Color.BLUE.getArgb(), Color.BLUE.getArgb(), //
                        Color.BLUE.getArgb(), Color.BLACK.getArgb(), Color.WHITE.getArgb(), Color.WHITE.getArgb(), //
                        Color.BLUE.getArgb(), Color.GREEN.getArgb(), Color.WHITE.getArgb(), Color.RED.getArgb() //
                }, //
                draw.asArgbPixels()//
        );
    }

    @Test
    void test_draw_toArgbArray() {
        sketch.addDrawable(blackGreenPipe, 0, 0);
        sketch.addDrawable(redDot, 1, 1);

        Drawable draw = sketch.draw();
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
        sketch.addDrawable(blackGreenPipe, 0, 0);
        sketch.addDrawable(redDot, 1, 1);

        Drawable draw = sketch.draw();

        assertArrayEquals(//
                new int[] { //
                        Color.BLACK.getRgba(), Color.BLUE.getRgba(), //
                        Color.GREEN.getRgba(), Color.RED.getRgba() //
                }, //
                draw.asRgbaPixels()//
        );
    }
}
