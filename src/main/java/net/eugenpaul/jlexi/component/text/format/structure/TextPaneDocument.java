package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.utils.Size;

public class TextPaneDocument extends TextStructure {

    protected TextPaneDocument(FormatAttribute format) {
        super(format);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<TextStructureForm> printableIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            var iterator = printableIterator();
            while (iterator.hasNext()) {
                structureForm.add(iterator.next());
            }
        }
        return structureForm;
    }
}
