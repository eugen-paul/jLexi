package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextNewLine extends TextElementAbstract {

    private static final String NEW_LINE_SEPARATOR = "\n";

    public TextNewLine(Glyph parent, ResourceManager storage, TextStructure parentStructure, TextFormat format,
            TextFormatEffect formatEffect) {
        super(parent, storage, parentStructure, format, formatEffect);

        // we must compute pixels to set size of element
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

        setSize(this.cachedDrawable.getSize());

        doEffects(this.cachedDrawable);

        return this.cachedDrawable.draw();
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        return getTextPosition();
    }

    @Override
    public boolean isEndOfLine() {
        return true;
    }

    @Override
    public String toString() {
        return NEW_LINE_SEPARATOR;
    }

}
