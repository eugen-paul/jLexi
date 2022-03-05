package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.effect.TextPaneEffectV2;

public interface EffectHolderV2 {
    public void addEffect(TextPaneEffectV2 effect);

    public void removeEffect(TextPaneEffectV2 effect);

    public void updateEffect(TextPaneEffectV2 effect);
}
