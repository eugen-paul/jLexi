package net.eugenpaul.jlexi.component.text.format;

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

    public <T> T setIfNull(T target, T source) {
        if (target == null) {
            return source;
        }
        return target;
    }

    public boolean isFull() {
        return //
        bold != null //
                && italic != null //
                && fontsize != null //
                && fontName != null //
        ;
    }
}
