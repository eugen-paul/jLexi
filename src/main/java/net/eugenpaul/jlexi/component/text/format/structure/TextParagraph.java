package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextElementToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextRepresentation> {

    protected TextCompositor<TextElement> compositor;
    private LinkedList<TextElement> textElements;

    private boolean needRestruct = true;

    public TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
        this.textElements = new LinkedList<>();
        // TODO get margin from paragraph konfiguration
        this.compositor = new TextElementToRowCompositor<>(0, 0);
    }

    @Override
    public Iterator<TextRepresentation> drawableChildIterator() {
        if (null == this.representation) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(this.representation);
    }

    @Override
    public List<TextRepresentation> getRepresentation(Size size) {
        if (null == this.representation) {
            this.representation = this.compositor.compose(this.textElements.iterator(), size);
        }
        return this.representation;
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return element instanceof TextParagraph;
    }

    @Override
    protected TextElement mergeWithNext(TextStructure element) {
        if (!checkMergeWith(element)) {
            return null;
        }

        TextParagraph nextParagraph = (TextParagraph) element;
        TextElement responseSeparator = null;
        if (this.textElements.getLast().isEndOfLine()) {
            responseSeparator = this.textElements.removeLast();
        }

        nextParagraph.textElements.stream().forEach(v -> v.setStructureParent(this));
        this.textElements.addAll(nextParagraph.textElements);

        this.representation = null;

        return responseSeparator;
    }

    @Override
    protected TextElement mergeWithPrevious(TextStructure element) {
        if (!checkMergeWith(element)) {
            return null;
        }

        TextParagraph previousParagraph = (TextParagraph) element;

        previousParagraph.textElements.stream().forEach(v -> v.setStructureParent(this));

        TextElement position = this.textElements.getFirst();

        var iterator = this.textElements.listIterator();
        previousParagraph.textElements.stream()//
                .filter(v -> !v.isEndOfLine())//
                .forEach(iterator::add);

        this.representation = null;

        return position;
    }

    @Override
    public void resetStructure() {
        this.representation = null;
    }

    @Override
    protected void restructChildren() {
        if (!this.needRestruct) {
            return;
        }

        checkAndSplit();

        this.needRestruct = false;
    }

    private void checkAndSplit() {
        var iterator = this.textElements.listIterator();
        var newParagraph = new TextParagraph(this.parentStructure, this.format, this.storage);

        clearSplitter();

        boolean doSplit = false;

        while (iterator.hasNext()) {
            var currentElement = iterator.next();

            if (doSplit) {
                newParagraph.add(currentElement);
                currentElement.setStructureParent(newParagraph);
                iterator.remove();
            }

            if (currentElement.isEndOfLine()) {
                if (!newParagraph.isEmpty()) {
                    splits.add(newParagraph);
                }
                newParagraph = new TextParagraph(this.parentStructure, this.format, this.storage);
                doSplit = true;
            }
        }
    }

    public void add(TextElement element) {
        this.textElements.add(element);
        element.setStructureParent(this);

        setRestructIfNeeded(element);
    }

    @Override
    public TextElement removeElement(TextElement elementToRemove, List<TextElement> removedElements) {
        var iterator = this.textElements.iterator();
        TextElement nextElement = null;
        boolean found = false;
        while (iterator.hasNext()) {
            if (iterator.next() == elementToRemove) {
                found = true;
                if (iterator.hasNext()) {
                    nextElement = iterator.next();
                }
                break;
            }
        }

        if (!found) {
            return null;
        }

        if (nextElement == null) {
            if (this.parentStructure != null) {
                var newCursorPosition = this.parentStructure.mergeChildWithNext(this);
                if (newCursorPosition != null) {
                    removedElements.add(elementToRemove);
                }
                notifyChange();
                return newCursorPosition;
            }
            return null;
        }

        removeElementFromText(elementToRemove);

        removedElements.add(elementToRemove);
        notifyChange();
        return nextElement;
    }

    private void removeElementFromText(TextElement elementToRemove) {
        var iterator = this.textElements.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == elementToRemove) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public boolean removeElementBefore(TextElement position, List<TextElement> removedElements) {
        var iterator = this.textElements.listIterator();
        TextElement elementToRemove = null;
        boolean found = false;
        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (currentChild == position) {
                found = true;
                break;
            }
            elementToRemove = currentChild;
        }

        if (!found) {
            return false;
        }

        if (elementToRemove != null) {
            removeElementFromText(elementToRemove);
            removedElements.add(elementToRemove);
            this.representation = null;
            notifyChange();
            return true;
        }

        if (this.parentStructure != null) {
            var removedElement = this.parentStructure.mergeChildWithPrevious(this);
            if (removedElement != null) {
                removedElements.add(removedElement);
                notifyChange();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addBefore(TextElement position, TextElement element) {
        var iterator = this.textElements.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);

                setRestructIfNeeded(element);

                notifyChange();
                return true;
            }
        }
        return false;
    }

    private void setRestructIfNeeded(TextElement addedElement) {
        if (addedElement.isEndOfLine()) {
            this.needRestruct = true;
        }
    }

    @Override
    public void clear() {
        this.textElements.clear();
        resetStructure();
    }

    @Override
    public boolean isEmpty() {
        return this.textElements.isEmpty();
    }

    @Override
    protected TextElement getFirstElement() {
        if (isEmpty()) {
            return null;
        }
        return this.textElements.peekFirst();
    }

    @Override
    protected TextElement getLastElement() {
        if (isEmpty()) {
            return null;
        }
        return this.textElements.peekLast();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return Collections.emptyListIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return null;
    }

    @Override
    protected TextStructure getLastChild() {
        return null;
    }

    public boolean isEndOfSection() {
        return !this.textElements.isEmpty() && this.textElements.getLast().isEndOfSection();
    }

    public TextElement removeEndOfSection() {
        if (isEndOfSection()) {
            return this.textElements.pollLast();
        }
        throw new NullPointerException("Paragraph has no EndOfSection");
    }

}
