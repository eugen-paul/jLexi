package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.utils.Size;

@Slf4j
public class CursorEffectBox implements GlyphEffect {

    private static final int CURSOR_COLOR = 0xFF0000FF;

    private boolean showCursor;

    private TextElement glyph;

    public CursorEffectBox(TextElement glyph) {
        this.glyph = glyph;
        this.showCursor = false;
        this.glyph.addEffect(this);
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
        if (this.showCursor) {
            this.showCursor = false;
            LOGGER.trace("hide Cursor on {}.", this.glyph);
        } else {
            this.showCursor = true;
            LOGGER.trace("show Cursor on {}.", this.glyph);
        }

        this.glyph.updateEffect(this);
        this.glyph.redraw();
        return this;
    }

    @Override
    public void terminate() {
        LOGGER.trace("terminate Cursor on {}.", this.glyph);
        this.glyph.removeEffect(this);
        this.glyph.updateEffect(null);
        this.glyph.redraw();
    }

    @Override
    public void addToDrawable(DrawableSketch drawable) {
        if (this.showCursor) {
            int[] cursorsPixelsV = new int[drawable.getSize().getHeight()];
            Arrays.fill(cursorsPixelsV, CURSOR_COLOR);

            Drawable cursorDrawableV = DrawablePixelsImpl.builderArgb()//
                    .argbPixels(cursorsPixelsV)//
                    .size(new Size(1, cursorsPixelsV.length))//
                    .build();

            drawable.addDrawable(cursorDrawableV, 0, 0, 1);
            drawable.addDrawable(cursorDrawableV, drawable.getSize().getWidth() - 1, 0, 1);

            int[] cursorsPixelsH = new int[drawable.getSize().getWidth()];
            Arrays.fill(cursorsPixelsH, CURSOR_COLOR);

            Drawable cursorDrawableH = DrawablePixelsImpl.builderArgb()//
                    .argbPixels(cursorsPixelsH)//
                    .size(new Size(cursorsPixelsH.length, 1))//
                    .build();

            drawable.addDrawable(cursorDrawableH, 0, 0, 1);
            drawable.addDrawable(cursorDrawableH, 0, drawable.getSize().getHeight() - 1, 1);
        }
    }

}
