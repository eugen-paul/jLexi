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
import net.eugenpaul.jlexi.utils.AligmentV;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Put all elements to one row. Set vertical alignment of the all elements in the row.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class VerticalAlignmentGlypthCompositor<T extends Glyph>
        implements GlyphCompositor<T>, ToSingleGlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Getter
    @Setter
    private AligmentV aligment;

    @Override
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize) {
        return List.of(composeToSingle(iterator, maxSize));
    }

    @Override
    public Glyph composeToSingle(Iterator<T> iterator, Size maxSize) {
        List<T> listOfElements = new LinkedList<>();

        int maxHeight = 0;
        while (iterator.hasNext()) {
            T element = iterator.next();
            listOfElements.add(element);
            var elementDrawable = element.getDrawable();
            maxHeight = Math.max(maxHeight, elementDrawable.getSize().getHeight());
        }

        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);

        int currentX = 0;
        for (T element : listOfElements) {
            Drawable drawable = element.getDrawable();
            int y;
            switch (aligment) {
            case CENTER:
                y = maxHeight / 2 - maxSize.getHeight() / 2;
                break;
            case BOTTOM:
                y = maxHeight - drawable.getSize().getHeight();
                break;
            case TOP:
            default:
                y = 0;
                break;
            }

            element.setRelativPosition(new Vector2d(currentX, y));

            responseSketch.addDrawable(//
                    drawable, //
                    currentX, //
                    y //
            );

            currentX += drawable.getSize().getWidth();
        }

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return responseGlyph;
    }

}
