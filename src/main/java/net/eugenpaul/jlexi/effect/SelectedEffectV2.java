package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.iterator.PreOrderLeafIterator;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentRoot;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;

public class SelectedEffectV2 implements GlyphEffect {

    private TextStructureV2 rootStructure;
    private TextPaneDocumentRoot docRoot;

    public SelectedEffectV2(TextStructureV2 rootStructure, TextPaneDocumentRoot docRoot) {
        this.rootStructure = rootStructure;
        this.docRoot = docRoot;

        var iterator = new PreOrderLeafIterator<TextStructureV2>(rootStructure);
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof EffectHolder) {
                var target = (EffectHolder) element;
                target.addEffect(this);
            }
        }
        // TODO
        // for (var glyph : glyphs) {
        // glyph.addEffect(this);
        // }
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
        if (rootStructure.isEmpty()) {
            return;
        }

        var iterator = new PreOrderLeafIterator<TextStructureV2>(rootStructure);
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof EffectHolder) {
                var target = (EffectHolder) element;
                target.updateEffect(this);
            }
        }

        // TODO
        // for (var glyph : glyphs) {
        // glyph.updateEffect(this);
        // }
        docRoot.redrawDocument();
    }

    @Override
    public void terminate() {
        if (rootStructure.isEmpty()) {
            return;
        }

        var iterator = new PreOrderLeafIterator<TextStructureV2>(rootStructure);
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof EffectHolder) {
                var target = (EffectHolder) element;
                target.removeEffect(this);
                target.updateEffect(null);
            }
        }

        // TODO
        // for (var glyph : glyphs) {
        // glyph.removeEffect(this);
        // glyph.updateEffect(null);
        // }
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
