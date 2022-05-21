package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.SimpleGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Put all elements to one column. Set horizontal alignment of the all elements in the column.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class HorizontalAlignmentGlypthCompositor<T extends Glyph> implements GlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Getter
    @Setter
    private AligmentH aligment;

    @Override
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize) {
        List<Drawable> listOfElements = new LinkedList<>();

        int maxWidth = 0;
        while (iterator.hasNext()) {
            Glyph element = iterator.next();
            var elementDrawable = element.getDrawable();
            listOfElements.add(elementDrawable);
            maxWidth = Math.max(maxWidth, elementDrawable.getSize().getWidth());
        }

        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);

        int currentY = 0;
        for (Drawable drawable : listOfElements) {
            switch (aligment) {
            case CENTER:
                responseSketch.addDrawable(//
                        drawable, //
                        maxWidth / 2 - maxSize.getWidth() / 2, //
                        currentY //
                );
                break;
            case LEFT:
                responseSketch.addDrawable(//
                        drawable, //
                        0, //
                        currentY //
                );
                break;
            case RIGHT:
                responseSketch.addDrawable(//
                        drawable, //
                        maxWidth - drawable.getSize().getWidth(), //
                        currentY //
                );
                break;
            default:
                responseSketch.addDrawable(//
                        drawable, //
                        0, //
                        currentY //
                );
                break;
            }
            currentY += drawable.getSize().getHeight();
        }

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return List.of(responseGlyph);
    }

}
