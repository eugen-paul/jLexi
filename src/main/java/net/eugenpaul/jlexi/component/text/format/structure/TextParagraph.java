package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TextParagraph(TextStructure parentStructure, FormatAttribute format, FontStorage fontStorage, String text) {
        super(parentStructure, format, fontStorage);
        this.fields = new LinkedList<>();
        initFields(text);
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    protected TextParagraph(TextStructure parentStructure, FormatAttribute format, FontStorage fontStorage,
            List<TextField> fields) {
        super(parentStructure, format, fontStorage);
        this.fields = fields;
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    /**
     * !!JUST for Test!!<br>
     * test format: {XX:text} XX - format text - printed Text example: "Text can be: {BI:bold and italics}, {B :bold} or
     * { I:italics}."
     * 
     * @param text
     */
    private void initFields(String text) {
        Pattern tagRegex = Pattern.compile("([^\\}]*)\\{(.+?)\\}([^\\{]*)");
        final Matcher matcher = tagRegex.matcher(text);
        int counter = 0;
        while (matcher.find()) {
            counter++;
            if (!matcher.group(1).isEmpty()) {
                fields.add(new TextSpan(this, new FormatAttribute(), fontStorage, matcher.group(1)));
            }

            if (!matcher.group(2).isEmpty()) {
                var f = new FormatAttribute();
                f.setBold(matcher.group(2).charAt(0) == 'B');
                f.setItalic(matcher.group(2).charAt(1) == 'I');
                fields.add(new TextSpan(this, f, fontStorage, matcher.group(2).substring(3)));
            }

            if (!matcher.group(3).isEmpty()) {
                fields.add(new TextSpan(this, new FormatAttribute(), fontStorage, matcher.group(3)));
            }
        }
        if (counter == 0 && !text.isEmpty()) {
            fields.add(new TextSpan(this, new FormatAttribute(), fontStorage, text));
        }
        fields.add(new TextSpan(this, new FormatAttribute(), fontStorage, "\n"));
    }

    protected TextParagraph(TextStructure parentStructure, FormatAttribute format, FontStorage fontStorage) {
        super(parentStructure, format, fontStorage);
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

    @Override
    protected void restructureChildren() {
        var iterator = fields.listIterator();
        while (iterator.hasNext()) {
            var field = iterator.next();
            if (!field.getSplits().isEmpty()) {
                field.getSplits().stream().forEach(iterator::add);
                field.clearSplitter();
            }
        }
        split();
    }

    private void split() {
        ListIterator<TextField> iterator = fields.listIterator();
        LinkedList<TextField> splitter = new LinkedList<>();

        clearSplitter();

        boolean found = false;
        var newParagraph = new TextParagraph(parentStructure, format, fontStorage, splitter);

        while (iterator.hasNext()) {
            var iteratorPosition = iterator.next();
            if (found) {
                splitter.add(iteratorPosition);
                iteratorPosition.setStructureParent(newParagraph);
                iterator.remove();
            }
            if (iteratorPosition.getLastChild().isEndOfLine()) {
                found = true;
                if (!splitter.isEmpty()) {
                    splits.add(newParagraph);
                    splitter = new LinkedList<>();
                }
            }
        }

        if (!splitter.isEmpty()) {
            splits.add(newParagraph);
        }
    }

}
