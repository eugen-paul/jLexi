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
        List<Drawable> listOfElements = new LinkedList<>();

        int maxHeight = 0;
        while (iterator.hasNext()) {
            Glyph element = iterator.next();
            var elementDrawable = element.getDrawable();
            listOfElements.add(elementDrawable);
            maxHeight = Math.max(maxHeight, elementDrawable.getSize().getHeight());
        }

        DrawableSketchImpl responseSketch = new DrawableSketchImpl(backgroundColor, maxSize);

        int currentX = 0;
        for (Drawable drawable : listOfElements) {
            switch (aligment) {
            case CENTER:
                responseSketch.addDrawable(//
                        drawable, //
                        currentX, //
                        maxHeight / 2 - maxSize.getHeight() / 2 //
                );
                break;
            case BOTTOM:
                responseSketch.addDrawable(//
                        drawable, //
                        currentX, //
                        maxHeight - drawable.getSize().getHeight() //
                );
                break;
            case TOP:
            default:
                responseSketch.addDrawable(//
                        drawable, //
                        currentX, //
                        0 //
                );
                break;
            }
            currentX += drawable.getSize().getWidth();
        }

        SimpleGlyph responseGlyph = new SimpleGlyph();
        responseGlyph.setDrawable(responseSketch);

        return responseGlyph;
    }

}
