package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;

public class TextEmptyV2 extends TextStructureV2 {

    protected TextEmptyV2(TextStructureV2 parentStructure) {
        super(parentStructure);
    }

    @Override
    public boolean isEmpty() {
        return true;
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
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 element) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    protected TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
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
    public void clear() {
        // nothing to do

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
        return null;
    }

    @Override
    protected TextStructureV2 getLastChild() {
        return null;
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        return Collections.emptyList();
    }

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
}
