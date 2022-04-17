package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextElement extends Glyph implements EffectHolder, TextDocumentElement {

    @Getter(value = AccessLevel.PROTECTED)
    @Setter
    protected TextStructure structureParent;
    private List<GlyphEffect> effects;
    @Getter
    private TextFormat format;
    @Getter
    private TextFormatEffect formatEffect;
    @Getter
    private TextPosition textPosition;

    protected TextElement(Glyph parent, TextStructure structureParent, TextFormat format,
            TextFormatEffect formatEffect) {
        super(parent);
        this.structureParent = structureParent;
        this.format = format;
        this.formatEffect = formatEffect;
        this.effects = new LinkedList<>();
        this.textPosition = new TextPosition(this);
    }

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isEndOfSection() {
        return false;
    }

    public abstract boolean isCursorHoldable();

    public abstract TextPosition getCursorElementAt(Vector2d pos);

    public void reset() {
        cachedDrawable = null;
    }

    @Override
    public void addEffect(GlyphEffect effect) {
        effects.add(effect);
    }

    @Override
    public void removeEffect(GlyphEffect effect) {
        effects.remove(effect);
    }

    protected Drawable doEffects(Drawable element) {
        effects.stream()//
                .forEach(v -> v.editDrawable(element));
        return element;
    }

    @Override
    public void updateEffect(GlyphEffect effect) {
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

    public boolean isChildOf(TextStructure parentToCheck) {
        var currentParent = structureParent;
        while (currentParent != null) {
            if (currentParent == parentToCheck) {
                return true;
            }
            currentParent = currentParent.getParentStructure();
        }
        return false;
    }

    public boolean addBefore(TextElement element) {
        if (structureParent == null) {
            return false;
        }
        return structureParent.addBefore(this, element);
    }

    public TextElement removeElement(List<TextElement> removedElements) {
        if (structureParent == null) {
            return null;
        }
        return structureParent.removeElement(this, removedElements);
    }

    public boolean removeElementBefore(List<TextElement> removedElements) {
        if (structureParent == null) {
            return false;
        }

        return structureParent.removeElementBefore(this, removedElements);
    }

    public void notifyChange() {
        if (structureParent == null) {
            return;
        }
        structureParent.notifyChange();
    }

    public TextElement getTextElement() {
        return this;
    }

}
