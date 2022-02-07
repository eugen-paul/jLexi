package net.eugenpaul.jlexi.component.text.format;

import java.awt.Font;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormatAttribute {

    private Boolean bold;
    private Boolean italic;
    private Integer fontsize;
    private String fontName;

    /**
     * takes over the parameters from the source that are not set by itself.
     * 
     * @param src
     */
    public void merge(FormatAttribute src) {
        setIfNull(bold, src.getBold());
        setIfNull(italic, src.getItalic());
        setIfNull(fontsize, src.getFontsize());
        setIfNull(fontName, src.getFontName());
    }

    private <T> T setIfNull(T target, T source) {
        if (target == null) {
            return source;
        }
        return target;
    }

    public int getStyle() {
        return ((bold != null && bold.booleanValue()) ? Font.BOLD : 0)//
                | ((italic != null && italic.booleanValue()) ? Font.ITALIC : 0)//
        ;
    }

    /**
     * Return true if all pattributes are set.
     * 
     * @return
     */
    public boolean isFull() {
        return //
        bold != null //
                && italic != null //
                && fontsize != null //
                && fontName != null //
        ;
    }
}
