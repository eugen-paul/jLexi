package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.EffectHolderV2;
import net.eugenpaul.jlexi.component.text.format.TextFormat;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.Effect;
import net.eugenpaul.jlexi.effect.TextPaneEffectV2;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextElement extends Glyph implements EffectHolderV2, TextFormat {

    @Getter
    @Setter
    protected TextField parentTextField;
    private List<TextPaneEffectV2> effects;

    protected TextElement(Glyph parent, TextField parentTextField) {
        super(parent);
        this.parentTextField = parentTextField;
        this.effects = new LinkedList<>();
    }

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isPlaceHolder() {
        return false;
    }

    public abstract boolean isCursorHoldable();

    public abstract TextElement getCorsorElementAt(Vector2d pos);

    // public void remove() {
    //     effects.stream().forEach(Effect::terminate);
    //     effects.clear();
    //     if (null != parentTextField) {
    //         parentTextField.notifyChange();
    //     }
    // }

    public void reset() {
        cachedDrawable = null;
    }

    @Override
    public void addEffect(TextPaneEffectV2 effect) {
        effects.add(effect);
    }

    @Override
    public void removeEffect(TextPaneEffectV2 effect) {
        effects.remove(effect);
    }

    protected Drawable doEffects(Drawable element) {
        effects.stream()//
                .forEach(v -> v.editDrawable(element));
        return element;
    }

    @Override
    public void updateEffect(TextPaneEffectV2 effect) {
        if (null != parent) {
            cachedDrawable = null;
            parent.notifyRedraw(getPixels(), getRelativPosition(), getSize());
        }
    }

}
