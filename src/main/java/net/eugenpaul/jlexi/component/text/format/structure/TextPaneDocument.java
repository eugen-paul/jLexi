package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneDocument extends TextStructure {

    private TextPane textPane;

    public TextPaneDocument(TextFormat format, ResourceManager storage, String text, TextPane textPane) {
        super(null, format, storage);
        this.textPane = textPane;
        initParagraphs(text);
    }

    public TextPaneDocument(TextFormat format, ResourceManager storage) {
        super(null, format, storage);
    }

    private void initParagraphs(String text) {
        String[] paragraphText = text.split("\n");
        children = Stream.of(paragraphText) //
                .map(v -> new TextParagraph(this, format, storage, v)) //
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        children.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    public void notifyChange(boolean restructure) {
        if (restructure) {
            restructChildren();
        }
        structureForm = null;
        textPane.notifyChange();
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
    public boolean childCompleteTest() {
        return true;
    }

}
