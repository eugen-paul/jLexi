package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

/**
 * Immutable text format data.
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormat {
    @Builder.Default
    private Boolean bold = false;

    @Builder.Default
    private Boolean italic = false;

    @Builder.Default
    private Integer fontsize = FontStorage.DEFAULT_FONT_SIZE;

    @Builder.Default
    private String fontName = FontStorage.DEFAULT_FONT_NAME;
}
