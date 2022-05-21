package net.eugenpaul.jlexi.draw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

class DrawableSketchImplTest {

    private DrawableSketchImpl sketch;
    private DrawableSketchImpl sketchFixed;
    private Size sketchFixedSize = new Size(5, 5);

    private DrawableSketchImpl innerSketch;
    private DrawableSketchImpl innerSketchFixed;
    private Size innerSketchFixedSize = new Size(3, 3);

    private Drawable blackGreenPipe;
    private Drawable redDot;

    private final int b = Color.BLACK.getArgb();
    private final int y = Color.YELLOW.getArgb();
    private final int g = Color.GREEN.getArgb();
    private final int w = Color.WHITE.getArgb();
    private final int r = Color.RED.getArgb();
    private final int u = Color.BLUE.getArgb();

    @BeforeEach
    void init() {
        sketch = new DrawableSketchImpl(Color.YELLOW);
        sketchFixed = new DrawableSketchImpl(Color.YELLOW, sketchFixedSize);

        innerSketch = new DrawableSketchImpl(Color.WHITE);
        innerSketchFixed = new DrawableSketchImpl(Color.WHITE, innerSketchFixedSize);

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

    @Test
    void test_draw_argb_fixed_size() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, 1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, y, y, y, y, //
                y, b, w, w, y, //
                y, g, w, r, y, //
                y, w, w, w, y, //
                y, y, y, y, y, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_fixed_size_neggativ_position_inner() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, -1);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, 1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, y, y, y, y, //
                y, g, w, w, y, //
                y, w, w, r, y, //
                y, w, w, w, y, //
                y, y, y, y, y, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_fixed_size_neggativ_position_of_inner() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, -1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, g, w, r, y, //
                y, w, w, w, y, //
                y, y, y, y, y, //
                y, y, y, y, y, //
                y, y, y, y, y, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_fixed_size_with_draw_area() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, -1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = draw.asArgbPixels();
        int[] expectedPixels = new int[] { //
                y, g, w, r, y, //
                y, w, w, w, y, //
                y, y, y, y, y, //
                y, y, y, y, y, //
                y, y, y, y, y, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_to_array_fixed_size() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, 1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = new int[25];
        Arrays.fill(argbPixels, u);

        draw.toArgbPixels(//
                argbPixels, //
                new Size(5, 5), //
                new Area(//
                        new Vector2d(0, 0), //
                        new Size(5, 5) //
                ), //
                new Vector2d(0, 0));

        int[] expectedPixels = new int[] { //
                y, y, y, y, y, //
                y, b, w, w, y, //
                y, g, w, r, y, //
                y, w, w, w, y, //
                y, y, y, y, y, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_to_array_fixed_size_with_draw_area() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), 1, 1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = new int[25];
        Arrays.fill(argbPixels, u);

        draw.toArgbPixels(//
                argbPixels, //
                new Size(5, 5), //
                new Area(//
                        new Vector2d(1, 1), //
                        new Size(3, 3) //
                ), //
                new Vector2d(0, 0));

        int[] expectedPixels = new int[] { //
                u, u, u, u, u, //
                u, y, y, y, u, //
                u, y, b, w, u, //
                u, y, g, w, u, //
                u, u, u, u, u, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_to_array_fixed_size_with_draw_area_offset() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), -1, -1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = new int[25];
        Arrays.fill(argbPixels, u);

        draw.toArgbPixels(//
                argbPixels, //
                new Size(5, 5), //
                new Area(//
                        new Vector2d(1, 1), //
                        new Size(3, 3) //
                ), //
                new Vector2d(1, 1));

        int[] expectedPixels = new int[] { //
                u, u, u, u, u, //
                u, u, u, u, u, //
                u, u, w, r, u, //
                u, u, w, w, u, //
                u, u, u, u, u, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_to_array_fixed_size_with_draw_area_offset_2() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), -1, -1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = new int[25];
        Arrays.fill(argbPixels, u);

        draw.toArgbPixels(//
                argbPixels, //
                new Size(5, 5), //
                new Area(//
                        new Vector2d(1, 1), //
                        new Size(3, 3) //
                ), //
                new Vector2d(-1, -1));

        int[] expectedPixels = new int[] { //
                u, u, u, u, u, //
                u, w, y, y, u, //
                u, y, y, y, u, //
                u, y, y, y, u, //
                u, u, u, u, u, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

    @Test
    void test_draw_argb_to_array_fixed_size_with_draw_area_offset_3() {
        innerSketchFixed.addDrawable(blackGreenPipe, 0, 0);
        innerSketchFixed.addDrawable(redDot, 2, 1);

        sketchFixed.addDrawable(innerSketchFixed.draw(), -1, -1);

        Drawable draw = sketchFixed.draw();

        int[] argbPixels = new int[25];
        Arrays.fill(argbPixels, u);

        draw.toArgbPixels(//
                argbPixels, //
                new Size(5, 5), //
                new Area(//
                        new Vector2d(1, 1), //
                        new Size(4, 4) //
                ), //
                new Vector2d(-3, -3));

        int[] expectedPixels = new int[] { //
                u, u, u, u, u, //
                u, y, y, u, u, //
                u, y, y, u, u, //
                u, u, u, u, u, //
                u, u, u, u, u, //
        }; //

        assertArrayEquals(expectedPixels, argbPixels);
    }

}
