package net.eugenpaul.jlexi.component.text.format.element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;
import net.eugenpaul.jlexi.utils.Color;

public class TextFormatEffectTest {
    @Test
    void testBuilder() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());
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
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .build();

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
        assertEquals(Color.GREEN, copy.getFontColor());
        assertEquals(Color.RED, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_CopyAndReplace() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .underline(FormatUnderlineType.DOUBLE)//
                .underlineColor(new Color(0xFF0000FA))//
                .fontColor(new Color(0xFF00FF0A))//
                .backgroundColor(new Color(0xFFFF000A))//
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(new Color(0xFF0000FA), copy.getUnderlineColor());
        assertEquals(new Color(0xFF00FF0A), copy.getFontColor());
        assertEquals(new Color(0xFFFF000A), copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withUnderline_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = original.withUnderline(FormatUnderlineType.DOUBLE);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
        assertEquals(Color.GREEN, copy.getFontColor());
        assertEquals(Color.RED, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withUnderlineColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = original.withUnderlineColor(new Color(0xFF0000FA));

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(new Color(0xFF0000FA), copy.getUnderlineColor());
        assertEquals(Color.GREEN, copy.getFontColor());
        assertEquals(Color.RED, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withFontColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = original.withFontColor(new Color(0xFF00FF0A));

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
        assertEquals(new Color(0xFF00FF0A), copy.getFontColor());
        assertEquals(Color.RED, copy.getBackgroundColor());
    }

    @Test
    void testBuilder_withBackgroundColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .fontColor(Color.GREEN)//
                .backgroundColor(Color.RED)//
                .build();

        TextFormatEffect copy = original.withBackgroundColor(new Color(0xFFFF000A));

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
        assertEquals(Color.GREEN, original.getFontColor());
        assertEquals(Color.RED, original.getBackgroundColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
        assertEquals(Color.GREEN, copy.getFontColor());
        assertEquals(new Color(0xFFFF000A), copy.getBackgroundColor());
    }
}
