package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.EffectHolder;
import net.eugenpaul.jlexi.component.iterator.TextStructureToListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.effect.GlyphEffect;

public class TextElementV2 extends TextStructure implements EffectHolder {

    private List<GlyphEffect> effects;

    @Getter
    private TextFormat format;

    @Getter
    private TextFormatEffect formatEffect;

    @Getter
    private TextPosition textPosition;

    protected TextElementV2(TextStructure parentStructure, TextFormat format, TextFormatEffect formatEffect) {
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
    protected boolean checkMergeWith(TextStructure element) {
        return false;
    }

    @Override
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {
        return TextAddResponse.EMPTY;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        return TextRemoveResponse.EMPTY;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        return TextRemoveResponse.EMPTY;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<TextElement> getAllTextElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsFrom(TextElement from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsTo(TextElement to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear() {
        // Nothing to do
    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return new TextStructureToListIterator<>(List.of(this));
    }

    @Override
    protected ListIterator<TextStructure> childListIterator(int index) {
        if (index == 0) {
            return childListIterator();
        }
        return Collections.emptyListIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return this;
    }

    @Override
    protected TextStructure getLastChild() {
        return this;
    }

    @Override
    protected TextElement getFirstElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextElement getLastElement() {
        // TODO Auto-generated method stub
        return null;
    }

}
