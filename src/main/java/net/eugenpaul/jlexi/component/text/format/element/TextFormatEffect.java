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
import net.eugenpaul.jlexi.utils.Color;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormatEffect {

    public static final FormatUnderlineType DEFAULT_UNDERLINE = FormatUnderlineType.NONE;
    public static final Color DEFAULT_UNDERLINE_COLOR = Color.BLACK;

    public static final TextFormatEffect DEFAULT_FORMAT_EFFECT = TextFormatEffect.builder().build();

    @Builder.Default
    @With
    @NonNull
    private FormatUnderlineType underline = DEFAULT_UNDERLINE;

    @Builder.Default
    @With
    private Color underlineColor = DEFAULT_UNDERLINE_COLOR;

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
