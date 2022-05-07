package net.eugenpaul.jlexi.draw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class DrawablePixelsImplTest {

    private final int w = Color.WHITE.getArgb();
    private final int b = Color.BLACK.getArgb();

    DrawablePixelsImpl toDraw;

    int[] fullAreaToDraw;
    Size sizeOfFullAreaToDraw;

    @BeforeEach
    void init() {
        fullAreaToDraw = new int[25];
        Arrays.fill(fullAreaToDraw, w);
        sizeOfFullAreaToDraw = new Size(5, 5);

        int[] toDrawData = new int[9];
        Arrays.fill(toDrawData, b);

        toDraw = DrawablePixelsImpl.builderArgb()//
                .argbPixels(toDrawData)//
                .size(new Size(3, 3))//
                .build();
    }

    @Test
    void test_ToArgbPixels() {
        int[] expectedArea = { //
                b, b, b, w, w, //
                b, b, b, w, w, //
                b, b, b, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w,//
        };

        Area drawArea = new Area(//
                new Vector2d(0, 0), //
                sizeOfFullAreaToDraw//
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, Vector2d.zero());

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_1() {
        int[] expectedArea = { //
                w, w, w, w, w, //
                w, b, b, b, w, //
                w, b, b, b, w, //
                w, b, b, b, w, //
                w, w, w, w, w,//
        };

        Area drawArea = new Area(//
                new Vector2d(1, 1), //
                new Size(4, 4) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, Vector2d.zero());

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_2() {
        int[] expectedArea = { //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, b, b, b, //
                w, w, b, b, b, //
                w, w, b, b, b, //
        };

        Area drawArea = new Area(//
                new Vector2d(2, 2), //
                new Size(3, 3) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, Vector2d.zero());

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_3() {
        int[] expectedArea = { //
                b, b, w, w, w, //
                b, b, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
        };

        Area drawArea = new Area(//
                new Vector2d(0, 0), //
                new Size(5, 5) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, new Vector2d(-1, -1));

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_4() {
        int[] expectedArea = { //
                b, b, w, w, w, //
                b, b, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
        };

        Area drawArea = new Area(//
                new Vector2d(0, 0), //
                new Size(2, 2) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, new Vector2d(0, 0));

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_5() {
        int[] expectedArea = { //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, b, b, //
                w, w, w, b, b, //
        };

        Area drawArea = new Area(//
                new Vector2d(3, 3), //
                new Size(5, 5) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, new Vector2d(0, 0));

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }

    @Test
    void test_ToArgbPixels_6() {
        int[] expectedArea = { //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, w, w, //
                w, w, w, b, b, //
                w, w, w, b, b, //
        };

        Area drawArea = new Area(//
                new Vector2d(0, 0), //
                new Size(5, 5) //
        );

        toDraw.toArgbPixels(fullAreaToDraw, sizeOfFullAreaToDraw, drawArea, new Vector2d(3, 3));

        assertArrayEquals(expectedArea, fullAreaToDraw);
    }
}
