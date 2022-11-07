package net.eugenpaul.jlexi.component.text.format.structure;

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
public abstract class TextStructureV2 implements TextDocumentElement, Empty {

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

    public abstract TextAddResponseV2 splitChild(TextStructureV2 child, List<TextStructureV2> to);

    protected abstract TextRemoveResponseV2 mergeWith(TextStructureV2 element);

    protected abstract TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child);

    public abstract Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB);

    public abstract List<TextElementV2> getAllTextElements();

    public abstract List<TextElementV2> getAllTextElementsBetween(TextElementV2 from, TextElementV2 to);

    public abstract List<TextElementV2> getAllTextElementsFrom(TextElementV2 from);

    public abstract List<TextElementV2> getAllTextElementsTo(TextElementV2 to);

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isEndOfSection() {
        return false;
    }

    protected List<TextStructureV2> getElementPath(TextElementV2 element) {
        List<TextStructureV2> path = new LinkedList<>();
        TextStructureV2 currentStructure = element.getParentStructure();
        while (currentStructure != null) {
            path.add(0, currentStructure);
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

    /**
     * Returns the list of {@code TextRepresentation} that represent the object. Depending on the type, the
     * {@code TextRepresentation} object can be a row, a column, a page or other.
     * 
     * @param size - max size of the returned object
     * @return response
     */
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (this.representation != null) {
            return this.representation;
        }

        this.representation = new LinkedList<>();
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            this.representation.addAll(iterator.next().getRepresentation(size));
        }

        return this.representation;
    }

    public abstract void clear();

    public TextRemoveResponseV2 removeElement(TextElementV2 elementToRemove) {
        TextStructureV2 child = getChildWithElement(elementToRemove);
        if (null == child) {
            return TextRemoveResponseV2.EMPTY;
        }
        return child.removeElement(elementToRemove);
    }

    public TextAddResponse addBefore(TextElementV2 position, TextElementV2 element) {
        TextStructureV2 child = getChildWithElement(element);
        if (null == child) {
            return TextAddResponse.EMPTY;
        }
        return child.addBefore(position, element);
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

    protected TextStructureV2 getChildWithElement(TextElementV2 element) {
        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            var child = childIterator.next();
            if (element.isChildOf(child)) {
                return child;
            }
        }
        return null;
    }

    protected abstract ListIterator<TextStructureV2> childListIterator();

    protected abstract ListIterator<TextStructureV2> childListIterator(int index);

    protected abstract TextStructureV2 getFirstChild();

    protected abstract TextStructureV2 getLastChild();
}
