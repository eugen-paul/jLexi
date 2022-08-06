package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextChar extends TextElementAbstract {

    @Getter
    private final Character c;

    public TextChar(Glyph parent, ResourceManager storage, TextStructure parentStructure, Character c,
            TextFormat format, TextFormatEffect formatEffect) {
        super(parent, storage, parentStructure, format, formatEffect);
        this.c = c;

        getDrawable();
    }

    @Override
    public Drawable getDrawable() {
        if (null != this.cachedDrawable) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        Drawable charDrawable = this.storage.getFonts().ofChar2(this.c, getFormat());
        this.cachedDrawable.addDrawable(charDrawable, 0, 0, 0);

        setSize(this.cachedDrawable.getSize());

        doEffects(this.cachedDrawable);

        getFormatEffect().getFormatter(this.storage.getFormats()).stream()//
                .forEach(f -> f.doFormat(this.cachedDrawable));

        return this.cachedDrawable.draw();
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        return getTextPosition();
    }

    @Override
    public String toString() {
        return Character.toString(this.c);
    }

    @Override
    public int getDescent() {
        return storage.getFonts().getDescent(getFormat().getFontName(), getFormat().getFontsize());
    }

}
