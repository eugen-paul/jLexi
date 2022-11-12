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
    protected boolean hasToBeSplited(TextStructureV2 newChild) {
        return newChild.isEndOfLine();
    }

    @Override
    public void notifyChangeDown() {
        setRepresentation(null);
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
