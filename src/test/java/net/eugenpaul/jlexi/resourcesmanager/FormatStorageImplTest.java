package net.eugenpaul.jlexi.resourcesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.impl.FormatStorageImpl;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderline;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class FormatStorageImplTest {

    private FormatStorageImpl storage;

    @BeforeEach
    void init() {
        storage = new FormatStorageImpl();
    }

    @Test
    void testAdd() {
        TextFormat format = TextFormat.builder()//
                .bold(true)//
                .build();

        TextFormat responseFormat = storage.add(format);
        assertNotNull(responseFormat);
        assertSame(responseFormat, format);

        TextFormat formatCopy = TextFormat.builder()//
                .bold(true)//
                .build();

        TextFormat responseFormatCopy = storage.add(formatCopy);
        assertNotNull(responseFormatCopy);
        assertSame(responseFormat, responseFormatCopy);
    }

    @Test
    void testSetFontSize() {
        TextFormat format = TextFormat.builder()//
                .bold(true)//
                .fontsize(7)//
                .build();

        format = storage.add(format);

        TextFormat responseFormat = storage.setFontSize(format, 15);
        assertNotNull(responseFormat);
        assertEquals(15, responseFormat.getFontsize());

        format = TextFormat.builder()//
                .bold(true)//
                .fontsize(15)//
                .build();

        TextFormat responseNewFormat = storage.add(format);
        assertNotNull(responseNewFormat);
        assertSame(responseFormat, responseNewFormat);
    }

    @Test
    void testSetBold() {
        TextFormat format = TextFormat.builder()//
                .bold(false)//
                .build();

        format = storage.add(format);

        TextFormat responseFormat = storage.setBold(format, true);
        assertNotNull(responseFormat);
        assertTrue(responseFormat.getBold().booleanValue());

        format = TextFormat.builder()//
                .bold(true)//
                .build();

        TextFormat responseNewFormat = storage.add(format);
        assertNotNull(responseNewFormat);
        assertSame(responseFormat, responseNewFormat);
    }

    @Test
    void testSetFontName() {
        TextFormat format = TextFormat.builder()//
                .bold(true)//
                .build();

        format = storage.add(format);

        TextFormat responseFormat = storage.setFontName(format, "Example");
        assertNotNull(responseFormat);
        assertEquals("Example", responseFormat.getFontName());

        format = TextFormat.builder()//
                .bold(true)//
                .fontName("Example")//
                .build();

        TextFormat responseNewFormat = storage.add(format);
        assertNotNull(responseNewFormat);
        assertSame(responseFormat, responseNewFormat);
    }

    @Test
    void testSetItalic() {
        TextFormat format = TextFormat.builder()//
                .bold(false)//
                .italic(false)//
                .build();

        format = storage.add(format);

        TextFormat responseFormat = storage.setItalic(format, true);
        assertNotNull(responseFormat);
        assertTrue(responseFormat.getItalic().booleanValue());

        format = TextFormat.builder()//
                .bold(false)//
                .italic(true)//
                .build();

        TextFormat responseNewFormat = storage.add(format);
        assertNotNull(responseNewFormat);
        assertSame(responseFormat, responseNewFormat);
    }

    @Test
    void testFormatterUnderline_single() {
        TextFormatEffect formatEffect = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .build();

        var formatter = storage.formatter(formatEffect);
        assertNotNull(formatter);
        assertEquals(1, formatter.size());
        assertTrue(formatter.get(0) instanceof FormatUnderline);

        FormatUnderline f = (FormatUnderline) formatter.get(0);
        assertEquals(FormatUnderlineType.SINGLE, f.getType());
    }

    @Test
    void testFormatterUnderline_double() {
        TextFormatEffect formatEffect = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.DOUBLE)//
                .build();

        var formatter = storage.formatter(formatEffect);
        assertNotNull(formatter);
        assertEquals(1, formatter.size());
        assertTrue(formatter.get(0) instanceof FormatUnderline);

        FormatUnderline f = (FormatUnderline) formatter.get(0);
        assertEquals(FormatUnderlineType.DOUBLE, f.getType());
    }

    @Test
    void testFormatterUnderline_none() {
        TextFormatEffect formatEffect = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.NONE)//
                .build();

        var formatter = storage.formatter(formatEffect);
        assertNotNull(formatter);
        assertTrue(formatter.isEmpty());
    }

    @Test
    void testAddTextFormatEffect() {
        TextFormatEffect formatEffect = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .build();

        var responseFormatEffect = storage.add(formatEffect);
        assertNotNull(responseFormatEffect);
        assertSame(responseFormatEffect, formatEffect);

        TextFormatEffect formatEffectNew = TextFormatEffect.builder()//
                .underline(FormatUnderlineType.SINGLE)//
                .build();

        var responseFormatEffectNew = storage.add(formatEffectNew);
        assertNotNull(responseFormatEffectNew);
        assertSame(responseFormatEffect, responseFormatEffectNew);

        assertNotSame(formatEffect, formatEffectNew);
    }
}
