package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;

public final class TextStructureData extends TextStructureOfStructureV2 {

    public TextStructureData() {
        super(null);
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        return Collections.emptyList();
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return false;
    }

    @Override
    protected TextStructureOfStructureV2 copyStructure() {
        return null;
    }

    public void add(TextStructureV2 element) {
        this.children.add(element);
        element.setParentStructure(this);
    }

    @Override
    protected boolean hasToBeSplited(TextStructureV2 newChild) {
        return false;
    }

    @Override
    public void notifyChangeDown() {
        // nothing todo
    }

    @Override
    public void notifyChangeUp() {
        // nothing todo
    }

    @Override
    public void clear() {
        this.children.clear();
    }

}
