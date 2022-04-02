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
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());
    }

    @Test
    void testBuilder_Default() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .build();

        assertNotNull(original);
        assertEquals(TextFormatEffect.DEFAULT_UNDERLINE, original.getUnderline());
        assertEquals(TextFormatEffect.DEFAULT_UNDERLINE_COLOR, original.getUnderlineColor());
    }

    @Test
    void testBuilder_Copy() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .build();

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
    }

    @Test
    void testBuilder_CopyAndReplace() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .build();

        TextFormatEffect copy = TextFormatEffect.builder()//
                .textFormatEffect(original)//
                .underline(FormatUnderlineType.DOUBLE)//
                .underlineColor(new Color(0xFF0000FA))//
                .build();

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(new Color(0xFF0000FA), copy.getUnderlineColor());
    }

    @Test
    void testBuilder_withUnderline_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .build();

        TextFormatEffect copy = original.withUnderline(FormatUnderlineType.DOUBLE);

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.DOUBLE, copy.getUnderline());
        assertEquals(Color.BLUE, copy.getUnderlineColor());
    }

    @Test
    void testBuilder_withUnderlineColor_test() {
        TextFormatEffect original = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .underlineColor(Color.BLUE)//
                .build();

        TextFormatEffect copy = original.withUnderlineColor(new Color(0xFF0000FA));

        assertNotNull(original);
        assertEquals(FormatUnderlineType.SINGLE, original.getUnderline());
        assertEquals(Color.BLUE, original.getUnderlineColor());

        assertNotNull(copy);
        assertEquals(FormatUnderlineType.SINGLE, copy.getUnderline());
        assertEquals(new Color(0xFF0000FA), copy.getUnderlineColor());
    }
}
