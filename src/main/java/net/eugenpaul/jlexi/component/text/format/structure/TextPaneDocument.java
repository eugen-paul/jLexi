package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneDocument extends TextStructureOfStructure {

    private ChangeListener parent;
    private ResourceManager storage;

    public TextPaneDocument(List<TextSection> data, ChangeListener parent, ResourceManager storage) {
        super(null);
        this.parent = parent;
        this.children.addAll(data);
        this.children.forEach(v -> v.setParentStructure(this));
        this.storage = storage;
    }

    public TextPaneDocument(ResourceManager storage, ChangeListener parent) {
        super(null);
        this.parent = parent;
        this.storage = storage;
        initEmptyDocument();
    }

    private void initEmptyDocument() {
        TextSection section = new TextSection(this, TextSectionConfiguration.builder().build());
        TextParagraph paragraph = new TextParagraph(section, this.storage);
        paragraph.add(TextElementFactory.genNewLineChar(//
                this.storage, //
                TextFormat.DEFAULT, //
                TextFormatEffect.DEFAULT_FORMAT_EFFECT//
        ));
        children.add(section);
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
