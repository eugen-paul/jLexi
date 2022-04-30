package net.eugenpaul.jlexi.utils.helper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class ImageArrayConverterTest {

    @Test
    void test_ArgbToRgba_1() {
        int[] input = new int[] { 0x01_23_45_67 };
        int[] outputExpected = new int[] { 0x23_45_67_01 };
        int[] output = ImageArrayConverter.argbToRgba(input);

        assertArrayEquals(outputExpected, output);
    }

    @Test
    void test_ArgbToRgba_2() {
        int[] input = new int[] { 0x01_23_45_67, 0x02_23_45_67, };
        int[] outputExpected = new int[] { 0x23_45_67_01, 0x23_45_67_02 };
        int[] output = ImageArrayConverter.argbToRgba(input);

        assertArrayEquals(outputExpected, output);
    }

    @Test
    void test_RgbaToArgb_1() {
        int[] input = new int[] { 0x23_45_67_01 };
        int[] outputExpected = new int[] { 0x01_23_45_67 };
        int[] output = ImageArrayConverter.rgbaToArgb(input);

        assertArrayEquals(outputExpected, output);
    }

    @Test
    void test_RgbaToArgb_2() {
        int[] input = new int[] { 0x23_45_67_01, 0x23_45_67_02 };
        int[] outputExpected = new int[] { 0x01_23_45_67, 0x02_23_45_67 };
        int[] output = ImageArrayConverter.rgbaToArgb(input);

        assertArrayEquals(outputExpected, output);
    }
}
