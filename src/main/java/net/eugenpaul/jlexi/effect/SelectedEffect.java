package net.eugenpaul.jlexi.effect;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;

public class SelectedEffect extends GlyphEffect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectedEffect.class);

    public SelectedEffect(TextElement glyph) {
        super(glyph);
        glyph.addEffect(this);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Duration timeToNextExecute() {
        return Duration.ofDays(1);
    }

    @Override
    public GlyphEffect doEffect() {
        getGlyph().updateEffect(this);
        return this;
    }

    @Override
    public void terminate() {
        LOGGER.trace("terminate Selection on {}.", getGlyph());
        getGlyph().removeEffect(this);
        getGlyph().updateEffect(null);
    }

    @Override
    public void addToDrawable(DrawableSketch drawable) {
        int[] cursorsPixels = new int[(int) drawable.getSize().compArea()];
        int[] originalPixels = drawable.draw().asArgbPixels();

        for (int i = 0; i < originalPixels.length; i++) {
            int color = originalPixels[i];
            int blue = color & 0xFF;
            int red = color & 0xFF0000;
            int green = color & 0xFF00;

            // remove alpha and invert colors
            cursorsPixels[i] = 0xFF000000 //
                    | (0xFF0000 - red) //
                    | (0xFF00 - green) //
                    | (0xFF - blue) //
            ;
        }

        Drawable cursorDrawable = DrawablePixelsImpl.builderArgb()//
                .argbPixels(cursorsPixels)//
                .size(drawable.getSize())//
                .build();

        drawable.addDrawable(cursorDrawable, 0, 0, 1);
    }

}
