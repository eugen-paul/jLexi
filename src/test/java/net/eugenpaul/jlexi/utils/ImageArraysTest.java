package net.eugenpaul.jlexi.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.data.Size;

public class ImageArraysTest {

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, destSize, destBlock, destSize, 0, 0);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 1, 0, destSize, destBlock, destSize, 0, 0);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 1, destSize, destBlock, destSize, 0, 0);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 1, 1, destSize, destBlock, destSize, 0, 0);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, destSize, destBlock, destSize, 1, 0);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, destSize, destBlock, destSize, 0, 1);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, destSize, destBlock, destSize, 1, 1);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 1, 1, destSize, destBlock, destSize, 1, 1);

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

        ImageArrays.copyRectangle(srcBlock, srcSize, 1, 1, new Size(2, 3), destBlock, destSize, 1, 1);

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

        ImageArrays.copyRectangle(new int[] {}, Size.ZERO_SIZE, 0, 0, destSize, destBlock, destSize, 0, 0);

        assertArrayEquals(expectedArray, destBlock);
    }

    @Test
    void testCopyRectangle_EmptyDest() {
        int[] expectedArray = new int[] {};
        int[] emtyDest = new int[] {};

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, destSize, emtyDest, Size.ZERO_SIZE, 0, 0);

        assertArrayEquals(expectedArray, emtyDest);
    }

    @Test
    void testCopyRectangle_EmptyBlock() {
        int[] expectedArray = destBlock.clone();

        ImageArrays.copyRectangle(srcBlock, srcSize, 0, 0, Size.ZERO_SIZE, destBlock, destSize, 0, 0);

        assertArrayEquals(expectedArray, destBlock);
    }
}
