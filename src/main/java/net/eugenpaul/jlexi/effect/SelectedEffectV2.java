package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentRoot;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;

public class SelectedEffectV2 implements GlyphEffect {

    private List<TextElementV2> glyphs;
    private TextPaneDocumentRoot docRoot;

    public SelectedEffectV2(List<TextElementV2> glyphs, TextPaneDocumentRoot docRoot) {
        this.glyphs = glyphs;
        this.docRoot = docRoot;
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
        if (glyphs.isEmpty()) {
            return;
        }

        for (var glyph : glyphs) {
            glyph.updateEffect(this);
        }
        docRoot.redrawDocument();
    }

    @Override
    public void terminate() {
        if (glyphs.isEmpty()) {
            return;
        }

        for (var glyph : glyphs) {
            glyph.removeEffect(this);
            glyph.updateEffect(null);
        }
        docRoot.redrawDocument();
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
