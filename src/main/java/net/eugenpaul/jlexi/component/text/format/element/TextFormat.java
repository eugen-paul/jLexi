package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * Immutable text format data
 */
@Builder
@EqualsAndHashCode
public class TextFormat {
    private Boolean bold = null;
    private Boolean italic = null;
    private Integer fontsize = null;
    private String fontName = null;
}
