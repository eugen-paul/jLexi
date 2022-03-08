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

    public FormatAttribute(FormatAttribute original) {
        this.bold = original.bold;
        this.italic = original.italic;
        this.fontsize = original.fontsize;
        this.fontName = original.fontName;
    }

    /**
     * takes over the parameters from the source that are not set by itself.
     * 
     * @param src
     */
    public FormatAttribute merge(FormatAttribute src) {
        bold = setIfNull(bold, src.getBold());
        italic = setIfNull(italic, src.getItalic());
        fontsize = setIfNull(fontsize, src.getFontsize());
        fontName = setIfNull(fontName, src.getFontName());
        return this;
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
