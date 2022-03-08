package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public class TextSection extends TextStructure implements GlyphIterable<TextStructureForm> {

    private List<TextParagraph> paragraphs;

    protected TextSection(TextStructure parentStructure, FormatAttribute format, FontStorage fontStorage) {
        super(parentStructure, format, fontStorage);
        this.paragraphs = new LinkedList<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated constructor stub
    }

    public Iterator<TextStructureForm> printableChildIterator() {
        return new ListOfListIterator<>(paragraphs);
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
        paragraphs.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    protected void restructureChildren() {
        // TODO
    }
}
