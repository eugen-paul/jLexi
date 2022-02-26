package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.component.text.format.field.TextSpanIterator;

public class TextParagraph extends TextStructure {

    private List<TextField> fields;

    protected TextParagraph(FormatAttribute format) {
        super(format);
        this.fields = new LinkedList<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyChange() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<TextStructure> getRows(int length) {
        Iterator<TextElement> elIterator = new TextSpanIterator(fields);
        return compositor.compose(elIterator);
    }

}
