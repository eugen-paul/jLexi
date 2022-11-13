package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;

public class SelectedEffectV2 implements GlyphEffect {

    List<TextElementV2> glyphs;

    public SelectedEffectV2(List<TextElementV2> glyphs) {
        this.glyphs = glyphs;
        for (var glyph : glyphs) {
            glyph.addEffect(this);
        }
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
    public void execute() {
        for (var glyph : glyphs) {
            glyph.updateEffect(this);
        }
        if (!glyphs.isEmpty()) {
            //TODO
            // glyphs.get(0).redraw();
        }
    }

    @Override
    public void terminate() {
        for (var glyph : glyphs) {
            glyph.removeEffect(this);
            glyph.updateEffect(null);
        }
        // TODO: do it better
        if (!glyphs.isEmpty()) {
            //TODO
            // glyphs.get(0).redraw();
        }
    }

    @Override
    public void addToDrawable(DrawableSketch drawable) {
        int[] cursorsPixels = new int[(int) drawable.getSize().compArea()];
        int[] originalPixels = drawable.draw().asArgbPixels();

        for (int i = 0; i < originalPixels.length; i++) {
            int color = originalPixels[i] & 0xFFFFFF;

            // remove alpha and invert colors
            cursorsPixels[i] = 0xFF000000 | (0xFFFFFF - color);
        }

        Drawable cursorDrawable = DrawablePixelsImpl.builderArgb()//
                .argbPixels(cursorsPixels)//
                .size(drawable.getSize())//
                .build();

        drawable.addDrawable(cursorDrawable, 0, 0, 1);
    }

}
