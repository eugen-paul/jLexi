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
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Put all elements to one column. Set horizontal alignment of the all elements in the column.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class HorizontalAlignmentGlypthCompositor<T extends Glyph>
        implements GlyphCompositor<T>, ToSingleGlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Getter
    @Setter
    private AligmentH aligment;

    @Override
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize) {
        return List.of(composeToSingle(iterator, maxSize));
    }

    @Override
    public Glyph composeToSingle(Iterator<T> iterator, Size maxSize) {
        List<T> listOfElements = new LinkedList<>();

        int maxWidth = 0;
        while (iterator.hasNext()) {
            T element = iterator.next();
            listOfElements.add(element);
            var elementDrawable = element.getDrawable();
            maxWidth = Math.max(maxWidth, elementDrawable.getSize().getWidth());
        }

        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);

        int currentY = 0;
        for (T element : listOfElements) {
            Drawable drawable = element.getDrawable();
            int x;
            switch (aligment) {
            case CENTER:
                x = maxWidth / 2 - maxSize.getWidth() / 2;
                break;
            case RIGHT:
                x = maxWidth - drawable.getSize().getWidth();
                break;
            case LEFT:
            default:
                x = 0;
                break;
            }

            element.setRelativPosition(new Vector2d(x, currentY));

            responseSketch.addDrawable(//
                    drawable, //
                    x, //
                    currentY //
            );

            currentY += drawable.getSize().getHeight();
        }

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return responseGlyph;
    }

}
