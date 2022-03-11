package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextNewLine extends TextElementAbstract {

    public TextNewLine(Glyph parent, ResourceManager storage, TextStructure parentTextField, TextFormat format) {
        super(parent, storage, parentTextField, format);

        // we must compute pixels to set size of element
        getPixels();
    }

    @Override
    public Drawable getPixels() {
        if (null != cachedDrawable) {
            return cachedDrawable;
        }

        FormatAttribute textFormat = new FormatAttribute();

        if (null != structureParent) {
            textFormat = structureParent.mergeFormat(textFormat);

            int[] pixels = new int[storage.getFonts().getMaxAscent(//
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

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        return this;
    }

    @Override
    public boolean isEndOfLine() {
        return true;
    }

}
