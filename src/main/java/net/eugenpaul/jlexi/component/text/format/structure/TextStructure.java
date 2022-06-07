package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.Empty;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Basic structure of a document element, such as a paragraph, a section, or the similar.
 * 
 * The object is the component object of the composite pattern. The object can be a part of other
 * <code>TextStructure</code> and/or contain other <code>TextStructure</code>. It provides the direct child objects with
 * the functions to split, merge and access previous/next objects.
 */
public abstract class TextStructure implements TextDocumentElement, Splitable<TextStructure>, Empty {

    @Getter
    @Setter
    protected TextStructure parentStructure;
    protected TextFormat format;
    protected ResourceManager storage;
    protected List<TextRepresentation> representation;

    protected List<TextStructure> splits;

    protected TextStructure(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        this.parentStructure = parentStructure;
        this.format = format;
        this.storage = storage;
        this.representation = null;
        this.splits = new LinkedList<>();
    }

    /**
     * Check whether the object can be merged with the given element.
     * 
     * @param element
     * @return
     */
    protected abstract boolean checkMergeWith(TextStructure element);

    /**
     * The specified element is added to the end of the object.
     * 
     * @param element
     * @return Separator deleted at the end of the object when the element was added.
     */
    protected abstract TextElement mergeWithNext(TextStructure element);

    /**
     * The specified element is added to the beginning of the object.
     * 
     * @param element
     * @return The first element of current object (before the merge).
     */
    protected abstract TextElement mergeWithPrevious(TextStructure element);

    protected TextElement mergeChildWithPrevious(TextStructure child) {
        var previousChild = getPreviousChild(child);

        if (previousChild.isEmpty()) {
            if (this.parentStructure != null) {
                return this.parentStructure.mergeChildWithPrevious(this);
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
                return parentStructure.mergeChildWithNext(this);
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

    protected abstract void restructChildren();

    public void notifyChange() {
        this.representation = null;
        if (null != this.parentStructure) {
            this.parentStructure.notifyChange();
        }
    }

    public List<TextRepresentation> getRepresentation(Size size) {
        if (null == this.representation) {
            restructChildren();
            this.representation = new LinkedList<>();
            var iterator = childListIterator();
            while (iterator.hasNext()) {
                this.representation.addAll(iterator.next().getRepresentation(size));
            }
        }
        return this.representation;
    }

    protected void resetStructure() {
        this.representation = null;
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            iterator.next().resetStructure();
        }
    }

    public abstract void clear();

    public TextRemoveResponse removeElement(TextElement elementToRemove) {
        TextStructure child = getChildWithElement(elementToRemove);
        if (null == child) {
            return TextRemoveResponse.EMPTY;
        }
        return child.removeElement(elementToRemove);
    }

    public TextRemoveResponse removeElementBefore(TextElement position) {
        TextStructure child = getChildWithElement(position);
        if (null == child) {
            return TextRemoveResponse.EMPTY;
        }

        return child.removeElementBefore(position);
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
