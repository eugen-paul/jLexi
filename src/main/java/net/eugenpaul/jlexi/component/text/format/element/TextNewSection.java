package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextNewSection extends TextElementAbstract {

    public TextNewSection(Glyph parent, ResourceManager storage, TextStructure parentStructure, TextFormat format,
            TextFormatEffect formatEffect) {
        super(parent, storage, parentStructure, format, formatEffect);

        // we must compute pixels to set size of element
        getDrawable();
    }

    @Override
    public DrawableV2 getDrawable() {
        if (null != cachedDrawableV2) {
            return cachedDrawableV2.draw();
        }

        int[] pixels = new int[storage.getFonts().getMaxAscent(//
                getFormat().getFontName(), //
                getFormat().getFontsize() //
        )];

        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE);

        DrawableV2 newLineDrawable = DrawableV2PixelsImpl.builderArgb()//
                .argbPixels(pixels)//
                .size(new Size(1, pixels.length))//
                .build();

        cachedDrawableV2.addDrawable(newLineDrawable, 0, 0, 0);

        setSize(cachedDrawableV2.getSize());

        doEffects(cachedDrawableV2);

        return cachedDrawableV2.draw();
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
    public boolean isEndOfSection() {
        return true;
    }

}
