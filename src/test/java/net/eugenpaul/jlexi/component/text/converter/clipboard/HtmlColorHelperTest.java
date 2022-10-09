package net.eugenpaul.jlexi.component.text.converter.clipboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.component.text.converter.clipboard.html.HtmlColorHelper;
import net.eugenpaul.jlexi.utils.Color;

class HtmlColorHelperTest {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static enum HexTestData {
        RED_3("#F00", Color.fromHexArgb("0xFFFF0000")), //
        GREEN_3("#0F0", Color.fromHexArgb("0xFF00FF00")), //
        RED_4("#F008", Color.fromHexArgb("0x88FF0000")), //
        GREEN_4("#0F08", Color.fromHexArgb("0x8800FF00")), //
        RED_6("#FF0000", Color.fromHexArgb("0xFFFF0000")), //
        GREEN_6("#00FF00", Color.fromHexArgb("0xFF00FF00")), //
        RED_8("#FF000080", Color.fromHexArgb("0x80FF0000")), //
        GREEN_8("#00FF0080", Color.fromHexArgb("0x8000FF00")), //
        ;

        @Getter
        private final String hexValue;
        @Getter
        private final Color color;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static enum RgbTestData {
        RED_1("rgb(255, 0, 0)", Color.fromHexArgb("0xFFFF0000")), //
        GREEN_1("rgb(0,255,  0)", Color.fromHexArgb("0xFF00FF00")), //
        RED_2("rgba(255, 0, 0, 1)", Color.fromHexArgb("0xFFFF0000")), //
        GREEN_2("rgba(0,255, 0, 1)", Color.fromHexArgb("0xFF00FF00")), //
        RED_3("rgba(255, 0, 0, 0)", Color.fromHexArgb("0x00FF0000")), //
        RED_4("rgba(255, 0, 0, 0.5)", Color.fromHexArgb("0x7FFF0000")), //
        ;

        @Getter
        private final String rbgValue;
        @Getter
        private final Color color;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static enum TestDataNok {
        ERR_1("F00"), //
        ERR_2("#F0"), //
        ERR_3("#FFZZ00"), //
        ERR_4("#FF00000000080"), //
        ERR_5("rgb(0,0)"), //
        ERR_6("rgb(0,0,0,0,0,0)"), //
        ERR_7("rgb(a,b,c)"), //
        ERR_8("rgba(0,0)"), //
        ;

        @Getter
        private final String value;
    }

    @ParameterizedTest
    @EnumSource(HexTestData.class)
    void testHexColorOk(HexTestData value) {
        assertTrue(HtmlColorHelper.isHexColor(value.getHexValue()));

        assertEquals(value.getColor(), HtmlColorHelper.parseHexColor(value.getHexValue()));
        assertEquals(value.getColor(), HtmlColorHelper.parseColor(value.getHexValue()));
    }

    @ParameterizedTest
    @EnumSource(TestDataNok.class)
    void testParseNok(TestDataNok value) {
        assertFalse(HtmlColorHelper.isHexColor(value.getValue()));
        assertFalse(HtmlColorHelper.isRgbaColor(value.getValue()));

        assertNull(HtmlColorHelper.parseHexColor(value.getValue()));
        assertNull(HtmlColorHelper.parseRgbaColor(value.getValue()));

        assertNull(HtmlColorHelper.parseColor(value.getValue()));
    }

    @ParameterizedTest
    @EnumSource(RgbTestData.class)
    void testRgbaColorOk(RgbTestData value) {
        assertTrue(HtmlColorHelper.isRgbaColor(value.getRbgValue()));

        assertEquals(value.getColor(), HtmlColorHelper.parseRgbaColor(value.getRbgValue()));
        assertEquals(value.getColor(), HtmlColorHelper.parseColor(value.getRbgValue()));
    }

}
