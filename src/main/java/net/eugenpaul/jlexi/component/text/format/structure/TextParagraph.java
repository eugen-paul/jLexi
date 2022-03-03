package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextElementToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.component.text.format.field.TextSpan;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextStructureForm> {

    protected TextCompositor<TextElement> fieldCompositor;
    private List<TextField> fields;

    public TextParagraph(FormatAttribute format, FontStorage fontStorage, String text) {
        super(format);
        this.fields = new LinkedList<>();
        this.fields.add(new TextSpan(this, fontStorage, new FormatAttribute(), text));
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    protected TextParagraph(FormatAttribute format) {
        super(format);
        this.fields = new LinkedList<>();
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub
    }

    @Override
    public Iterator<TextStructureForm> printableChildIterator() {
        if (null == structureForm) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(structureForm);
    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            Iterator<TextElement> elIterator = getCompositorIterator();
            structureForm = fieldCompositor.compose(elIterator, size);
        }
        return structureForm;
    }

    private Iterator<TextElement> getCompositorIterator() {
        return new ListOfListIterator<>(fields);
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        fields.stream().forEach(TextField::reset);
    }

}
