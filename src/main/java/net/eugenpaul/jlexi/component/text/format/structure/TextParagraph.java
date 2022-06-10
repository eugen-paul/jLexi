package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextRepresentation> {

    private LinkedList<TextElement> textElements;

    private TextParagraphConfiguration config;

    private boolean needRestruct = true;

    public TextParagraph(TextStructure parentStructure) {
        super(parentStructure);
        this.textElements = new LinkedList<>();

        this.config = TextParagraphConfiguration.builder()//
                .build();
    }

    public TextParagraph(TextStructure parentStructure, TextParagraphConfiguration config) {
        super(parentStructure);
        this.textElements = new LinkedList<>();

        this.config = config;
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
            var words = this.config.getTextToWordCompositor().compose(this.textElements.iterator());
            this.representation = this.config.getTextToRowsCompositor().compose(words, size);
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

        textElements.addAll(0, previousParagraph.textElements);

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
        var newParagraph = new TextParagraph(this.parentStructure);

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
                newParagraph = new TextParagraph(this.parentStructure);
                doSplit = true;
            }
        }
        if (!newParagraph.isEmpty()) {
            splits.add(newParagraph);
        }
    }

    public void add(TextElement element) {
        this.textElements.add(element);
        element.setStructureParent(this);

        setRestructIfNeeded(element);
    }

    @Override
    public TextRemoveResponse removeElement(TextElement elementToRemove) {
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
            return TextRemoveResponse.EMPTY;
        }

        if (nextElement == null) {
            // There is no following element. Try to merge the paragraph with the following paragraph.
            if (this.parentStructure != null) {
                // The last character (elementToRemove) is deleted before the merge.
                textElements.removeLast();
                var newCursorPosition = this.parentStructure.mergeChildWithNext(this);
                if (newCursorPosition != null) {
                    notifyChange();
                    return new TextRemoveResponse(//
                            elementToRemove, //
                            newCursorPosition.getTextPosition() //
                    );
                } else {
                    // Merge was not successful. Last character (elementToRemove) must be added to the text elements
                    // again.
                    textElements.addLast(elementToRemove);
                }
            }
            return TextRemoveResponse.EMPTY;
        }

        removeElementFromText(elementToRemove);

        notifyChange();
        return new TextRemoveResponse(//
                elementToRemove, //
                nextElement.getTextPosition() //
        );
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
    public TextRemoveResponse removeElementBefore(TextElement position) {
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
            return TextRemoveResponse.EMPTY;
        }

        if (elementToRemove != null) {
            removeElementFromText(elementToRemove);

            notifyChange();

            return new TextRemoveResponse(//
                    elementToRemove, //
                    position.getTextPosition() //
            );
        }

        // No previous element was found. Check if you can merge the paragraph with the previous paragraph.
        if (this.parentStructure != null) {
            var removedElement = this.parentStructure.mergeChildWithPrevious(this);
            if (removedElement != null) {
                notifyChange();
                return new TextRemoveResponse(//
                        removedElement, //
                        position.getTextPosition() //
                );
            }
        }
        return TextRemoveResponse.EMPTY;
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

    public void setToEol(ResourceManager storage) {
        if (getLastElement().isEndOfLine()) {
            return;
        }

        if (!textElements.isEmpty()) {
            add(TextElementFactory.genNewLineChar(//
                    null, //
                    storage, //
                    null, //
                    textElements.getLast().getFormat(), //
                    textElements.getLast().getFormatEffect() //
            ));
        } else {
            add(TextElementFactory.genNewLineChar(//
                    null, //
                    storage, //
                    null, //
                    TextFormat.DEFAULT, //
                    TextFormatEffect.DEFAULT_FORMAT_EFFECT //
            ));
        }
    }

    public void setToEos(ResourceManager storage) {
        if (getLastElement().isEndOfSection()) {
            return;
        }

        if (getLastElement().isEndOfLine()) {
            textElements.removeLast();
        }

        if (!textElements.isEmpty()) {
            add(TextElementFactory.genNewSectionChar(//
                    null, //
                    storage, //
                    null, //
                    textElements.getLast().getFormat(), //
                    textElements.getLast().getFormatEffect() //
            ));
        } else {
            add(TextElementFactory.genNewSectionChar(//
                    null, //
                    storage, //
                    null, //
                    TextFormat.DEFAULT, //
                    TextFormatEffect.DEFAULT_FORMAT_EFFECT //
            ));
        }
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
