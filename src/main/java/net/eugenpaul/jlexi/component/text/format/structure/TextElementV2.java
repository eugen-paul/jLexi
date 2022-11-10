package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.iterator.TextStructureV2ToListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPositionV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketch;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextElementV2 extends TextStructureV2 implements EffectHolder {

    @Getter(value = AccessLevel.PROTECTED)
    private List<GlyphEffect> effects;

    protected ResourceManager storage;

    @Getter
    private TextFormat format;

    @Getter
    private TextFormatEffect formatEffect;

    @Getter
    private TextPositionV2 textPosition;

    protected DrawableSketch cachedDrawable;

    protected TextElementV2(ResourceManager storage, TextStructureV2 parentStructure, TextFormat format,
            TextFormatEffect formatEffect) {
        super(parentStructure);
        this.storage = storage;
        this.format = format;
        this.formatEffect = formatEffect;
        this.effects = new LinkedList<>();
        this.textPosition = new TextPositionV2(this);
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

    @Override
    public void updateEffect(GlyphEffect effect) {
        notifyChangeUp();
    }

    protected void doEffects(DrawableSketch element) {
        this.effects.stream()//
                .forEach(v -> v.addToDrawable(element));
    }

    public abstract Drawable getDrawable();

    public Size getSize() {
        return getDrawable().getSize();
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (getRepresentation() != null) {
            return getRepresentation();
        }

        var response = new TextPaneElement(null, this);

        return List.of(response);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return false;
    }

    @Override
    public TextAddResponseV2 splitChild(TextStructureV2 child, List<TextStructureV2> to) {
        return TextAddResponseV2.EMPTY;
    }

    @Override
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 element) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    protected TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child) {
        if (getParentStructure() == null) {
            return TextRemoveResponseV2.EMPTY;
        }

        var selfCopy = copy();

        return new TextRemoveResponseV2(//
                selfCopy.getTextPosition(), //
                getParentStructure(), //
                List.of(this, child), //
                List.of(selfCopy) //
        );
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<TextElementV2> getAllTextElements() {
        return List.of(this);
    }

    @Override
    public List<TextElementV2> getAllTextElementsBetween(TextElementV2 from, TextElementV2 to) {
        return List.of(this);
    }

    @Override
    public List<TextElementV2> getAllTextElementsFrom(TextElementV2 from) {
        return List.of(this);
    }

    @Override
    public List<TextElementV2> getAllTextElementsTo(TextElementV2 to) {
        return List.of(this);
    }

    @Override
    public void clear() {
        // Nothing to do
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator() {
        return new TextStructureV2ToListIterator<>(List.of(this));
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator(int index) {
        if (index == 0) {
            return childListIterator();
        }
        return Collections.emptyListIterator();
    }

    @Override
    protected TextStructureV2 getFirstChild() {
        return this;
    }

    @Override
    protected TextStructureV2 getLastChild() {
        return this;
    }

    public boolean isChildOf(TextStructureV2 parentToCheck) {
        var currentParent = getParentStructure();
        while (currentParent != null) {
            if (currentParent == parentToCheck) {
                return true;
            }
            currentParent = currentParent.getParentStructure();
        }
        return false;
    }

    public abstract int getDescent();

    public abstract TextElementV2 copy();

}
