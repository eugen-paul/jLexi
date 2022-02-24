package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.effect.TextPaneEffect;

public abstract class TextElement extends Glyph implements EffectHolder {

    @Getter
    protected TextField parentTextField;
    protected TextStructureForm parentStructureForm;
    private List<TextPaneEffect> effects;

    protected TextElement(Glyph parent) {
        super(parent);
        effects = new LinkedList<>();
    }

    public abstract boolean isEndOfLine();

    public abstract boolean isPlaceHolder();

    public abstract boolean isCursorHoldable();

    public abstract void remove();

    public abstract void reset();

    @Override
    public void addEffect(TextPaneEffect effect) {
        effects.add(effect);
    }

    @Override
    public void removeEffect(TextPaneEffect effect) {
        effects.remove(effect);
    }

    @Override
    public void updateEffect(TextPaneEffect effect) {
        // TODO Auto-generated method stub
    }
}
