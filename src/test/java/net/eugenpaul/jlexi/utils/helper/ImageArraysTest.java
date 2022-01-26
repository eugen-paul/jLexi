package net.eugenpaul.jlexi.utils.helper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Verctor2d;

class ImageArraysTest {

    private int[] srcBlock;
    private Size srcSize;

    private int[] destBlock;
    private Size destSize;

    @BeforeEach
    void init() {
        srcBlock = new int[] { //
                11, 12, 13, 14, 15, 16, 17, 18, 19, //
                21, 22, 23, 24, 25, 26, 27, 28, 29, //
                31, 32, 33, 34, 35, 36, 37, 38, 39, //
                41, 42, 43, 44, 45, 46, 47, 48, 49, //
                51, 52, 53, 54, 55, 56, 57, 58, 59, //
        };
        srcSize = new Size(9, 5);

        destBlock = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
        };
        destSize = new Size(9, 4);
    }

    @Test
    void testCopyRectangleCopy_Complete() {
        int[] expectedArray = new int[] { //
                11, 12, 13, 14, 15, 16, 17, 18, 19, //
                21, 22, 23, 24, 25, 26, 27, 28, 29, //
                31, 32, 33, 34, 35, 36, 37, 38, 39, //
                41, 42, 43, 44, 45, 46, 47, 48, 49, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), destSize, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_From_X_Offset_() {
        int[] expectedArray = new int[] { //
                12, 13, 14, 15, 16, 17, 18, 19, 0, //
                22, 23, 24, 25, 26, 27, 28, 29, 0, //
                32, 33, 34, 35, 36, 37, 38, 39, 0, //
                42, 43, 44, 45, 46, 47, 48, 49, 0, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(1, 0), destSize, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_From_Y_Offset_() {
        int[] expectedArray = new int[] { //
                21, 22, 23, 24, 25, 26, 27, 28, 29, //
                31, 32, 33, 34, 35, 36, 37, 38, 39, //
                41, 42, 43, 44, 45, 46, 47, 48, 49, //
                51, 52, 53, 54, 55, 56, 57, 58, 59, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 1), destSize, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_From_XY_Offset_() {
        int[] expectedArray = new int[] { //
                22, 23, 24, 25, 26, 27, 28, 29, 0, //
                32, 33, 34, 35, 36, 37, 38, 39, 0, //
                42, 43, 44, 45, 46, 47, 48, 49, 0, //
                52, 53, 54, 55, 56, 57, 58, 59, 0, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(1, 1), destSize, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_To_X_Offset_() {
        int[] expectedArray = new int[] { //
                0, 11, 12, 13, 14, 15, 16, 17, 18, //
                0, 21, 22, 23, 24, 25, 26, 27, 28, //
                0, 31, 32, 33, 34, 35, 36, 37, 38, //
                0, 41, 42, 43, 44, 45, 46, 47, 48, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), destSize, destBlock, destSize,
                new Verctor2d(1, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_To_Y_Offset_() {
        int[] expectedArray = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                11, 12, 13, 14, 15, 16, 17, 18, 19, //
                21, 22, 23, 24, 25, 26, 27, 28, 29, //
                31, 32, 33, 34, 35, 36, 37, 38, 39, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), destSize, destBlock, destSize,
                new Verctor2d(0, 1));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_To_XY_Offset_() {
        int[] expectedArray = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 11, 12, 13, 14, 15, 16, 17, 18, //
                0, 21, 22, 23, 24, 25, 26, 27, 28, //
                0, 31, 32, 33, 34, 35, 36, 37, 38, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), destSize, destBlock, destSize,
                new Verctor2d(1, 1));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_From_XY_Offset_To_XY_Offset() {
        int[] expectedArray = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 22, 23, 24, 25, 26, 27, 28, 29, //
                0, 32, 33, 34, 35, 36, 37, 38, 39, //
                0, 42, 43, 44, 45, 46, 47, 48, 49, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(1, 1), destSize, destBlock, destSize,
                new Verctor2d(1, 1));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_Copy_From_XY_Offset_To_XY_Offset_SmallSize() {
        int[] expectedArray = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 22, 23, 0, 0, 0, 0, 0, 0, //
                0, 32, 33, 0, 0, 0, 0, 0, 0, //
                0, 42, 43, 0, 0, 0, 0, 0, 0, //
        };

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(1, 1), new Size(2, 3), destBlock, destSize,
                new Verctor2d(1, 1));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_EmptySrc() {
        int[] expectedArray = new int[] { //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
                0, 0, 0, 0, 0, 0, 0, 0, 0, //
        };

        ImageArrayHelper.copyRectangle(new int[] {}, Size.ZERO_SIZE, new Verctor2d(0, 0), destSize, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_EmptyDest() {
        int[] expectedArray = new int[] {};
        int[] emtyDest = new int[] {};

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), destSize, emtyDest, Size.ZERO_SIZE,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, emtyDest);
    }

    @Test
    void testCopyRectangle_EmptyBlock() {
        int[] expectedArray = destBlock.clone();

        ImageArrayHelper.copyRectangle(srcBlock, srcSize, new Verctor2d(0, 0), Size.ZERO_SIZE, destBlock, destSize,
                new Verctor2d(0, 0));

        assertArrayEquals(expectedArray, destBlock);
    }
}
