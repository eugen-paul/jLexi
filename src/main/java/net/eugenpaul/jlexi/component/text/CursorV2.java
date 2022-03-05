package net.eugenpaul.jlexi.component.text;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.effect.CursorEffectV2;
import net.eugenpaul.jlexi.effect.EffectController;
import net.eugenpaul.jlexi.effect.TextPaneEffectV2;

public class CursorV2 {

    @Getter
    private TextElement currentGlyph;
    private TextPaneEffectV2 effect;
    private EffectController effectHandler;

    public CursorV2(TextElement glyphElement, TextPaneEffectV2 effect, EffectController effectHandler) {
        this.currentGlyph = glyphElement;
        this.effect = effect;
        this.effectHandler = effectHandler;
    }

    public void moveCursorTo(TextElement newGlyph) {
        if (null != currentGlyph && null != effect) {
            currentGlyph.removeEffect(effect);
            effectHandler.removeEffectFromController(effect);
        }

        if (null == newGlyph) {
            return;
        }

        currentGlyph = newGlyph;
        effect = new CursorEffectV2(newGlyph);
        effectHandler.addEffectToController(effect);
    }
}
