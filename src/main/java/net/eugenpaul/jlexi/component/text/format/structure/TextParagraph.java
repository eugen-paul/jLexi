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

    public TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage,
            LinkedList<TextElement> children) {
        super(parentStructure, format, storage);
        this.textElements = children;
        this.compositor = new TextElementToRowCompositor<>();
    }

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
            Iterator<TextElement> elIterator = getCompositorIterator();
            structureForm = compositor.compose(elIterator, size);
        }
        return structureForm;
    }

    private Iterator<TextElement> getCompositorIterator() {
        return textElements.iterator();
    }

    @Override
    public void resetStructure() {
        structureForm = null;
    }

    @Override
    protected void restructChildren() {
        checkAndSplit();

        checkAndMergeWithNext();
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
    }

    private void checkAndMergeWithNext() {
        if (isEmpty() || textElements.getLast().isEndOfLine()) {
            return;
        }

        var nextParagraph = getNextParagraph();
        if (null == nextParagraph) {
            return;
        }

        nextParagraph.textElements.stream().forEach(v -> v.setStructureParent(this));
        textElements.addAll(nextParagraph.textElements);
        nextParagraph.clear();
    }

    @Override
    public TextElement removeElement(TextElement element) {
        if (element.getStructureParent() != this) {
            return element;
        }

        var iterator = textElements.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == element) {
                if (iterator.hasNext()) {
                    iterator.remove();
                    return iterator.next();
                } else {
                    return checkNextAndRemove(element, iterator);
                }
            }
        }
        return null;
    }

    private TextElement checkNextAndRemove(TextElement element, Iterator<TextElement> iterator) {
        if (parentStructure == null) {
            return element;
        }

        var nextStructure = parentStructure.getNextStructure(this);
        if (null == nextStructure) {
            // There is no further structure => EOL cann't be removed.
            return element;
        }

        if (!(nextStructure instanceof TextParagraph)) {
            // The next structure is not a paragraph. EOL can only be deleted if the current structure is empty.
            if (isEmpty()) {
                iterator.remove();
                return nextStructure.getFirstElement();
            }
            return element;
        }

        // The next structure is a paragraph. EOL can be deleted.
        var nextParagraph = (TextParagraph) nextStructure;
        iterator.remove();
        return nextParagraph.getFirstElement();
    }

    @Override
    public boolean addBefore(TextElement position, TextElement element) {
        if (position.getStructureParent() != this) {
            return false;
        }

        var iterator = textElements.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);
                return true;
            }
        }
        return false;
    }

    private TextParagraph getNextParagraph() {
        if (parentStructure == null) {
            return null;
        }

        var nextParagraph = parentStructure.getNextStructure(this);
        if (!(nextParagraph instanceof TextParagraph)) {
            return null;
        }
        return (TextParagraph) nextParagraph;
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
    public boolean childCompleteTest() {
        return textElements.peekLast().isEndOfLine();
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
