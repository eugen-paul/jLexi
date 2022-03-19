package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.component.text.TextPane;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneDocument extends TextStructureOfStructure {

    private TextPane textPane;

    public TextPaneDocument(TextFormat format, ResourceManager storage, String text, TextPane textPane) {
        super(null, format, storage);
        this.textPane = textPane;
        initParagraphs(text);
    }

    public TextPaneDocument(TextFormat format, ResourceManager storage, List<TextElement> data, TextPane textPane) {
        super(null, format, storage);
        this.textPane = textPane;
        initParagraphs(data);
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

    private void initParagraphs(List<TextElement> data) {
        children.clear();

        var currentParagraph = new TextParagraph(this, format, storage);

        var iterator = data.iterator();
        while (iterator.hasNext()) {
            var element = iterator.next();
            currentParagraph.add(element);

            if (element.isEndOfLine()) {
                children.add(currentParagraph);
                currentParagraph = new TextParagraph(this, format, storage);
            }
        }

        if (!currentParagraph.isEmpty()) {
            children.add(currentParagraph);
        }
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
