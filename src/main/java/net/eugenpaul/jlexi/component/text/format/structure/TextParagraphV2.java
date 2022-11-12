package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraphV2 extends TextStructureOfStructureV2 {

    private TextParagraphConfigurationV2 config;

    private ResourceManager storage;

    public TextParagraphV2(TextStructureV2 parentStructure, ResourceManager storage) {
        this(//
                parentStructure, //
                TextParagraphConfigurationV2.builder().build(), //
                storage //
        );
    }

    public TextParagraphV2(TextStructureV2 parentStructure, TextParagraphConfigurationV2 config, ResourceManager storage) {
        super(parentStructure);

        this.config = config;
        this.storage = storage;
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (null == getRepresentation()) {
            var words = this.config.getTextToWordCompositor().compose(this.children.iterator(), this.storage);
            setRepresentation(this.config.getTextToRowsCompositor().compose(words, size));
        }
        return getRepresentation();
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return element instanceof TextParagraphV2;
    }

    @Override
    protected TextStructureOfStructureV2 createMergedStructute() {
        return new TextParagraphV2(getParentStructure(), this.config, this.storage);
    }

    public void add(TextStructureV2 element) {
        this.children.add(element);
        element.setParentStructure(this);
    }

    @Override
    protected boolean isNeedToSplit(TextStructureV2 newChild) {
        return newChild.isEndOfLine();
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

    public TextElementV2 removeEndOfSection() {
        if (isEndOfSection()) {
            //TODO
            // return this.children.pollLast();
        }
        throw new NullPointerException("Paragraph has no EndOfSection");
    }
}
