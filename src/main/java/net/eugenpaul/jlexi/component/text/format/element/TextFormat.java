package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable text format data
 */
@Builder
@Getter
@EqualsAndHashCode
public class TextFormat {
    @Builder.Default
    private Boolean bold = null;
    @Builder.Default
    private Boolean italic = null;
    @Builder.Default
    private Integer fontsize = null;
    @Builder.Default
    private String fontName = null;
}
