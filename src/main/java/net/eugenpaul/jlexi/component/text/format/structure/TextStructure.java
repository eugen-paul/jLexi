package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.Empty;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextStructure implements TextDocumentElement, Splitable<TextStructure>, Empty {

    @Getter
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

    protected abstract boolean checkMergeWith(TextStructure element);

    protected abstract TextElement mergeWithNext(TextStructure element);

    protected abstract TextElement mergeWithPrevious(TextStructure element);

    protected TextElement mergeChildWithPrevious(TextStructure child) {
        var previousChild = getPreviousChild(child);

        if (previousChild.isEmpty()) {
            if (parentStructure != null) {
                parentStructure.mergeChildWithNext(this);
            }
            return null;
        }

        if (!previousChild.get().checkMergeWith(child)) {
            return null;
        }

        TextElement separator = previousChild.get().mergeWithNext(child);
        if (separator != null) {
            removeChild(child);
            return separator;
        }
        return null;
    }

    protected TextElement mergeChildWithNext(TextStructure child) {
        var nextChild = getNextChild(child);

        if (nextChild.isEmpty()) {
            if (parentStructure != null) {
                return parentStructure.mergeChildWithPrevious(this);
            }
            return null;
        }

        if (!nextChild.get().checkMergeWith(child)) {
            return null;
        }

        TextElement position = nextChild.get().mergeWithPrevious(child);
        if (position != null) {
            removeChild(child);
            return position;
        }
        return null;
    }

    private Optional<TextStructure> getPreviousChild(TextStructure position) {
        var iterator = childListIterator();
        TextStructure previousChild = null;
        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (currentChild == position) {
                break;
            }
            previousChild = currentChild;
        }
        if (previousChild == null) {
            return Optional.empty();
        }
        return Optional.of(previousChild);
    }

    private Optional<TextStructure> getNextChild(TextStructure position) {
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == position) {
                if (iterator.hasNext()) {
                    return Optional.of(iterator.next());
                }
                break;
            }
        }
        return Optional.empty();
    }

    private void removeChild(TextStructure child) {
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == child) {
                iterator.remove();
                break;
            }
        }
    }

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

    public void notifyChange() {
        restructChildren();
        structureForm = null;
        if (null != parentStructure) {
            parentStructure.notifyChange();
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

    protected void resetStructure() {
        structureForm = null;
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            iterator.next().resetStructure();
        }
    }

    public abstract void clear();

    public TextElement removeElement(TextElement element, List<TextElement> removedElements) {
        TextStructure child = getChildWithElement(element);
        if (null == child) {
            return null;
        }
        return child.removeElement(element, removedElements);
    }

    public boolean removeElementBefore(TextElement position, List<TextElement> removedElements) {
        TextStructure child = getChildWithElement(position);
        if (null == child) {
            return false;
        }

        return child.removeElementBefore(position, removedElements);
    }

    public boolean addBefore(TextElement position, TextElement element) {
        TextStructure child = getChildWithElement(element);
        if (null == child) {
            return false;
        }
        return child.addBefore(position, element);
    }

    protected TextStructure getChildWithElement(TextElement element) {
        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            var child = childIterator.next();
            if (element.isChildOf(child)) {
                return child;
            }
        }
        return null;
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
}
