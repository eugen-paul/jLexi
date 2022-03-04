package net.eugenpaul.jlexi.component.text;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class CursorV2 {

    @Getter
    private TextElement currentGlyph;
    private TextPaneEffect effect;
    private EffectHandler effectHandler;

    public CursorV2(TextElement glyphElement, TextPaneEffect effect, EffectHandler effectHandler) {
        this.currentGlyph = glyphElement;
        this.effect = effect;
        this.effectHandler = effectHandler;
    }

    public void moveCursorTo(TextElement newGlyph) {
        if (null != currentGlyph && null != effect) {
            currentGlyph.removeEffect(effect);
            effectHandler.removeEffect(effect);
        }

        if (null == newGlyph) {
            return;
        }

        // currentGlyph = newGlyph;
        // effect = new CursorEffect(newGlyph);
        // effectHandler.addEffect(effect);
    }
}
