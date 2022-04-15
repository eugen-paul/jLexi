package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextChar extends TextElementAbstract {

    private Character c;

    public TextChar(Glyph parent, ResourceManager storage, TextStructure parentStructure, Character c,
            TextFormat format, TextFormatEffect formatEffect) {
        super(parent, storage, parentStructure, format, formatEffect);
        this.c = c;

        getPixels();
    }

    @Override
    public Drawable getPixels() {
        if (null != cachedDrawable) {
            return cachedDrawable;
        }

        cachedDrawable = storage.getFonts().ofChar(c, getFormat());

        setSize(cachedDrawable.getPixelSize());
        cachedDrawable = doEffects(cachedDrawable);

        getFormatEffect().getFormatter(storage.getFormats()).stream()//
                .forEach(f -> f.doFormat(cachedDrawable));

        return cachedDrawable;
    }

    @Override
    public TextElement getCursorElementAt(Vector2d pos) {
        return this;
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

}
