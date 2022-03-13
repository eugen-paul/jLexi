package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.EqualsAndHashCode.Exclude;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.PixelsFormatter;
import net.eugenpaul.jlexi.resourcesmanager.textformatter.UnderlineType;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormatEffect {

    public static final int DEFAULT_FONT_COLOR = 0xFF000000;
    public static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    public static final UnderlineType DEFAULT_UNDERLINE = UnderlineType.NONE;
    public static final int DEFAULT_UNDERLINE_COLOR = 0xFFFFFFFF;

    public static final TextFormatEffect DEFAULT_FORMAT_EFFECT = TextFormatEffect.builder().build();

    @Builder.Default
    private UnderlineType underline = DEFAULT_UNDERLINE;

    @Builder.Default
    private int underlineColor = DEFAULT_UNDERLINE_COLOR;

    @Builder.Default
    private int fontColor = DEFAULT_FONT_COLOR;

    @Builder.Default
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    @Getter(lombok.AccessLevel.NONE)
    @Exclude
    private final List<PixelsFormatter> formatter = new LinkedList<>();

    @Getter(lombok.AccessLevel.NONE)
    @Exclude
    private final AtomicBoolean formatterInit = new AtomicBoolean(false);

    @SuppressWarnings("all")
    public static class TextFormatEffectBuilder {

        public TextFormatEffectBuilder textFormatEffect(TextFormatEffect original) {
            underline(original.underline);
            underlineColor(original.underlineColor);
            fontColor(original.fontColor);
            backgroundColor(original.backgroundColor);
            return this;
        }
    }

    public List<PixelsFormatter> getFormatter(FormatStorage storage) {
        if (formatterInit.get()) {
            return formatter;
        }

        formatterInit.set(true);

        formatter.addAll(storage.formatter(this));

        return List.copyOf(formatter);
    }
}
