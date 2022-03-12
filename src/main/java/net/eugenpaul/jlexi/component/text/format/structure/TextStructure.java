package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.Empty;
import net.eugenpaul.jlexi.component.text.format.Splitable;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextStructure implements TextDocumentElement, Splitable<TextStructure>, Empty {

    protected TextStructure parentStructure;
    protected TextFormat format;
    protected ResourceManager storage;
    protected List<TextStructureForm> structureForm;

    protected List<TextStructure> splits;

    protected TextStructure(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        this.parentStructure = parentStructure;
        this.format = format;
        this.storage = storage;
        this.structureForm = null;
        this.splits = new LinkedList<>();
    }

    public abstract void setFormat(TextElement from, TextElement to);

    protected abstract void restructureChildren();

    public void notifyChange(boolean restructure) {
        if (restructure) {
            restructureChildren();
        }
        structureForm = null;
        if (null != parentStructure) {
            parentStructure.notifyChange(restructure);
        }
    }

    public abstract List<TextStructureForm> getRows(Size size);

    public abstract void resetStructure();

    public abstract TextElement removeElement(TextElement element);

    public abstract void addBefore(TextElement position, TextElement element);

    @Override
    public List<TextStructure> getSplits() {
        return splits;
    }

    @Override
    public void clearSplitter() {
        splits.clear();
    }

    protected abstract Iterator<TextStructure> childIterator();

    protected void remove(TextStructure element) {
        var iterator = childIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                iterator.remove();
                return;
            }
        }
    }

    public TextStructure getNextStructure(TextStructure element) {
        var iterator = childIterator();
        var found = false;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                found = true;
                continue;
            }
            if (found && !currentElement.isEmpty()) {
                return currentElement;
            }
        }
        return null;
    }

    public TextStructure getPrevious(TextStructure element) {
        var iterator = childIterator();
        TextStructure lastNotEmptyElement = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                return lastNotEmptyElement;
            }
            if (!currentElement.isEmpty()) {
                lastNotEmptyElement = currentElement;
            }
        }
        return null;
    }
}
