package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextRemoveResponse;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextElement extends Glyph implements EffectHolder, TextDocumentElement {

    @Getter
    @Setter
    protected TextStructure structureParent;

    private List<GlyphEffect> effects;

    @Getter
    private TextFormat format;

    @Getter
    private TextFormatEffect formatEffect;

    @Getter
    private TextPosition textPosition;
    @Getter
    private TextPositionV2 textPositionV2;

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
    public abstract TextPositionV2 getCursorElementAtV2(Vector2d pos);

    public void reset() {
        this.cachedDrawable = null;
    }

    @Override
    public void addEffect(GlyphEffect effect) {
        this.effects.add(effect);
    }

    @Override
    public void removeEffect(GlyphEffect effect) {
        this.effects.remove(effect);
    }

    protected void doEffects(DrawableSketch element) {
        this.effects.stream()//
                .forEach(v -> v.addToDrawable(element));
    }

    @Override
    public void updateEffect(GlyphEffect effect) {
        this.cachedDrawable = null;
        if (null != this.parent) {
            this.parent.notifyChange();
        }
    }

    public void updateFormat(TextFormat newFormat) {
        this.format = newFormat;
        this.cachedDrawable = null;
        if (null != this.parent) {
            this.parent.notifyChange();
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

    public TextRemoveResponse removeElement() {
        if (structureParent == null) {
            return TextRemoveResponse.EMPTY;
        }
        return structureParent.removeElement(this);
    }

    public TextRemoveResponse removeElementBefore() {
        if (structureParent == null) {
            return TextRemoveResponse.EMPTY;
        }

        return structureParent.removeElementBefore(this);
    }

    @Override
    public void notifyChange() {
        this.cachedDrawable = null;
        if (structureParent == null) {
            return;
        }
        structureParent.notifyChange();
    }

    public TextElement getTextElement() {
        return this;
    }

    public int getDescent() {
        return 0;
    }

}
