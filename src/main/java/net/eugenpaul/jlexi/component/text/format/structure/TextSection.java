package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextSection extends TextStructure implements GlyphIterable<TextStructureForm> {

    private LinkedList<TextParagraph> children;

    protected TextSection(TextStructure parentStructure, FormatAttribute format, ResourceManager storage) {
        super(parentStructure, format, storage);
        this.children = new LinkedList<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated constructor stub
    }

    public Iterator<TextStructureForm> printableChildIterator() {
        return new ListOfListIterator<>(children);
    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            var iterator = printableChildIterator();
            while (iterator.hasNext()) {
                structureForm.add(iterator.next());
            }
        }
        return structureForm;
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        children.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    protected void restructureChildren() {
        // TODO
    }

    @Override
    protected Iterator<TextStructure> childIterator() {
        return new TextStructureToIterator<>(children);
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

}
