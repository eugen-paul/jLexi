package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraphV2 extends TextStructureOfStructureV2 {

    private TextParagraphConfiguration config;

    private ResourceManager storage;

    public TextParagraphV2(TextStructureV2 parentStructure, ResourceManager storage) {
        this(//
                parentStructure, //
                TextParagraphConfiguration.builder().build(), //
                storage //
        );
    }

    public TextParagraphV2(TextStructureV2 parentStructure, TextParagraphConfiguration config, ResourceManager storage) {
        super(parentStructure);

        this.config = config;
        this.storage = storage;
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        //TODO
        // if (null == getRepresentation()) {
        //     var words = this.config.getTextToWordCompositor().compose(this.children.iterator(), this.storage);
        //     setRepresentation(this.config.getTextToRowsCompositor().compose(words, size));
        // }
        return getRepresentation();
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return element instanceof TextParagraphV2;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructureV2 element) {
        if (!checkMergeWith(element)) {
            return TextRemoveResponse.EMPTY;
        }

        //TODO

        // var nextParagraph = (TextParagraphV2) element;
        // TextElementV2 removedSeparator = null;

        // var responseParagraph = new TextParagraphV2(getParentStructure(), config, storage);

        // responseParagraph.children.addAll(this.children);
        // if (responseParagraph.children.getLast().isEndOfLine()) {
        //     removedSeparator = responseParagraph.children.removeLast();
        // }

        // TextPosition newCursorPosition = nextParagraph.children.getFirst().getTextPosition();

        // responseParagraph.children.addAll(nextParagraph.children);
        // responseParagraph.children.stream().forEach(v -> v.setStructureParent(responseParagraph));

        // return new TextRemoveResponse(//
        //         removedSeparator, //
        //         newCursorPosition, //
        //         this, //
        //         List.of(this, nextParagraph), //
        //         List.of(responseParagraph) //
        // );

        return null;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructureV2 child) {
        // TODO ??? It may not be necessary to overwrite this function.
        return TextRemoveResponse.EMPTY;
    }

    public void add(TextElementV2 element) {
        //TODO Is it better to work with the abstract class here?
        this.children.add(element);
        element.setParentStructure(this);
    }

    @Override
    public TextRemoveResponse removeElement(TextElementV2 elementToRemove) {
        // TODO  It may not be necessary to overwrite this function.
        return TextRemoveResponse.EMPTY;

        // var nextElement = getNext(elementToRemove);

        // if (nextElement.isEmpty()) {
        //     // There is no following element. Try to merge the paragraph with the following paragraph.
        //     if (getParentStructure() != null) {
        //         return getParentStructure().mergeChildsWithNext(this);
        //     }
        //     return TextRemoveResponse.EMPTY;
        // }

        // removeChild(elementToRemove);

        // notifyChangeUp();
        // return new TextRemoveResponse(//
        //         elementToRemove, //
        //         nextElement.get().getTextPosition() //
        // );
    }

    @Override
    public TextAddResponse splitChild(TextStructureV2 child, List<TextStructureV2> to) {
        // Paragraph cann't be splited by this method -> no. new Paragraph can be splited by this method
        // TODO  It may not be necessary to overwrite this function.
        return TextAddResponse.EMPTY;
    }

    @Override
    public TextAddResponse addBefore(TextElementV2 position, TextElementV2 element) {

        if (position.getParentStructure() != this) {
            return TextAddResponse.EMPTY;
        }

        TextAddResponse response = TextAddResponse.EMPTY;
        //TODO
        // if (isSplitNeeded(element)) {
        //     var newParagraphs = split(position, element);
        //     if (getParentStructure() != null) {
        //         return getParentStructure().splitChild(this, newParagraphs);
        //     }
        // } else {
        //     response = addElementBefore(position, element);
        // }

        // notifyChangeUp();

        return response;
    }

    private List<TextStructureV2> split(TextElementV2 position, TextElementV2 separator) {
        //TODO Here you need a generic function.
        TextParagraphV2 first = new TextParagraphV2(getParentStructure(), config, storage);
        TextParagraphV2 second = new TextParagraphV2(getParentStructure(), config, storage);
        TextParagraphV2 current = first;

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var currentElement = chiltIterator.next();
            if (currentElement == position) {
                if (separator != null) {
                    current.children.add(separator);
                    separator.setParentStructure(current);
                }
                current = second;
            }
            current.children.add(currentElement);
            currentElement.setParentStructure(current);
        }

        return List.of(first, second);
    }

    private TextAddResponse addElementBefore(TextElementV2 position, TextElementV2 element) {
        var iterator = this.children.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setParentStructure(this);
                break;
            }
        }
        return null;
        //TODO
        // return new TextAddResponse(position.getTextPosition());
    }

    @Override
    public void notifyChangeDown() {
        setRepresentation(null);
    }

    private boolean isSplitNeeded(TextElementV2 addedElement) {
        return addedElement.isEndOfLine();
    }

    @Override
    public void clear() {
        this.children.clear();
        setRepresentation(null);
    }

    public void setToEol(ResourceManager storage) {
        if (!this.children.isEmpty() && getLastChild().isEndOfLine()) {
            return;
        }

        //TODO- don't use static class
        // if (!children.isEmpty()) {
        //     add(TextElementFactory.genNewLineChar(//
        //             null, //
        //             storage, //
        //             null, //
        //             children.getLast().getFormat(), //
        //             children.getLast().getFormatEffect() //
        //     ));
        // } else {
        //     add(TextElementFactory.genNewLineChar(//
        //             null, //
        //             storage, //
        //             null, //
        //             TextFormat.DEFAULT, //
        //             TextFormatEffect.DEFAULT_FORMAT_EFFECT //
        //     ));
        // }
    }

    public void setToEos(ResourceManager storage) {
        if (!this.children.isEmpty() && getLastChild().isEndOfSection()) {
            return;
        }

        if (!this.children.isEmpty() && getLastChild().isEndOfLine()) {
            children.removeLast();
        }

        //TODO
        // if (!children.isEmpty()) {
        //     add(TextElementFactory.genNewSectionChar(//
        //             null, //
        //             storage, //
        //             null, //
        //             children.getLast().getFormat(), //
        //             children.getLast().getFormatEffect() //
        //     ));
        // } else {
        //     add(TextElementFactory.genNewSectionChar(//
        //             null, //
        //             storage, //
        //             null, //
        //             TextFormat.DEFAULT, //
        //             TextFormatEffect.DEFAULT_FORMAT_EFFECT //
        //     ));
        // }
    }

    public boolean isEndOfSection() {
        return !this.children.isEmpty() && this.children.getLast().isEndOfSection();
    }

    public TextElementV2 removeEndOfSection() {
        if (isEndOfSection()) {
            //TODO
            // return this.children.pollLast();
        }
        throw new NullPointerException("Paragraph has no EndOfSection");
    }
}
