package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.effect.GlyphEffect;

public interface EffectHolder {
    public void addEffect(GlyphEffect effect);

    public void removeEffect(GlyphEffect effect);

    public void updateEffect(GlyphEffect effect);
}
