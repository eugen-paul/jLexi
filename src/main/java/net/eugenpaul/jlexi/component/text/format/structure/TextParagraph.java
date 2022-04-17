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
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextStructureForm> {

    protected TextCompositor<TextElement> compositor;
    private LinkedList<TextElement> textElements;

    private boolean needRestruct = true;

    public TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
        this.textElements = new LinkedList<>();
        this.compositor = new TextElementToRowCompositor<>();
    }

    @Override
    public Iterator<TextStructureForm> drawableChildIterator() {
        if (null == structureForm) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(structureForm);
    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            structureForm = compositor.compose(textElements.iterator(), size);
        }
        return structureForm;
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
        if (textElements.getLast().isEndOfLine()) {
            responseSeparator = textElements.removeLast();
        }

        nextParagraph.textElements.stream().forEach(v -> v.setStructureParent(this));
        textElements.addAll(nextParagraph.textElements);

        structureForm = null;

        return responseSeparator;
    }

    @Override
    protected TextElement mergeWithPrevious(TextStructure element) {
        if (!checkMergeWith(element)) {
            return null;
        }

        TextParagraph previousParagraph = (TextParagraph) element;

        previousParagraph.textElements.stream().forEach(v -> v.setStructureParent(this));

        TextElement position = textElements.getFirst();

        var iterator = textElements.listIterator();
        previousParagraph.textElements.stream()//
                .filter(v -> !v.isEndOfLine())//
                .forEach(iterator::add);

        structureForm = null;

        return position;
    }

    @Override
    public void resetStructure() {
        structureForm = null;
    }

    @Override
    protected void restructChildren() {
        if (!needRestruct) {
            return;
        }

        checkAndSplit();

        needRestruct = false;
    }

    private void checkAndSplit() {
        var iterator = textElements.listIterator();
        var newParagraph = new TextParagraph(parentStructure, format, storage);

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
                newParagraph = new TextParagraph(parentStructure, format, storage);
                doSplit = true;
            }
        }
    }

    public void add(TextElement element) {
        textElements.add(element);
        element.setStructureParent(this);

        setRestructIfNeeded(element);
    }

    @Override
    public TextElement removeElement(TextElement elementToRemove, List<TextElement> removedElements) {
        var iterator = textElements.iterator();
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
            if (parentStructure != null) {
                var newCursorPosition = parentStructure.mergeChildWithNext(this);
                if (newCursorPosition != null) {
                    removedElements.add(elementToRemove);
                }
                return newCursorPosition;
            }
            return null;
        }

        removeElementFromText(elementToRemove);

        structureForm = null;
        removedElements.add(elementToRemove);
        return nextElement;
    }

    private void removeElementFromText(TextElement elementToRemove) {
        var iterator = textElements.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == elementToRemove) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public boolean removeElementBefore(TextElement position, List<TextElement> removedElements) {
        var iterator = textElements.listIterator();
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
            structureForm = null;
            return true;
        }

        if (parentStructure != null) {
            var removedElement = parentStructure.mergeChildWithPrevious(this);
            if (removedElement != null) {
                removedElements.add(removedElement);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addBefore(TextElement position, TextElement element) {
        var iterator = textElements.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);

                setRestructIfNeeded(element);
                return true;
            }
        }
        return false;
    }

    private void setRestructIfNeeded(TextElement addedElement) {
        if (addedElement.isEndOfLine()) {
            needRestruct = true;
        }
    }

    @Override
    public void clear() {
        textElements.clear();
        resetStructure();
    }

    @Override
    public boolean isEmpty() {
        return textElements.isEmpty();
    }

    @Override
    protected TextElement getFirstElement() {
        if (isEmpty()) {
            return null;
        }
        return textElements.peekFirst();
    }

    @Override
    protected TextElement getLastElement() {
        if (isEmpty()) {
            return null;
        }
        return textElements.peekLast();
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

}
