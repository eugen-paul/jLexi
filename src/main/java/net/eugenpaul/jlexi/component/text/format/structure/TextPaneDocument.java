package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.text.converter.TextData;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextPaneDocument extends TextStructureOfStructure {

    private ChangeListener parent;
    private ResourceManager storage;

    public TextPaneDocument(ResourceManager storage, ChangeListener parent) {
        super(null);
        this.parent = parent;
        this.storage = storage;
        initEmptyDocument();
    }

    public void setText(TextData data) {
        this.children.clear();
        this.children.addAll(data.getSections());
        this.children.forEach(v -> v.setParentStructure(this));
        setRepresentation(null);
    }

    private void initEmptyDocument() {
        TextSection section = new TextSection(this, TextSectionConfiguration.builder().build());
        TextParagraph paragraph = new TextParagraph(section, this.storage);
        paragraph.add(TextElementFactory.genNewLineChar(//
                this.storage, //
                TextFormat.DEFAULT, //
                TextFormatEffect.DEFAULT_FORMAT_EFFECT//
        ));
        this.children.add(section);
    }

    @Override
    public void notifyChangeUp() {
        setRepresentation(null);
        if (this.parent != null) {
            this.parent.notifyChange();
        }
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return false;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        // Document ist the root class and cann't be merged with other elements
        return TextRemoveResponse.EMPTY;
    }

    @Override
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {
        var iterator = this.children.listIterator();

        while (iterator.hasNext()) {
            var elem = iterator.next();
            if (elem == child) {
                iterator.remove();
                to.forEach(iterator::add);
                to.forEach(v -> v.setParentStructure(this));

                notifyChangeUp();

                return new TextAddResponse(//
                        this, //
                        child, //
                        to //
                );
            }
        }

        return TextAddResponse.EMPTY;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        var nextChild = getNextChild(child);

        if (nextChild.isPresent()) {
            var removedData = child.mergeWith(nextChild.get());
            if (removedData != TextRemoveResponse.EMPTY) {
                var iterator = this.children.listIterator();
                while (iterator.hasNext()) {
                    // TODO do it better
                    var currentChild = iterator.next();
                    if (currentChild == child) {
                        iterator.remove();
                        iterator.next();
                        iterator.remove();
                        var newStructureList = removedData.getNewStructures();

                        newStructureList.forEach(v -> {
                            iterator.add(v);
                            v.setParentStructure(this);
                        });

                        break;
                    }
                }
            }

            notifyChangeDown();
            notifyChangeUp();

            return new TextRemoveResponse(//
                    removedData.getRemovedElement(), //
                    removedData.getNewCursorPosition(), //
                    this, //
                    removedData.getRemovedStructures(), //
                    removedData.getNewStructures() //
            );
        }
        // Document ist the root class. No need to check if parentStructure is present.

        return TextRemoveResponse.EMPTY;
    }

}
