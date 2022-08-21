package net.eugenpaul.jlexi.component.text.format.element;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.structure.TextAddResponse;
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
        var currentParent = this.structureParent;
        while (currentParent != null) {
            if (currentParent == parentToCheck) {
                return true;
            }
            currentParent = currentParent.getParentStructure();
        }
        return false;
    }

    public TextAddResponse addBefore(TextElement element) {
        if (this.structureParent == null) {
            return TextAddResponse.EMPTY;
        }
        return this.structureParent.addBefore(this, element);
    }

    public boolean replaceStructure(//
            TextStructure owner, //
            List<TextStructure> oldStructure, //
            List<TextStructure> newStructure //
    ) {
        if (this.structureParent == null) {
            return false;
        }
        return this.structureParent.replaceStructure(owner, oldStructure, newStructure);
    }

    public TextRemoveResponse removeElement() {
        if (this.structureParent == null) {
            return TextRemoveResponse.EMPTY;
        }
        return this.structureParent.removeElement(this);
    }

    @Override
    public void notifyChange() {
        this.cachedDrawable = null;
        if (this.structureParent == null) {
            return;
        }
        this.structureParent.notifyChangeUp();
    }

    public TextElement getTextElement() {
        return this;
    }

    public int getDescent() {
        return 0;
    }

}
