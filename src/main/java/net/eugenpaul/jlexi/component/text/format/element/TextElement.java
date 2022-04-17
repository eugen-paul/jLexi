package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.CursorPosition;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextElement extends Glyph implements EffectHolder, TextDocumentElement, CursorPosition {

    @Getter(value = AccessLevel.PROTECTED)
    @Setter
    protected TextStructure structureParent;
    private List<GlyphEffect> effects;
    @Getter
    private TextFormat format;
    @Getter
    private TextFormatEffect formatEffect;

    protected TextElement(Glyph parent, TextStructure structureParent, TextFormat format,
            TextFormatEffect formatEffect) {
        super(parent);
        this.structureParent = structureParent;
        this.format = format;
        this.formatEffect = formatEffect;
        this.effects = new LinkedList<>();
    }

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isEndOfSection() {
        return false;
    }

    public abstract boolean isCursorHoldable();

    public abstract TextElement getCursorElementAt(Vector2d pos);

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

    @Override
    public boolean addBefore(TextElement element) {
        if (structureParent == null) {
            return false;
        }
        return structureParent.addBefore(this, element);
    }
    
    @Override
    public TextElement removeElement(List<TextElement> removedElements) {
        if (structureParent == null) {
            return null;
        }
        return structureParent.removeElement(this, removedElements);
    }
    
    @Override
    public boolean removeElementBefore(List<TextElement> removedElements) {
        if (structureParent == null) {
            return false;
        }
        
        return structureParent.removeElementBefore(this, removedElements);
    }
    
    @Override
    public void notifyChange() {
        if (structureParent == null) {
            return;
        }
        structureParent.notifyChange();
    }

    @Override
    public TextElement getTextElement() {
        return this;
    }

}
