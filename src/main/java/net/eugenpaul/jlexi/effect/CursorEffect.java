package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.utils.Size;

public class CursorEffect extends GlyphEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CursorEffect.class);

    private static final int CURSOR_COLOR = 0xFF0000FF;

    private boolean showCursor;

    public CursorEffect(TextElement glyph) {
        super(glyph);
        showCursor = false;
        glyph.addEffect(this);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Duration timeToNextExecute() {
        return Duration.ofSeconds(1);
    }

    @Override
    public GlyphEffect doEffect() {
        if (showCursor) {
            showCursor = false;
            LOGGER.trace("hide Cursor on {}.", getGlyph());
        } else {
            showCursor = true;
            LOGGER.trace("show Cursor on {}.", getGlyph());
        }

        getGlyph().updateEffect(this);
        return this;
    }

    @Override
    public void terminate() {
        LOGGER.trace("terminate Cursor on {}.", getGlyph());
        getGlyph().removeEffect(this);
        getGlyph().updateEffect(null);
    }

    @Override
    public void addToDrawable(DrawableSketch drawable) {
        if (showCursor) {
            int[] cursorsPixels = new int[drawable.getSize().getHeight()];
            Arrays.fill(cursorsPixels, CURSOR_COLOR);

            Drawable cursorDrawable = DrawablePixelsImpl.builderArgb()//
                    .argbPixels(cursorsPixels)//
                    .size(new Size(1, cursorsPixels.length))//
                    .build();

            drawable.addDrawable(cursorDrawable, 0, 0, 1);
        }
    }

}
