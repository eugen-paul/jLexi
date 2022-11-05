package net.eugenpaul.jlexi.component.text.format.structure.textelements;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;

public class TextCharV2 extends TextElementV2 {

    @Getter
    private final Character c;

    public TextCharV2(ResourceManager storage, TextStructureV2 parentStructure, Character c, TextFormat format,
            TextFormatEffect formatEffect) {
        super(storage, parentStructure, format, formatEffect);
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

        doEffects(this.cachedDrawable);

        getFormatEffect().getFormatter(this.storage.getFormats()).stream()//
                .forEach(f -> f.doFormat(this.cachedDrawable));

        return this.cachedDrawable.draw();
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
