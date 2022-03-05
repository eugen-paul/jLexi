package net.eugenpaul.jlexi.component.text;

import lombok.Getter;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.EffectController;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public class Cursor {

    @Getter
    private NodeListElement<TextPaneElement> currentGlyph;
    private TextPaneEffect effect;
    private EffectController effectHandler;

    public Cursor(NodeListElement<TextPaneElement> glyphElement, TextPaneEffect effect, EffectController effectHandler) {
        this.currentGlyph = glyphElement;
        this.effect = effect;
        this.effectHandler = effectHandler;
    }

    public void moveCursorTo(NodeListElement<TextPaneElement> newGlyph) {
        if (null != currentGlyph && null != effect) {
            currentGlyph.getData().removeEffect(effect);
            effectHandler.removeEffectFromController(effect);
        }

        if (null == newGlyph || null == newGlyph.getData()) {
            return;
        }

        currentGlyph = newGlyph;
        effect = new CursorEffect(newGlyph.getData());
        effectHandler.addEffectToController(effect);
    }
}
