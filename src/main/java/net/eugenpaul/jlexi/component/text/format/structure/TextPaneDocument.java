package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.component.text.TextPaneExtended;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public class TextPaneDocument extends TextStructure {

    private LinkedList<TextParagraph> paragraphs;
    private TextPaneExtended textPane;

    public TextPaneDocument(FormatAttribute format, FontStorage fontStorage, String text, TextPaneExtended textPane) {
        super(null, format, fontStorage);
        this.textPane = textPane;
        initParagraphs(text);
    }

    private void initParagraphs(String text) {
        String[] paragraphText = text.split("\n");
        paragraphs = Stream.of(paragraphText) //
                .map(v -> new TextParagraph(this, new FormatAttribute(), fontStorage, v)) //
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected TextPaneDocument(FormatAttribute format, FontStorage fontStorage) {
        super(null, format, fontStorage);
        paragraphs = new LinkedList<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            paragraphs.stream().map(v -> v.getRows(size)).forEach(structureForm::addAll);
        }
        return structureForm;
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        paragraphs.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    public void notifyChange(boolean restructure) {
        if (restructure) {
            restructureChildren();
        }
        structureForm = null;
        textPane.notifyChange();
    }

    @Override
    protected void restructureChildren() {
        var iterator = paragraphs.listIterator();
        while (iterator.hasNext()) {
            var paragraph = iterator.next();
            if (!paragraph.getSplits().isEmpty()) {
                paragraph.getSplits().stream()//
                        .filter(TextParagraph.class::isInstance)//
                        .forEach(v -> iterator.add((TextParagraph) v));
                paragraph.clearSplitter();
            }
        }
    }

    @Override
    public List<TextStructure> getSplits() {
        return Collections.emptyList();
    }

    @Override
    public void clearSplitter() {
        // nothing to do.
    }
}
