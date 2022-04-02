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
    void testAddNew() {
        TextFormat format = TextFormat.builder()//
                .bold(true)//
                .fontName("testFont")//
                .build();

        TextFormat responseFormat = storage.add(format);
        assertNotNull(responseFormat);
        assertSame(responseFormat, format);

        TextFormat formatNew = format.withItalic(true);

        TextFormat responseFormatCopy = storage.add(formatNew);
        assertNotNull(responseFormatCopy);
        assertSame(formatNew, responseFormatCopy);
        assertNotSame(format, responseFormatCopy);
    }

    @Test
    void testAddCopy() {
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
