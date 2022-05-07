package net.eugenpaul.jlexi.utils.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class CollisionHelperTest {

    @Test
    void test_DoOverlap_equal() {
        Vector2d pos = new Vector2d(0, 0);
        Size size = new Size(10, 10);

        assertTrue(CollisionHelper.doOverlap(pos, size, pos, size));
    }

    @Test
    void test_DoOverlap_inside() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(2, 2);
        Size size2 = new Size(5, 5);

        assertTrue(CollisionHelper.doOverlap(pos1, size1, pos2, size2));
    }

    @ParameterizedTest
    @CsvSource({ //
            "-2, -2", //
            "-2,  2", //
            " 8,  8", //
            " 8, -2", //
            "-2,  8", //
    })
    void test_DoOverlap_overlapping(int pos2X, int pos2Y) {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(pos2X, pos2Y);
        Size size2 = new Size(5, 5);

        assertTrue(CollisionHelper.doOverlap(pos1, size1, pos2, size2));
    }

    void test_DoOverlap_overlapping_line() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(2, 2);
        Size size2 = new Size(5, 1);

        assertTrue(CollisionHelper.doOverlap(pos1, size1, pos2, size2));
    }

    @ParameterizedTest
    @CsvSource({ //
            "10, 10", //
            "10, 12", //
            "-5, -5", //
            "-5, -8", //
            "-6, -6", //
            " 2, 10", //
            " 2, 11", //
    })
    void test_DoOverlap_not_overlapping(int pos2X, int pos2Y) {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(pos2X, pos2Y);
        Size size2 = new Size(5, 5);

        assertFalse(CollisionHelper.doOverlap(pos1, size1, pos2, size2));
    }

    @Test
    void test_GetOverlapping_equal() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(0, 0);
        Size size2 = new Size(10, 10);

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);

        assertEquals(new Area(new Vector2d(0, 0), new Size(10, 10)), overlappingArea);
    }

    @Test
    void test_GetOverlapping_inside() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(2, 2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(2, 2), new Size(5, 5));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_1() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(-2, -2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(0, 0), new Size(3, 3));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_2() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(2, -2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(2, 0), new Size(5, 3));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_3() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(8, -2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(8, 0), new Size(2, 3));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_4() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(8, 2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(8, 2), new Size(2, 5));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_5() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(8, 8);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(8, 8), new Size(2, 2));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_6() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(2, 8);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(2, 8), new Size(5, 2));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_7() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(-2, 8);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(0, 8), new Size(3, 2));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_8() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(-2, 2);
        Size size2 = new Size(5, 5);

        Area expectedArea = new Area(new Vector2d(0, 2), new Size(3, 5));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_line() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(-2, 2);
        Size size2 = new Size(15, 1);

        Area expectedArea = new Area(new Vector2d(0, 2), new Size(10, 1));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @Test
    void test_GetOverlapping_overlapping_empty() {
        Vector2d pos1 = new Vector2d(0, 0);
        Size size1 = new Size(10, 10);
        Vector2d pos2 = new Vector2d(-2, 2);
        Size size2 = new Size(15, 0);

        Area expectedArea = new Area(new Vector2d(0, 0), new Size(0, 0));

        Area overlappingArea = CollisionHelper.getOverlapping(pos1, size1, pos2, size2);
        assertEquals(expectedArea, overlappingArea);

        overlappingArea = CollisionHelper.getOverlapping(pos2, size2, pos1, size1);
        assertEquals(expectedArea, overlappingArea);
    }

    @ParameterizedTest
    @CsvSource({ //
            "0, 0", //
            "1, 1", //
            "0, 0", //
            "6, 7", //
            "8, 9", //
            "9, 8", //
            "9, 9", //
    })
    void testIsPointOnArea_on_area(int x, int y) {
        Vector2d areaPos = new Vector2d(0, 0);
        Size areaSize = new Size(10, 10);

        Vector2d pointPosition = new Vector2d(x, y);

        assertTrue(CollisionHelper.isPointOnArea(pointPosition, areaPos, areaSize));
    }

    @ParameterizedTest
    @CsvSource({ //
            "-1, -1", //
            "-1, 1", //
            "-1, 0", //
            "10, 8", //
            "8, 10", //
            "10, 10", //
    })
    void testIsPointOnArea_not_on_area(int x, int y) {
        Vector2d areaPos = new Vector2d(0, 0);
        Size areaSize = new Size(10, 10);

        Vector2d pointPosition = new Vector2d(x, y);

        assertFalse(CollisionHelper.isPointOnArea(pointPosition, areaPos, areaSize));
    }

}
