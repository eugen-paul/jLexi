package net.eugenpaul.jlexi.component.text;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.effect.CursorEffect;
import net.eugenpaul.jlexi.effect.EffectController;
import net.eugenpaul.jlexi.effect.GlyphEffect;

public class Cursor {

    @Getter
    private TextElement currentGlyph;
    private GlyphEffect effect;
    private EffectController effectHandler;

    public Cursor(TextElement glyphElement, GlyphEffect effect, EffectController effectHandler) {
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
        effect = new CursorEffect(newGlyph);
        effectHandler.addEffectToController(effect);
    }
}
