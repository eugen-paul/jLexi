package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextChar extends TextElementAbstract {

    private Character c;

    public TextChar(Glyph parent, ResourceManager storage, TextStructure parentTextField, Character c,
            TextFormat format) {
        super(parent, storage, parentTextField, format);
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

        return cachedDrawable;
    }

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        return this;
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

}
