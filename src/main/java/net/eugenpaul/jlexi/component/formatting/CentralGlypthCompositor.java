package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

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
public class CentralGlypthCompositor<T extends Glyph> implements GlyphCompositor<T>, SingleGlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Override
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize) {
        Vector2d elementSize = Vector2d.zero();

        DrawableSketchImpl innerSketch = new DrawableSketchImpl(backgroundColor);

        int currentY = 0;
        while (iterator.hasNext()) {
            Glyph element = iterator.next();
            var elementDrawable = element.getDrawable();
            innerSketch.addDrawable(elementDrawable, 0, currentY);
            currentY += elementDrawable.getSize().getHeight();
            elementSize.add(elementDrawable.getSize());
        }

        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);
        responseSketch.addDrawable(//
                innerSketch.draw(), //
                maxSize.getWidth() / 2 - elementSize.getX() / 2, //
                maxSize.getHeight() / 2 - elementSize.getY() / 2 //
        );

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return List.of(responseGlyph);
    }

    @Override
    public Glyph compose(T element, Size maxSize) {
        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);
        int hOffset = maxSize.getWidth() / 2 - element.getSize().getWidth() / 2;
        int vOffset = maxSize.getHeight() / 2 - element.getSize().getHeight() / 2;

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
