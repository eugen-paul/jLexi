package net.eugenpaul.jlexi.draw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

class DrawableSketchImplTest {

    private DrawableSketchImpl sketch;

    private DrawableSketchImpl innerSketch;
    private Drawable blackGreenPipe;
    private Drawable redDot;

    private final int b = Color.BLACK.getArgb();
    private final int y = Color.YELLOW.getArgb();
    private final int g = Color.GREEN.getArgb();
    private final int w = Color.WHITE.getArgb();
    private final int r = Color.RED.getArgb();

    @BeforeEach
    void init() {
        sketch = new DrawableSketchImpl(Color.YELLOW);
        innerSketch = new DrawableSketchImpl(Color.WHITE);

        int[] pixelsElememt1 = new int[] { b, g };
        blackGreenPipe = DrawablePixelsImpl.builderArgb()//
                .argbPixels(pixelsElememt1)//
                .size(new Size(1, 2))//
                .build();

        int[] pixelsElememt2 = new int[] { r };
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
                        b, y, //
                        g, r //
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

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                b, w, //
                g, r //
        };

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_with_innerSketh_and_bg() {
        innerSketch.addDrawable(blackGreenPipe, 0, 0);
        innerSketch.addDrawable(redDot, 2, 1);

        sketch.addDrawable(innerSketch.draw(), 1, 1);

        Drawable draw = sketch.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, y, y, y, //
                y, b, w, w, //
                y, g, w, r //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_with_negativ_position() {
        innerSketch.addDrawable(blackGreenPipe, 0, -1);
        innerSketch.addDrawable(redDot, 2, 1);

        sketch.addDrawable(innerSketch.draw(), 1, 1);

        Drawable draw = sketch.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, y, y, y, //
                y, g, w, w, //
                y, w, w, r //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    // @Test
    // void test_draw_toArgbArray() {
    // sketch.addDrawable(blackGreenPipe, 0, 0);
    // sketch.addDrawable(redDot, 1, 1);

    // Drawable draw = sketch.draw();
    // int[] output = new int[4];
    // Arrays.fill(output, y);
    // draw.toArgbPixels(output, new Size(2, 2), new Vector2d(0, 0));

    // assertArrayEquals(//
    // new int[] { //
    // b(), y, //
    // g, r //
    // }, //
    // output//
    // );
    // }

    // @Test
    // void test_draw_rgba() {
    //     sketch.addDrawable(blackGreenPipe, 0, 0);
    //     sketch.addDrawable(redDot, 1, 1);

    //     Drawable draw = sketch.draw();

    //     assertArrayEquals(//
    //             new int[] { //
    //                     Color.BLACK.getRgba(), Color.YELLOW.getRgba(), //
    //                     Color.GREEN.getRgba(), Color.RED.getRgba() //
    //             }, //
    //             draw.asRgbaPixels()//
    //     );
    // }
}
