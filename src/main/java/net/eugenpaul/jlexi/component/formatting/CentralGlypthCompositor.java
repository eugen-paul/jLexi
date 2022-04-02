package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.SimpleGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

/**
 * Put all elements to one column and set the column to center of returned glyph.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class CentralGlypthCompositor<T extends Glyph> implements GlyphCompositor<T> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Override
    public List<Glyph> compose(Iterator<T> iterator, Size maxSize) {
        Vector2d elementSize = Vector2d.zero();

        Drawable tempDrawable = new DrawableImpl(maxSize);
        ImageArrayHelper.fillRectangle(//
                backgroundColor, //
                tempDrawable, //
                maxSize, //
                Vector2d.zero() //
        );

        while (iterator.hasNext()) {
            Glyph element = iterator.next();
            var elementDrawable = element.getPixels();
            ImageArrayHelper.copyRectangle(elementDrawable, tempDrawable, elementSize);
            elementSize.add(elementDrawable.getPixelSize());
        }

        Glyph responseGlyph = new SimpleGlyph();
        Drawable glyphDrawable = new DrawableImpl(maxSize);

        ImageArrayHelper.fillRectangle(//
                backgroundColor, //
                glyphDrawable, //
                maxSize, //
                Vector2d.zero() //
        );

        ImageArrayHelper.copyRectangle(//
                tempDrawable, //
                glyphDrawable, //
                new Vector2d( //
                        maxSize.getWidth() / 2 - elementSize.getX() / 2, //
                        maxSize.getHeight() / 2 - elementSize.getY() / 2 //
                ) //
        );
        responseGlyph.setCachedDrawable(glyphDrawable);

        return List.of(responseGlyph);
    }

}
