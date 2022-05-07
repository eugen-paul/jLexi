package net.eugenpaul.jlexi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

class ColorTest {
    @Test
    void test_Convert() {
        assertEquals(Color.WHITE, Color.convert("0xFF_FF_FF_FF"));
        assertEquals(Color.WHITE, Color.convert("white"));
    }

    @Test
    void test_Convert_exception() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.convert("0xFF_FF_FF6FF"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.convert("DATA"));
    }

    @Test
    void test_FromHexARGB() {
        assertEquals(Color.WHITE, Color.fromHexArgb("0xFF_FF_FF_FF"));
        assertEquals(Color.BLACK, Color.fromHexArgb("0xFF_00_00_00"));
        assertEquals(Color.RED, Color.fromHexArgb("0xFF_FF_00_00"));
        assertEquals(Color.GREEN, Color.fromHexArgb("0xFF_00_FF_00"));
        assertEquals(Color.BLUE, Color.fromHexArgb("0xFF_00_00_FF"));

        assertEquals(Color.WHITE, Color.fromHexArgb("0xFFFFFFFF"));
        assertEquals(Color.BLACK, Color.fromHexArgb("0xFF000000"));
        assertEquals(Color.RED, Color.fromHexArgb("0xFFFF0000"));
        assertEquals(Color.GREEN, Color.fromHexArgb("0xFF00FF00"));
        assertEquals(Color.BLUE, Color.fromHexArgb("0xFF0000FF"));
    }

    @Test
    void test_FromHexARGB_exception() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexArgb("0xFF_FF_FF__FF"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexArgb("0xFF_FF_FF_"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexArgb("0xZZ_FF_FF_FF"));
    }

    @Test
    void test_FromText() {
        assertEquals(Color.WHITE, Color.fromText("White"));
        assertEquals(Color.BLACK, Color.fromText("BLACK"));
        assertEquals(Color.RED, Color.fromText("red"));
        assertEquals(Color.GREEN, Color.fromText("grEEn"));
        assertEquals(Color.YELLOW, Color.fromText("YelLow"));
        assertEquals(Color.BLUE, Color.fromText("BLUe"));
    }

    @Test
    void test_FromText_exception() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromText("_black"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromText(" black"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromText("black "));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromText("0xFF_FF_FF_FF"));
    }

    @Test
    void test_GetARGB() {
        assertEquals(0xFF_FF_FF_FF, Color.WHITE.getArgb());
        assertEquals(0xFF_00_00_00, Color.BLACK.getArgb());
        assertEquals(0xFF_FF_00_00, Color.RED.getArgb());
        assertEquals(0xFF_00_FF_00, Color.GREEN.getArgb());
        assertEquals(0xFF_00_00_FF, Color.BLUE.getArgb());
    }

    @Test
    void text_getHexARGB() {
        assertEquals("0xFF_FF_FF_FF", Color.WHITE.getHexArgb());
        assertEquals("0xFF_00_00_00", Color.BLACK.getHexArgb());
        assertEquals("0xFF_FF_00_00", Color.RED.getHexArgb());
        assertEquals("0xFF_00_FF_00", Color.GREEN.getHexArgb());
        assertEquals("0xFF_00_00_FF", Color.BLUE.getHexArgb());
    }
}
