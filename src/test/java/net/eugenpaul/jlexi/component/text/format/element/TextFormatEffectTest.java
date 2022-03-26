package net.eugenpaul.jlexi.component.text.format.element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class TextFormatEffectTest {
    @Test
    void testBuilder() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());
    }

    @Test
    void testBuilder_Default() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .build();

        assertNotNull(original);
        assertEquals(TextFormatEffect.DEFAULT_UNDERLINE, original.getUnderline());
        assertEquals(TextFormatEffect.DEFAULT_UNDERLINE_COLOR, original.getUnderlineColor());
        assertEquals(TextFormatEffect.DEFAULT_FONT_COLOR, original.getFontColor());
        assertEquals(TextFormatEffect.DEFAULT_BACKGROUND_COLOR, original.getBackgroundColor());
    }

    @Test
    void testBuilder_Copy() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .build();

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(0xFF0000FF, copy.getUnderlineColor());
        assertEquals(0xFF00FF00, copy.getFontColor());
        assertEquals(0xFFFF0000, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_CopyAndReplace() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .underline(FormatUnderlineType.DOUBLE)//
                .underlineColor(0xFF0000FA)//
                .fontColor(0xFF00FF0A)//
                .backgroundColor(0xFFFF000A)//
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(0xFF0000FA, copy.getUnderlineColor());
        assertEquals(0xFF00FF0A, copy.getFontColor());
        assertEquals(0xFFFF000A, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withUnderline_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = original.withUnderline(FormatUnderlineType.DOUBLE);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(0xFF0000FF, copy.getUnderlineColor());
        assertEquals(0xFF00FF00, copy.getFontColor());
        assertEquals(0xFFFF0000, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withUnderlineColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = original.withUnderlineColor(0xFF0000FA);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(0xFF0000FA, copy.getUnderlineColor());
        assertEquals(0xFF00FF00, copy.getFontColor());
        assertEquals(0xFFFF0000, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withFontColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = original.withFontColor(0xFF00FF0A);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(0xFF0000FF, copy.getUnderlineColor());
        assertEquals(0xFF00FF0A, copy.getFontColor());
        assertEquals(0xFFFF0000, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withBackgroundColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(0xFF0000FF)//
                .fontColor(0xFF00FF00)//
                .backgroundColor(0xFFFF0000)//
                .build();

        TextFormatEffect copy = original.withBackgroundColor(0xFFFF000A);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(0xFF0000FF, original.getUnderlineColor());
        assertEquals(0xFF00FF00, original.getFontColor());
        assertEquals(0xFFFF0000, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(0xFF0000FF, copy.getUnderlineColor());
        assertEquals(0xFF00FF00, copy.getFontColor());
        assertEquals(0xFFFF000A, copy.getBackgroundColor());
    }
}
