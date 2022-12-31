package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
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

    protected TextPaneElement elementRep;
    protected List<TextRepresentationV2> elementRepList;

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
        this.elementRep = new TextPaneElement(null, this);
        this.elementRepList = List.of(elementRep);
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

    public void updateFormat(TextFormat newFormat) {
        this.format = newFormat;
        notifyChangeUp();
    }

    @Override
    public void notifyChangeUp() {
        this.cachedDrawable = null;
        super.notifyChangeUp();
        setRepresentation(elementRepList);
    }

    @Override
    public void notifyChangeDown() {
        this.cachedDrawable = null;
        super.notifyChangeDown();
        setRepresentation(elementRepList);
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
        return elementRepList;
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
    protected boolean canContainChild(TextStructureV2 element) {
        return false;
    }

    @Override
    public TextAddResponseV2 replaceChild(TextStructureV2 child, List<TextStructureV2> to) {
        return TextAddResponseV2.EMPTY;
    }

    @Override
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 nextElement) {
        if (getParentStructure() == null) {
            return TextRemoveResponseV2.EMPTY;
        }

        var selfCopy = copy();

        return new TextRemoveResponseV2(//
                selfCopy.getTextPosition(), //
                getParentStructure(), //
                List.of(this, nextElement), //
                List.of(selfCopy) //
        );
    }

    @Override
    protected TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
        if (elemA == this || elemB == this) {
            return Optional.of(Boolean.TRUE);
        }
        return Optional.empty();
    }

    @Override
    public TextStructureV2 getSelectedAll() {
        return this;
    }

    @Override
    public TextStructureV2 getSelectedBetween(TextElementV2 from, TextElementV2 to) {
        return this;
    }

    @Override
    public TextStructureV2 getSelectedFrom(TextElementV2 from) {
        return this;
    }

    @Override
    public TextStructureV2 getSelectedTo(TextElementV2 to) {
        return this;
    }

    @Override
    public TextStructureV2 removeBetween(TextElementV2 from, TextElementV2 to) {
        return this;
    }

    @Override
    public TextStructureV2 removeFrom(TextElementV2 from) {
        return this;
    }

    @Override
    public TextStructureV2 removeTo(TextElementV2 to) {
        return this;
    }

    @Override
    public void clear() {
        // Nothing to do
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator() {
        return Collections.emptyListIterator();
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator(int index) {
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

    public abstract int getDescent();

    public abstract TextElementV2 copy();

    @Override
    protected boolean isComplete() {
        return true;
    }

    @Override
    protected void setComplete() {
        // nothing to do
    }

    @Override
    protected TextMergeResponseV2 doMerge(TextStructureV2 next) {
        return TextMergeResponseV2.EMPTY;
    }

    @Override
    protected TextSplitResponse doSplit(TextStructureV2 position) {
        return TextSplitResponse.EMPTY;
    }

    @Override
    protected void removeEoS() {
        // nothing to do
    }

    @Override
    protected void removeEoL() {
        // nothing to do
    }

    public TextAddResponseV2 addBefore(TextCopyData element) {
        var currentParent = getParentStructure();
        if (currentParent != null) {
            return currentParent.addBefore(this, element);
        }
        return TextAddResponseV2.EMPTY;
    }
}
