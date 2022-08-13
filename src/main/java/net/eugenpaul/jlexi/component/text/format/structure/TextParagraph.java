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

    private boolean needRestruct = true;

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
                List.of(List.of(this, nextParagraph)), //
                List.of(responseParagraph) //
        );
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        // TODO Auto-generated method stub
        return TextRemoveResponse.EMPTY;
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
        var iterator = this.children.listIterator();
        var newParagraph = new TextParagraph(this.parentStructure, this.config.copy(), this.storage);

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
                newParagraph = new TextParagraph(this.parentStructure, this.config.copy(), this.storage);
                doSplit = true;
            }
        }
        if (!newParagraph.isEmpty()) {
            splits.add(newParagraph);
        }
    }

    public void add(TextElement element) {
        this.children.add(element);
        element.setStructureParent(this);

        setRestructIfNeeded(element);
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
                nextElement.get().getTextPosition(), //
                Collections.emptyList(), //
                Collections.emptyList() //
        );
    }

    @Override
    public boolean addBefore(TextElement position, TextElement element) {
        var iterator = this.children.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);

                setRestructIfNeeded(element);

                notifyChangeUp();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean splitStructures(List<TextStructure> oldStructure, List<List<TextStructure>> newStructures) {
        if (this.parentStructure != null) {
            // TODO do it better
            if (!newStructures.isEmpty()) {
                for (var str : newStructures.get(0)) {
                    if (str instanceof TextParagraph) {
                        ((TextParagraph) str).children.forEach(v -> v.setStructureParent(str));
                    }
                }
            }
            return this.parentStructure.splitStructures(oldStructure, newStructures);
        }
        return false;
    }

    @Override
    public void notifyChangeDown() {
        this.representation = null;
    }

    private void setRestructIfNeeded(TextElement addedElement) {
        if (addedElement.isEndOfLine()) {
            this.needRestruct = true;
        }
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
