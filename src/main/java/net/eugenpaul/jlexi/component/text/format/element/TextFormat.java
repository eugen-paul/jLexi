package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Color;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public final class TextFormat {

    public static final TextFormat DEFAULT = TextFormat.builder().build();

    @Builder.Default
    @With
    @NonNull
    private Boolean bold = false;

    @Builder.Default
    @With
    @NonNull
    private Boolean italic = false;

    @Builder.Default
    @With
    @NonNull
    private Integer fontsize = FontStorage.DEFAULT_FONT_SIZE;

    @Builder.Default
    @With
    @NonNull
    private String fontName = FontStorage.DEFAULT_FONT_NAME;
    
    @Builder.Default
    @With
    @NonNull
    private Color fontColor = Color.BLACK;

    @Builder.Default
    @With
    @NonNull
    private Color backgroundColor = Color.WHITE;
}
