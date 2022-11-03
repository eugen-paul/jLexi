package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.iterator.TextStructureV2ToListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.effect.GlyphEffect;

public class TextElementV2 extends TextStructureV2 implements EffectHolder {

    private List<GlyphEffect> effects;

    @Getter
    private TextFormat format;

    @Getter
    private TextFormatEffect formatEffect;

    @Getter
    private TextPosition textPosition;

    protected TextElementV2(TextStructureV2 parentStructure, TextFormat format, TextFormatEffect formatEffect) {
        super(parentStructure);
        this.format = format;
        this.formatEffect = formatEffect;
        this.effects = new LinkedList<>();
        this.textPosition = new TextPosition(this);
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

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return false;
    }

    @Override
    public TextAddResponse splitChild(TextStructureV2 child, List<TextStructureV2> to) {
        return TextAddResponse.EMPTY;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructureV2 element) {
        return TextRemoveResponse.EMPTY;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructureV2 child) {
        return TextRemoveResponse.EMPTY;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<TextElementV2> getAllTextElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElementV2> getAllTextElementsBetween(TextElementV2 from, TextElementV2 to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElementV2> getAllTextElementsFrom(TextElementV2 from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElementV2> getAllTextElementsTo(TextElementV2 to) {
        // TODO Auto-generated method stub
        return null;
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

}
