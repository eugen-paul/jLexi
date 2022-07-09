package net.eugenpaul.jlexi.effect;

import java.time.Duration;

import net.eugenpaul.jlexi.draw.DrawableSketch;

public interface GlyphEffect {

    public void addToDrawable(DrawableSketch drawable);

    public boolean isDone();

    public GlyphEffect doEffect();

    public Duration timeToNextExecute();

    public void terminate();
}
