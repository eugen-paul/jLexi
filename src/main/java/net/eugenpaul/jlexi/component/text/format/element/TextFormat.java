package net.eugenpaul.jlexi.component.text.format.element;

import java.awt.Font;

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

    public int getStyle() {
        return ((bold != null && bold.booleanValue()) ? Font.BOLD : 0)//
                | ((italic != null && italic.booleanValue()) ? Font.ITALIC : 0)//
        ;
    }
}
