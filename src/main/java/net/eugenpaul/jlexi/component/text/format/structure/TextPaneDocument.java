package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public class TextPaneDocument extends TextStructure {

    private List<TextParagraph> paragraphs;

    public TextPaneDocument(FormatAttribute format, FontStorage fontStorage, String text) {
        super(format);
        initParagraphs(fontStorage, text);
    }

    private void initParagraphs(FontStorage fontStorage, String text) {
        String[] paragraphText = text.split("\n");
        paragraphs = Stream.of(paragraphText) //
                .map(v -> new TextParagraph(new FormatAttribute(), fontStorage, v)) //
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected TextPaneDocument(FormatAttribute format) {
        super(format);
        paragraphs = new LinkedList<>();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub

    }

    // public Iterator<TextStructureForm> printableChildIterator() {
    // return new ListOfListIterator<>(paragraphs);
    // }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            paragraphs.stream().map(v -> v.getRows(size)).forEach(structureForm::addAll);
            // var iterator = printableChildIterator();
            // while (iterator.hasNext()) {
            // structureForm.add(iterator.next());
            // }
        }
        return structureForm;
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        paragraphs.stream().forEach(TextStructure::resetStructure);
    }
}
