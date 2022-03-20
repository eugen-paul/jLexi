package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.interfaces.Empty;
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

    public abstract boolean childCompleteTest();

    protected void restructChildren() {
        var iterator = childListIterator();

        while (iterator.hasNext()) {
            var child = iterator.next();
            if (child.isEmpty()) {
                iterator.remove();
            } else if (!child.getSplits().isEmpty()) {
                child.getSplits().stream()//
                        .forEach(iterator::add);
                child.clearSplitter();
            }
        }
    }

    public void notifyChange(boolean doRestruct) {
        if (doRestruct) {
            restructChildren();
        }
        structureForm = null;
        if (null != parentStructure) {
            parentStructure.notifyChange(doRestruct);
        }
    }

    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = new LinkedList<>();
            var iterator = childListIterator();
            while (iterator.hasNext()) {
                structureForm.addAll(iterator.next().getRows(size));
            }
        }
        return structureForm;
    }

    public abstract void resetStructure();

    public abstract void clear();

    public TextElement removeElement(TextElement element) {
        TextStructure child = getChildWithElement(element);
        if (null == child) {
            return null;
        }
        return child.removeElement(element);
    }

    public boolean addBefore(TextElement position, TextElement element) {
        TextStructure child = getChildWithElement(element);
        if (null == child) {
            return false;
        }
        return child.addBefore(position, element);
    }

    protected TextStructure getChildWithElement(TextElement element) {
        TextStructure responseChild = null;
        TextStructure parentOfElement = element.getStructureParent();
        boolean found = false;

        while (parentOfElement != null) {
            if (parentOfElement == this) {
                found = true;
                break;
            }
            responseChild = parentOfElement;
            parentOfElement = parentOfElement.parentStructure;
        }

        if (!found || null == responseChild) {
            return null;
        }
        return responseChild;
    }

    @Override
    public List<TextStructure> getSplits() {
        return splits;
    }

    @Override
    public void clearSplitter() {
        splits.clear();
    }

    protected abstract ListIterator<TextStructure> childListIterator();

    protected abstract TextStructure getFirstChild();

    protected abstract TextStructure getLastChild();

    protected abstract TextElement getFirstElement();

    protected abstract TextElement getLastElement();

    public TextStructure getNextStructure(TextStructure element) {
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == element) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
                return getNextStructureFromParent();
            }
        }
        return null;
    }

    private TextStructure getNextStructureFromParent() {
        if (parentStructure != null) {
            var next = parentStructure.getNextStructure(this);
            if (next != null) {
                return next.getFirstChild();
            }
        }
        return null;
    }

    public TextStructure getPreviousStructure(TextStructure element) {
        var iterator = childListIterator();
        TextStructure lastElement = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                if (lastElement != null) {
                    return lastElement;
                } else {
                    return getPreviousStructureFromParent();
                }
            }
            lastElement = currentElement;
        }
        return null;
    }

    private TextStructure getPreviousStructureFromParent() {
        if (parentStructure != null) {
            var next = parentStructure.getPreviousStructure(this);
            if (next != null) {
                return next.getLastChild();
            }
        }
        return null;
    }
}
