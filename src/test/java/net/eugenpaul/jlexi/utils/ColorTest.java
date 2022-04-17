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
        assertEquals(Color.WHITE, Color.fromHexARGB("0xFF_FF_FF_FF"));
        assertEquals(Color.BLACK, Color.fromHexARGB("0xFF_00_00_00"));
        assertEquals(Color.RED, Color.fromHexARGB("0xFF_FF_00_00"));
        assertEquals(Color.GREEN, Color.fromHexARGB("0xFF_00_FF_00"));
        assertEquals(Color.BLUE, Color.fromHexARGB("0xFF_00_00_FF"));

        assertEquals(Color.WHITE, Color.fromHexARGB("0xFFFFFFFF"));
        assertEquals(Color.BLACK, Color.fromHexARGB("0xFF000000"));
        assertEquals(Color.RED, Color.fromHexARGB("0xFFFF0000"));
        assertEquals(Color.GREEN, Color.fromHexARGB("0xFF00FF00"));
        assertEquals(Color.BLUE, Color.fromHexARGB("0xFF0000FF"));
    }

    @Test
    void test_FromHexARGB_exception() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexARGB("0xFF_FF_FF__FF"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexARGB("0xFF_FF_FF_"));
        assertThrowsExactly(IllegalArgumentException.class, () -> Color.fromHexARGB("0xZZ_FF_FF_FF"));
    }

    @Test
    void test_FromText() {
        assertEquals(Color.WHITE, Color.fromText("White"));
        assertEquals(Color.BLACK, Color.fromText("BLACK"));
        assertEquals(Color.RED, Color.fromText("red"));
        assertEquals(Color.GREEN, Color.fromText("grEEn"));
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
        assertEquals(0xFF_FF_FF_FF, Color.WHITE.getARGB());
        assertEquals(0xFF_00_00_00, Color.BLACK.getARGB());
        assertEquals(0xFF_FF_00_00, Color.RED.getARGB());
        assertEquals(0xFF_00_FF_00, Color.GREEN.getARGB());
        assertEquals(0xFF_00_00_FF, Color.BLUE.getARGB());
    }

    @Test
    void text_getHexARGB() {
        assertEquals("0xFF_FF_FF_FF", Color.WHITE.getHexARGB());
        assertEquals("0xFF_00_00_00", Color.BLACK.getHexARGB());
        assertEquals("0xFF_FF_00_00", Color.RED.getHexARGB());
        assertEquals("0xFF_00_FF_00", Color.GREEN.getHexARGB());
        assertEquals("0xFF_00_00_FF", Color.BLUE.getHexARGB());
    }
}
