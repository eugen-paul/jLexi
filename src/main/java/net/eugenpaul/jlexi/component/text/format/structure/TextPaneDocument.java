package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneDocument extends TextStructureOfStructure {

    private ChangeListener parent;

    public TextPaneDocument(TextFormat format, ResourceManager storage, List<TextElement> data, ChangeListener parent) {
        super(null, format, storage);
        this.parent = parent;
        initParagraphs(data);
    }

    public TextPaneDocument(TextFormat format, ResourceManager storage) {
        super(null, format, storage);
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
        if (parent != null) {
            parent.notifyChange();
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
    public boolean childCompleteTest() {
        return true;
    }

}
