package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public class TextNewLine extends TextElementAbstract {

    public TextNewLine(Glyph parent, FontStorage fontStorage, TextField parentTextField) {
        super(parent, fontStorage, parentTextField);
    }

    @Override
    public Drawable getPixels() {
        if (null != cachedDrawable) {
            return cachedDrawable;
        }

        FormatAttribute textFormat = new FormatAttribute();

        if (null != parentTextField) {
            textFormat = parentTextField.mergeFormat(textFormat);

            int[] pixels = new int[fontStorage.getMaxAscent(//
                    (textFormat.getFontName() == null) ? FontStorage.DEFAULT_FONT_NAME : textFormat.getFontName(), //
                    (textFormat.getFontsize() == null) ? FontStorage.DEFAULT_FONT_SIZE : textFormat.getFontsize() //
            )];
            setSize(new Size(1, pixels.length));

            cachedDrawable = new DrawableImpl(pixels, getSize());

            cachedDrawable = doEffects(cachedDrawable);
        } else {
            cachedDrawable = DrawableImpl.EMPTY_DRAWABLE;
        }

        return cachedDrawable;
    }

}
