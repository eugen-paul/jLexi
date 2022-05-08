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
        initTextStructure(data);
    }

    public TextPaneDocument(TextFormat format, ResourceManager storage) {
        super(null, format, storage);
    }

    private void initTextStructure(List<TextElement> data) {
        children.clear();

        var currentSection = new TextSection(this, format, storage);
        var currentParagraph = new TextParagraph(currentSection, format, storage);

        var iterator = data.iterator();
        while (iterator.hasNext()) {
            var element = iterator.next();
            currentParagraph.add(element);

            if (element.isEndOfLine()) {
                currentSection.add(currentParagraph);
                currentParagraph = new TextParagraph(currentSection, format, storage);
            }

            if (element.isEndOfSection()) {
                children.add(currentSection);
                currentSection = new TextSection(this, format, storage);
            }
        }
        
        if (!currentParagraph.isEmpty()) {
            currentSection.add(currentParagraph);
        }

        if (!currentSection.isEmpty()) {
            children.add(currentSection);
        }
    }

    @Override
    public void resetStructure() {
        representation = null;
        children.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    public void notifyChange() {
        restructChildren();
        representation = null;
        if (parent != null) {
            parent.notifyChange();
        }
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return false;
    }

    @Override
    protected TextElement mergeWithNext(TextStructure element) {
        return null;
    }

    @Override
    protected TextElement mergeWithPrevious(TextStructure element) {
        return null;
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
