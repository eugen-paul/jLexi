package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextChar extends TextElementAbstract {

    private Character c;

    public TextChar(Glyph parent, FontStorage fontStorage, TextField parentTextField, Character c) {
        super(parent, fontStorage, parentTextField);
        this.c = c;

        getPixels();
    }

    @Override
    public Drawable getPixels() {
        if (null != cachedDrawable) {
            return cachedDrawable;
        }

        FormatAttribute textFormat = new FormatAttribute();

        if (null != parentTextField) {
            textFormat = parentTextField.mergeFormat(textFormat);
            this.cachedDrawable = this.fontStorage.ofChar(//
                    c, //
                    (textFormat.getFontName() == null) ? FontStorage.DEFAULT_FONT_NAME : textFormat.getFontName(), //
                    textFormat.getStyle(), //
                    (textFormat.getFontsize() == null) ? FontStorage.DEFAULT_FONT_SIZE : textFormat.getFontsize() //
            );

            setSize(this.cachedDrawable.getPixelSize());

            cachedDrawable = doEffects(cachedDrawable);
        } else {
            cachedDrawable = DrawableImpl.EMPTY_DRAWABLE;
        }

        return cachedDrawable;
    }

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        return this;
    }

}
