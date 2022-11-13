package net.eugenpaul.jlexi.effect;

import java.time.Duration;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextPaneDocumentRoot;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.utils.Size;

@Slf4j
public class CursorEffectV2 implements GlyphEffect {

    private static final int CURSOR_COLOR = 0xFF0000FF;

    private boolean showCursor;

    private TextElementV2 element;

    private TextPaneDocumentRoot docRoot;

    public CursorEffectV2(TextElementV2 element, TextPaneDocumentRoot docRoot) {
        this.element = element;
        this.showCursor = false;
        this.element.addEffect(this);
        this.docRoot = docRoot;
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
    public void execute() {
        if (this.showCursor) {
            this.showCursor = false;
            LOGGER.trace("hide Cursor on {}.", this.element);
        } else {
            this.showCursor = true;
            LOGGER.trace("show Cursor on {}.", this.element);
        }

        this.element.updateEffect(this);
        this.docRoot.redrawDocument();
    }

    @Override
    public void terminate() {
        LOGGER.trace("terminate Cursor on {}.", this.element);
        this.element.removeEffect(this);
        this.element.updateEffect(null);
        this.docRoot.redrawDocument();
    }

    @Override
    public void addToDrawable(DrawableSketch drawable) {
        if (this.showCursor) {
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
