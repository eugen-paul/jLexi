package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextElement extends Glyph implements EffectHolder, TextDocumentElement {

    @Getter
    @Setter
    protected TextStructure structureParent;
    private List<TextPaneEffect> effects;
    @Getter
    private TextFormat format;

    protected TextElement(Glyph parent, TextStructure structureParent, TextFormat format) {
        super(parent);
        this.structureParent = structureParent;
        this.format = format;
        this.effects = new LinkedList<>();
    }

    public boolean isEndOfLine() {
        return false;
    }

    public abstract boolean isCursorHoldable();

    public abstract TextElement getCorsorElementAt(Vector2d pos);

    public void reset() {
        cachedDrawable = null;
    }

    @Override
    public void addEffect(TextPaneEffect effect) {
        effects.add(effect);
    }

    @Override
    public void removeEffect(TextPaneEffect effect) {
        effects.remove(effect);
    }

    protected Drawable doEffects(Drawable element) {
        effects.stream()//
                .forEach(v -> v.editDrawable(element));
        return element;
    }

    @Override
    public void updateEffect(TextPaneEffect effect) {
        if (null != parent) {
            cachedDrawable = null;
            parent.notifyRedraw(getPixels(), getRelativPosition(), getSize());
        }
    }

    /**
     * return true if the Text Element can be removed by DELETE or BACKSPACE
     * 
     * @return
     */
    public abstract boolean isRemoveable();

}
