package net.eugenpaul.jlexi.component.formatting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.SimpleGlyph;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Put all elements to one column and set the column to center of returned glyph.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class ScrollGlypthCompositor<T extends Glyph> implements SingleGlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Setter
    private int vOffset;
    @Setter
    private int hOffset;

    @Override
    public Glyph compose(T element, Size maxSize) {
        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);
        responseSketch.addDrawable(//
                element.getDrawable(), //
                hOffset, //
                vOffset //
        );

        element.setRelativPosition(new Vector2d(hOffset, vOffset));

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return responseGlyph;
    }

}
