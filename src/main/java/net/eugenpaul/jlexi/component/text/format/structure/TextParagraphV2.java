package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElementFactoryV2;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
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

    public TextParagraphV2(TextStructureV2 parentStructure, TextParagraphConfigurationV2 config,
            ResourceManager storage) {
        super(parentStructure);

        this.config = config;
        this.storage = storage;
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (null == getRepresentation()) {
            var words = this.config.getTextToWordCompositor().compose(this.children.listIterator(), this.storage);
            setRepresentation(this.config.getTextToRowsCompositor().compose(words, size));
        }
        return getRepresentation();
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return element instanceof TextParagraphV2;
    }

    @Override
    protected TextStructureOfStructureV2 copyStructure() {
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

        // TODO- don't use static class
        if (!children.isEmpty() && children.getLast() instanceof TextElementV2) {
            var lastElement = (TextElementV2) children.getLast();
            add(TextElementFactoryV2.genNewLineChar(//
                    storage, //
                    null, //
                    lastElement.getFormat(), //
                    lastElement.getFormatEffect() //
            ));
        } else {
            add(TextElementFactoryV2.genNewLineChar(//
                    storage, //
                    null, //
                    TextFormat.DEFAULT, //
                    TextFormatEffect.DEFAULT_FORMAT_EFFECT //
            ));
        }
    }

    public void setToEos(ResourceManager storage) {
        if (!this.children.isEmpty() && getLastChild().isEndOfSection()) {
            return;
        }

        if (!this.children.isEmpty() && getLastChild().isEndOfLine()) {
            children.removeLast();
        }

        // TODO- don't use static class
        if (!children.isEmpty() && children.getLast() instanceof TextElementV2) {
            var lastElement = (TextElementV2) children.getLast();
            add(TextElementFactoryV2.genNewSectionChar(//
                    storage, //
                    null, //
                    lastElement.getFormat(), //
                    lastElement.getFormatEffect() //
            ));
        } else {
            add(TextElementFactoryV2.genNewSectionChar(//
                    storage, //
                    null, //
                    TextFormat.DEFAULT, //
                    TextFormatEffect.DEFAULT_FORMAT_EFFECT //
            ));
        }
    }

    public TextElementV2 removeEndOfSection() {
        if (isEndOfSection()) {
            // TODO
            // return this.children.pollLast();
        }
        throw new NullPointerException("Paragraph has no EndOfSection");
    }

    @Override
    protected boolean isComplete() {
        return isEndOfLine();
    }
}
