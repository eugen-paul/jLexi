package net.eugenpaul.jlexi.effect;

import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.scheduler.DynamicEvent;

public interface GlyphEffect extends DynamicEvent{

    public void addToDrawable(DrawableSketch drawable);

    public boolean isDone();
}
