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
 * Put element to given offset.
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

    @Setter
    private boolean centerH = true;
    @Setter
    private boolean centerV = true;

    @Override
    public Glyph compose(T element, Size maxSize) {
        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);
        if (centerV && centerH) {
            responseSketch.addDrawable(//
                    element.getDrawable(), //
                    maxSize.getWidth() / 2 - element.getDrawable().getSize().getWidth() / 2, //
                    maxSize.getHeight() / 2 - element.getDrawable().getSize().getHeight() / 2 //
            );
        } else if (centerV) {
            responseSketch.addDrawable(//
                    element.getDrawable(), //
                    hOffset, //
                    maxSize.getHeight() / 2 - element.getDrawable().getSize().getHeight() / 2 //
            );
        } else if (centerH) {
            responseSketch.addDrawable(//
                    element.getDrawable(), //
                    maxSize.getWidth() / 2 - element.getDrawable().getSize().getWidth() / 2, //
                    vOffset //
            );
        } else {
            responseSketch.addDrawable(//
                    element.getDrawable(), //
                    hOffset, //
                    vOffset //
            );
        }

        element.setRelativPosition(new Vector2d(hOffset, vOffset));

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return responseGlyph;
    }

}
