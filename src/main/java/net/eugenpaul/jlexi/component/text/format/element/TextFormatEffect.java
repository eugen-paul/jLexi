package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;
import lombok.EqualsAndHashCode.Exclude;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.textformat.PixelsFormatter;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormatEffect {

    public static final int DEFAULT_FONT_COLOR = 0xFF000000;
    public static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    public static final FormatUnderlineType DEFAULT_UNDERLINE = FormatUnderlineType.NONE;
    public static final int DEFAULT_UNDERLINE_COLOR = 0xFFFFFFFF;

    public static final TextFormatEffect DEFAULT_FORMAT_EFFECT = TextFormatEffect.builder().build();

    @Builder.Default
    @With
    @NonNull
    private FormatUnderlineType underline = DEFAULT_UNDERLINE;

    @Builder.Default
    @With
    private int underlineColor = DEFAULT_UNDERLINE_COLOR;

    @Builder.Default
    @With
    private int fontColor = DEFAULT_FONT_COLOR;

    @Builder.Default
    @With
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
        if (formatterInit.getAndSet(true)) {
            return formatter;
        }

        formatter.addAll(storage.formatter(this));

        return List.copyOf(formatter);
    }
}
