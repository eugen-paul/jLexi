package net.eugenpaul.jlexi.component.text.format;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

/**
 * @deprecated since now
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Deprecated(since = "now")
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

    public FormatAttribute(TextFormat format) {
        this.bold = format.getBold();
        this.italic = format.getItalic();
        this.fontsize = format.getFontsize();
        this.fontName = format.getFontName();
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

    public TextFormat toTextFormat() {
        return TextFormat.builder()//
                .bold(bold)//
                .italic(italic)//
                .fontsize(fontsize)//
                .fontName(fontName)//
                .build();
    }
}
