package net.eugenpaul.jlexi.component.text.format.structure.textelements;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class TextNewLineV2 extends TextElementV2 {

    private static final String NEW_LINE_SEPARATOR = "\n";

    public TextNewLineV2(ResourceManager storage, TextStructureV2 parentStructure, TextFormat format,
            TextFormatEffect formatEffect) {
        super(storage, parentStructure, format, formatEffect);

        getDrawable();
    }

    public TextNewLineV2(ResourceManager storage, TextStructureV2 parentStructure) {
        super(storage, parentStructure, TextFormat.DEFAULT, TextFormatEffect.DEFAULT_FORMAT_EFFECT);

        getDrawable();
    }

    @Override
    public Drawable getDrawable() {
        if (null != this.cachedDrawable) {
            return this.cachedDrawable.draw();
        }

        int[] pixels = new int[this.storage.getFonts().getMaxAscent(//
                getFormat().getFontName(), //
                getFormat().getFontsize() //
        )];

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);

        Drawable newLineDrawable = DrawablePixelsImpl.builderArgb()//
                .argbPixels(pixels)//
                .size(new Size(1, pixels.length))//
                .build();

        this.cachedDrawable.addDrawable(newLineDrawable, 0, 0, 0);

        doEffects(this.cachedDrawable);

        getFormatEffect().getFormatter(this.storage.getFormats()).stream()//
                .forEach(f -> f.doFormat(this.cachedDrawable));

        return this.cachedDrawable.draw();
    }

    @Override
    public String toString() {
        return NEW_LINE_SEPARATOR;
    }

    @Override
    public int getDescent() {
        return storage.getFonts().getDescent(getFormat().getFontName(), getFormat().getFontsize());
    }
}
