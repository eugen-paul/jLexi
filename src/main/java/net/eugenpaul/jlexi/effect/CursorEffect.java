package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class CursorEffect extends TextPaneEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CursorEffect.class);

    private static final int CURSOR_COLOR = 0xFF0000FF;

    private boolean showCursor;

    public CursorEffect(TextPaneElement glyph) {
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
    public Effect doEffect() {
        if (showCursor) {
            showCursor = false;
            if (getGlyph() instanceof CharGlyph) {
                CharGlyph c = (CharGlyph) getGlyph();
                LOGGER.trace("hide Cursor on char {}.", c.getC());
            } else {
                LOGGER.trace("hide Cursor on {}.", getGlyph().getClass().getName());
            }
        } else {
            showCursor = true;
            if (getGlyph() instanceof CharGlyph) {
                CharGlyph c = (CharGlyph) getGlyph();
                LOGGER.trace("show Cursor on char {}.", c.getC());
            } else {
                LOGGER.trace("show Cursor on {}.", getGlyph().getClass().getName());
            }
        }

        getGlyph().updateEffect();
        return this;
    }

    @Override
    public void editDrawable(Drawable pixels) {
        if (showCursor) {
            int[] cursorsPixels = new int[pixels.getPixelSize().getHeight()];
            Arrays.fill(cursorsPixels, CURSOR_COLOR);

            ImageArrayHelper.copyRectangle(cursorsPixels, //
                    new Size(1, pixels.getPixelSize().getHeight()), //
                    new Vector2d(0, 0), //
                    new Size(1, pixels.getPixelSize().getHeight()), //
                    pixels.getPixels(), //
                    pixels.getPixelSize(), //
                    new Vector2d(0, 0) //
            );
        }
    }

    @Override
    public void terminate() {
        if (getGlyph() instanceof CharGlyph) {
            CharGlyph c = (CharGlyph) getGlyph();
            LOGGER.trace("terminate Cursor on char {}.", c.getC());
        } else {
            LOGGER.trace("terminate Cursor on {}.", getGlyph().getClass().getName());
        }
        getGlyph().removeEffect(this);
        getGlyph().updateEffect();
    }

}
