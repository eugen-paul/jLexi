package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructureOfElements implements GlyphIterable<TextRepresentation> {

    private TextParagraphConfiguration config;

    private ResourceManager storage;

    public TextParagraph(TextStructure parentStructure, ResourceManager storage) {
        this(//
                parentStructure, //
                TextParagraphConfiguration.builder().build(), //
                storage //
        );
    }

    public TextParagraph(TextStructure parentStructure, TextParagraphConfiguration config, ResourceManager storage) {
        super(parentStructure);

        this.config = config;
        this.storage = storage;
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
            var words = this.config.getTextToWordCompositor().compose(this.children.iterator(), this.storage);
            this.representation = this.config.getTextToRowsCompositor().compose(words, size);
        }
        return this.representation;
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return element instanceof TextParagraph;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        if (!checkMergeWith(element)) {
            return TextRemoveResponse.EMPTY;
        }

        var nextParagraph = (TextParagraph) element;
        TextElement removedSeparator = null;

        var responseParagraph = new TextParagraph(parentStructure, config, storage);

        responseParagraph.children.addAll(this.children);
        if (responseParagraph.children.getLast().isEndOfLine()) {
            removedSeparator = responseParagraph.children.removeLast();
        }

        TextPosition newCursorPosition = nextParagraph.children.getFirst().getTextPosition();

        responseParagraph.children.addAll(nextParagraph.children);
        responseParagraph.children.stream().forEach(v -> v.setStructureParent(responseParagraph));

        return new TextRemoveResponse(//
                removedSeparator, //
                newCursorPosition, //
                this, //
                List.of(this, nextParagraph), //
                List.of(responseParagraph) //
        );
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        // TODO Auto-generated method stub
        return TextRemoveResponse.EMPTY;
    }

    @Override
    protected void updateParentOfChildRecursiv() {
        var childIterator = children.iterator();
        while (childIterator.hasNext()) {
            var child = childIterator.next();
            child.setStructureParent(this);
        }
    }

    public void add(TextElement element) {
        this.children.add(element);
        element.setStructureParent(this);
    }

    @Override
    public TextRemoveResponse removeElement(TextElement elementToRemove) {
        // TODO, check if the element is in the child-list
        var nextElement = getNext(elementToRemove);

        if (nextElement.isEmpty()) {
            // There is no following element. Try to merge the paragraph with the following paragraph.
            if (this.parentStructure != null) {
                return this.parentStructure.mergeChildsWithNext(this);
            }
            return TextRemoveResponse.EMPTY;
        }

        removeChild(elementToRemove);

        notifyChangeUp();
        return new TextRemoveResponse(//
                elementToRemove, //
                nextElement.get().getTextPosition() //
        );
    }

    @Override
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {
        // Paragraph cann't be splited by this method
        return TextAddResponse.EMPTY;
    }

    @Override
    public TextAddResponse addBefore(TextElement position, TextElement element) {

        if (position.getStructureParent() != this) {
            return TextAddResponse.EMPTY;
        }

        TextAddResponse response = TextAddResponse.EMPTY;
        if (isSplitNeeded(element)) {
            var newParagraphs = split(position, element);
            if (this.parentStructure != null) {
                return this.parentStructure.splitChild(this, newParagraphs);
            }
        } else {
            response = addElementBefore(position, element);
        }

        notifyChangeUp();

        return response;
    }

    private List<TextStructure> split(TextElement position, TextElement separator) {
        TextParagraph first = new TextParagraph(parentStructure, config, storage);
        TextParagraph second = new TextParagraph(parentStructure, config, storage);
        TextParagraph current = first;

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var currentElement = chiltIterator.next();
            if (currentElement == position) {
                if (separator != null) {
                    current.children.add(separator);
                    separator.setStructureParent(current);
                }
                current = second;
            }
            current.children.add(currentElement);
            currentElement.setStructureParent(current);
        }

        return List.of(first, second);
    }

    private TextAddResponse addElementBefore(TextElement position, TextElement element) {
        var iterator = this.children.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);
                break;
            }
        }
        return new TextAddResponse(position.getTextPosition());
    }

    @Override
    public void notifyChangeDown() {
        this.representation = null;
    }

    private boolean isSplitNeeded(TextElement addedElement) {
        return addedElement.isEndOfLine();
    }

    @Override
    public void clear() {
        this.children.clear();
        this.representation = null;
    }

    public void setToEol(ResourceManager storage) {
        if (getLastElement().isEndOfLine()) {
            return;
        }

        if (!children.isEmpty()) {
            add(TextElementFactory.genNewLineChar(//
                    null, //
                    storage, //
                    null, //
                    children.getLast().getFormat(), //
                    children.getLast().getFormatEffect() //
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
            children.removeLast();
        }

        if (!children.isEmpty()) {
            add(TextElementFactory.genNewSectionChar(//
                    null, //
                    storage, //
                    null, //
                    children.getLast().getFormat(), //
                    children.getLast().getFormatEffect() //
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
        return !this.children.isEmpty() && this.children.getLast().isEndOfSection();
    }

    public TextElement removeEndOfSection() {
        if (isEndOfSection()) {
            return this.children.pollLast();
        }
        throw new NullPointerException("Paragraph has no EndOfSection");
    }
}
