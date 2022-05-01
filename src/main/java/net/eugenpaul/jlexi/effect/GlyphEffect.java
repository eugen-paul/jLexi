package net.eugenpaul.jlexi.effect;

import java.time.Duration;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.DrawableV2Sketch;

public abstract class GlyphEffect {

    @Getter
    private TextElement glyph;

    protected GlyphEffect(TextElement glyph) {
        this.glyph = glyph;
    }

    public abstract void addToDrawable(DrawableV2Sketch drawable);

    public abstract boolean isDone();

    public abstract GlyphEffect doEffect();

    public abstract Duration timeToNextExecute();

    public abstract void terminate();
}
