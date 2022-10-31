package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextEmpty extends TextStructure {

    protected TextEmpty(TextStructure parentStructure) {
        super(parentStructure);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return false;
    }

    @Override
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {
        return null;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        return null;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        return null;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB) {
        return Optional.empty();
    }

    @Override
    public List<TextElement> getAllTextElements() {
        return Collections.emptyList();
    }

    @Override
    public List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to) {
        return Collections.emptyList();
    }

    @Override
    public List<TextElement> getAllTextElementsFrom(TextElement from) {
        return Collections.emptyList();
    }

    @Override
    public List<TextElement> getAllTextElementsTo(TextElement to) {
        return Collections.emptyList();
    }

    @Override
    public void clear() {
        // nothing to do

    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return Collections.emptyListIterator();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator(int index) {
        return Collections.emptyListIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return null;
    }

    @Override
    protected TextStructure getLastChild() {
        return null;
    }

    @Override
    protected TextElement getFirstElement() {
        return null;
    }

    @Override
    protected TextElement getLastElement() {
        return null;
    }
}
