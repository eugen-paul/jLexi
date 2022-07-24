package net.eugenpaul.jlexi.component.text.format.element;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextWordBreak extends TextElementAbstract {

    private static final Character WORD_BREAK = '-';

    @Getter
    private TextPosition textPosition;
    @Getter
    private TextPositionV2 textPositionV2;

    public TextWordBreak(Glyph parent, ResourceManager storage, TextStructure parentStructure, TextFormat format,
            TextFormatEffect formatEffect, TextElement followingElement) {
        super(parent, storage, parentStructure, format, formatEffect);
        textPosition = followingElement.getTextPosition();
        getDrawable();
    }

    @Override
    public Drawable getDrawable() {
        if (null != cachedDrawable) {
            return cachedDrawable.draw();
        }

        cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        Drawable charDrawable = storage.getFonts().ofChar2(WORD_BREAK, getFormat());
        cachedDrawable.addDrawable(charDrawable, 0, 0, 0);

        setSize(cachedDrawable.getSize());

        doEffects(cachedDrawable);

        getFormatEffect().getFormatter(storage.getFormats()).stream()//
                .forEach(f -> f.doFormat(cachedDrawable));

        return cachedDrawable.draw();
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        return getTextPosition();
    }

    @Override
    public TextPositionV2 getCursorElementAtV2(Vector2d pos) {
        return getTextPositionV2();
    }

    @Override
    public String toString() {
        return Character.toString(WORD_BREAK);
    }

    @Override
    public int getDescent() {
        return storage.getFonts().getDescent(getFormat().getFontName(), getFormat().getFontsize());
    }

}
