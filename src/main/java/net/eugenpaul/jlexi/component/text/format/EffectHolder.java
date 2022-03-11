package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.effect.TextPaneEffect;

public interface EffectHolder {
    public void addEffect(TextPaneEffect effect);

    public void removeEffect(TextPaneEffect effect);

    public void updateEffect(TextPaneEffect effect);
}
