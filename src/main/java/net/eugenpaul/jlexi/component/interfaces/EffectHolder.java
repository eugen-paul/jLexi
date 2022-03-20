package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.effect.GlyphEffect;

public interface EffectHolder {
    public void addEffect(GlyphEffect effect);

    public void removeEffect(GlyphEffect effect);

    public void updateEffect(GlyphEffect effect);
}
