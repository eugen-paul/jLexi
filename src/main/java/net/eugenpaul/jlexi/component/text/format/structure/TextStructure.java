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
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Basic structure of a document element, such as a paragraph, a section, or the similar. The object is the component
 * object of the composite pattern. The object can be a part of other <code>TextStructure</code> and/or contain other
 * <code>TextStructure</code>. It provides the direct child objects with the functions to split, merge and access
 * previous/next objects.
 */
public abstract class TextStructure implements TextDocumentElement, Splitable<TextStructure>, Empty {

    @Getter
    @Setter
    protected TextStructure parentStructure;
    protected List<TextRepresentation> representation;

    protected List<TextStructure> splits;

    protected TextStructure(TextStructure parentStructure) {
        this.parentStructure = parentStructure;
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

    public abstract TextAddResponse splitChild(TextStructure child, List<TextStructure> to);

    protected abstract TextRemoveResponse mergeWith(TextStructure element);

    protected abstract TextRemoveResponse mergeChildsWithNext(TextStructure child);

    public abstract Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB);

    public abstract List<TextElement> getAllTextElements();

    public abstract List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to);

    public abstract List<TextElement> getAllTextElementsFrom(TextElement from);

    public abstract List<TextElement> getAllTextElementsTo(TextElement to);

    protected List<TextStructure> getElementPath(TextElement element) {
        List<TextStructure> path = new LinkedList<>();
        TextStructure currentStructure = element.getStructureParent();
        while (currentStructure != null) {
            path.add(0, currentStructure);
            currentStructure = currentStructure.getParentStructure();
        }

        return path;
    }

    protected Optional<TextStructure> getPreviousChild(TextStructure position) {
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

    protected Optional<TextStructure> getNextChild(TextStructure position) {
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

    protected abstract void restructChildren();

    public void notifyChangeUp() {
        this.representation = null;
        if (null != this.parentStructure) {
            this.parentStructure.notifyChangeUp();
        }
    }

    public void notifyChangeDown() {
        this.representation = null;
        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            childIterator.next().notifyChangeDown();
        }
    }

    protected void updateParentOfChildRecursiv() {
        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            var child = childIterator.next();
            child.updateParentOfChildRecursiv();
            child.setParentStructure(this);
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

    public abstract void clear();

    public TextRemoveResponse removeElement(TextElement elementToRemove) {
        TextStructure child = getChildWithElement(elementToRemove);
        if (null == child) {
            return TextRemoveResponse.EMPTY;
        }
        return child.removeElement(elementToRemove);
    }

    public TextAddResponse addBefore(TextElement position, TextElement element) {
        TextStructure child = getChildWithElement(element);
        if (null == child) {
            return TextAddResponse.EMPTY;
        }
        return child.addBefore(position, element);
    }

    public boolean replaceStructure(//
            TextStructure owner, //
            List<TextStructure> oldStructure, //
            List<TextStructure> newSructure //
    ) {
        if (owner == this) {
            var childIterator = childListIterator();
            while (childIterator.hasNext()) {
                var child = childIterator.next();
                if (child == oldStructure.get(0)) {
                    childIterator.remove();
                    // TODO do it better
                    for (int i = 1; i < oldStructure.size(); i++) {
                        childIterator.next();
                        childIterator.remove();
                    }

                    newSructure.forEach(childIterator::add);
                    newSructure.forEach(v -> v.setParentStructure(this));
                    newSructure.forEach(TextStructure::updateParentOfChildRecursiv);

                    notifyChangeDown();
                    notifyChangeUp();

                    return true;
                }
            }
        } else if (this.parentStructure != null) {
            return this.parentStructure.replaceStructure(owner, oldStructure, newSructure);
        }

        throw new IllegalArgumentException("Cann't restore child structure. Owner or child not found.");
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

    protected abstract ListIterator<TextStructure> childListIterator(int index);

    protected abstract TextStructure getFirstChild();

    protected abstract TextStructure getLastChild();

    protected abstract TextElement getFirstElement();

    protected abstract TextElement getLastElement();
}
