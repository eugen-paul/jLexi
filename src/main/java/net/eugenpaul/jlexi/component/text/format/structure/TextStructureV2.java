package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.Empty;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;

/**
 * Basic structure of a document element, such as a paragraph, a section, or the similar. The object is the component
 * object of the composite pattern. The object can be a part of other <code>TextStructure</code> and/or contain other
 * <code>TextStructure</code>. It provides the direct child objects with the functions to split, merge and access
 * previous/next objects.
 */
public abstract class TextStructureV2 implements TextDocumentElement, Empty, Iterable<TextStructureV2> {

    @Getter
    @Setter
    private TextStructureV2 parentStructure;

    @Getter
    @Setter
    private List<TextRepresentationV2> representation;

    protected TextStructureV2(TextStructureV2 parentStructure) {
        this.parentStructure = parentStructure;
        this.representation = null;
    }

    /**
     * Check whether the object can be merged with the given element.
     * 
     * @param element
     * @return
     */
    protected abstract boolean checkMergeWith(TextStructureV2 element);

    protected abstract boolean canContainChild(TextStructureV2 element);

    public abstract TextAddResponseV2 replaceChild(TextStructureV2 child, List<TextStructureV2> to);

    protected abstract TextSplitResponse doSplit(TextStructureV2 position);

    protected abstract TextMergeResponseV2 doMerge(TextStructureV2 next);

    protected abstract TextRemoveResponseV2 mergeWith(TextStructureV2 nextElement);

    protected abstract TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child);

    public abstract Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB);

    public abstract TextStructureV2 getSelectedAll();

    public abstract TextStructureV2 getSelectedBetween(TextElementV2 from, TextElementV2 to);

    public abstract TextStructureV2 getSelectedFrom(TextElementV2 from);

    public abstract TextStructureV2 getSelectedTo(TextElementV2 to);

    public abstract TextStructureV2 removeBetween(TextElementV2 from, TextElementV2 to);

    public abstract TextStructureV2 removeFrom(TextElementV2 from);

    public abstract TextStructureV2 removeTo(TextElementV2 to);

    protected abstract boolean isComplete();

    protected abstract void setComplete();

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isEndOfSection() {
        return false;
    }

    protected abstract void removeEoS();

    protected abstract void removeEoL();

    protected List<TextStructureV2> getPathToElement(TextElementV2 element) {
        LinkedList<TextStructureV2> path = new LinkedList<>();
        TextStructureV2 currentStructure = element.getParentStructure();

        while (currentStructure != this) {
            path.addFirst(currentStructure);
            currentStructure = currentStructure.getParentStructure();
        }

        return path;
    }

    protected Optional<TextStructureV2> getPreviousChild(TextStructureV2 position) {
        var iterator = childListIterator();
        TextStructureV2 previousChild = null;
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

    protected Optional<TextStructureV2> getNextChild(TextStructureV2 position) {
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

    public abstract List<TextRepresentationV2> getRepresentation(Size size);

    public abstract void clear();

    public TextRemoveResponseV2 removeElement(TextStructureV2 elementToRemove) {
        // TODO
        TextStructureV2 child = getChildWithElement(elementToRemove);
        if (null == child) {
            return TextRemoveResponseV2.EMPTY;
        }
        return child.removeElement(elementToRemove);
    }

    public boolean replaceStructure(//
            TextStructureV2 owner, //
            List<TextStructureV2> oldStructure, //
            List<TextStructureV2> newSructure //
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
                    newSructure.forEach(TextStructureV2::updateParentOfChildRecursiv);

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

    /**
     * The method returns the child node that contains the target node, or the target node if the current node is the
     * target node.
     * 
     * @param element
     * @return
     */
    protected TextStructureV2 getChildWithElement(TextStructureV2 element) {
        if (element == this) {
            return this;
        }

        var responsePath = element;
        var parentToCheck = element.getParentStructure();
        while (parentToCheck != null) {
            if (parentToCheck == this) {
                return responsePath;
            }
            responsePath = parentToCheck;
            parentToCheck = parentToCheck.getParentStructure();
        }

        return null;
    }

    public boolean isChildOf(TextStructureV2 parentToCheck) {
        var currentParent = getParentStructure();
        while (currentParent != null) {
            if (currentParent == parentToCheck) {
                return true;
            }
            currentParent = currentParent.getParentStructure();
        }
        return false;
    }

    @Override
    public Iterator<TextStructureV2> iterator() {
        return childListIterator();
    }

    protected abstract ListIterator<TextStructureV2> childListIterator();

    protected abstract ListIterator<TextStructureV2> childListIterator(int index);

    protected abstract TextStructureV2 getFirstChild();

    protected abstract TextStructureV2 getLastChild();

    protected TextAddResponseV2 addBefore(TextStructureV2 position, TextCopyData element) {
        var currentParent = getParentStructure();
        if (currentParent != null) {
            return currentParent.addBefore(position, element);
        }
        return TextAddResponseV2.EMPTY;
    }
}
