package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.FormatStorage;
import net.eugenpaul.jlexi.resourcesmanager.PixelsFormatter;
import net.eugenpaul.jlexi.resourcesmanager.textformatter.UnderlineType;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormat {

    private static final int DEFAULT_FONT_COLOR = 0xFF000000;
    private static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;

    @Builder.Default
    private Boolean bold = false;

    @Builder.Default
    private Boolean italic = false;

    @Builder.Default
    private Integer fontsize = FontStorage.DEFAULT_FONT_SIZE;

    @Builder.Default
    private String fontName = FontStorage.DEFAULT_FONT_NAME;

    @Builder.Default
    private UnderlineType underline = UnderlineType.NONE;

    @Builder.Default
    private int underlineColor = DEFAULT_FONT_COLOR;

    @Builder.Default
    private int fontColor = DEFAULT_FONT_COLOR;

    @Builder.Default
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    @Getter(lombok.AccessLevel.NONE)
    private final List<PixelsFormatter> formatter = new LinkedList<>();

    @Getter(lombok.AccessLevel.NONE)
    private final AtomicBoolean formatterInit = new AtomicBoolean(false);

    public List<PixelsFormatter> getFormatter(FormatStorage storage) {
        if (formatterInit.get()) {
            return formatter;
        }

        formatterInit.set(true);

        formatter.addAll(storage.formatter(this));

        return List.copyOf(formatter);
    }
}
