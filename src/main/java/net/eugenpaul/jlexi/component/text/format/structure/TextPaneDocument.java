package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextPaneDocument extends TextStructure {

    private LinkedList<TextParagraph> children;
    private TextPane textPane;

    public TextPaneDocument(TextFormat format, ResourceManager storage, String text, TextPane textPane) {
        super(null, format, storage);
        this.textPane = textPane;
        initParagraphs(text);
    }

    private void initParagraphs(String text) {
        String[] paragraphText = text.split("\n");
        children = Stream.of(paragraphText) //
                .map(v -> new TextParagraph(this, format, storage, v)) //
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected TextPaneDocument(TextFormat format, ResourceManager storage) {
        super(null, format, storage);
        children = new LinkedList<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            children.stream().map(v -> v.getRows(size)).forEach(structureForm::addAll);
        }
        return structureForm;
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        children.stream().forEach(TextStructure::resetStructure);
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
        var iterator = children.listIterator();
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

    @Override
    protected Iterator<TextStructure> childIterator() {
        return new TextStructureToIterator<>(children);
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public TextElement removeElement(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addBefore(TextElement position, TextElement element) {
        // TODO Auto-generated method stub

    }
}
