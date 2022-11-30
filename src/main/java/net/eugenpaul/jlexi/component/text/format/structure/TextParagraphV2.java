package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
    protected boolean canContainChild(TextStructureV2 element) {
        return element instanceof TextElementV2;
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

    @Override
    protected void setComplete() {
        setToEol(storage);
    }

    @Override
    protected TextAddResponseV2 doInsertBefore(TextStructureV2 position, List<TextStructureV2> data) {
        if (getParentStructure() == null) {
            return TextAddResponseV2.EMPTY;
        }

        if (!data.stream().allMatch(this::canContainChild)) {
            return TextAddResponseV2.EMPTY;
        }

        var pathToPosition = getChildWithElement(position);
        if (pathToPosition != position) {
            return TextAddResponseV2.EMPTY;
        }

        List<TextStructureV2> newParagraphs = new LinkedList<>();

        var selfCopy = copyStructure();
        newParagraphs.add(selfCopy);

        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            var currentChild = childIterator.next();
            if (currentChild == position) {
                var dataIterator = data.iterator();
                while (dataIterator.hasNext()) {
                    var currentData = dataIterator.next();
                    selfCopy.children.add(currentData);
                    if (currentData.isEndOfLine()) {
                        selfCopy = copyStructure();
                        newParagraphs.add(selfCopy);
                    }
                }
            }
            selfCopy.children.add(currentChild);
        }

        return new TextAddResponseV2(//
                getParentStructure(), //
                position, //
                newParagraphs //
        );
    }

    protected TextAddResponseV2 doInsertBefore(TextStructureV2 position, ListIterator<TextStructureV2> dataIterator) {
        if (getParentStructure() == null) {
            return TextAddResponseV2.EMPTY;
        }

        var pathToPosition = getChildWithElement(position);
        if (pathToPosition != position) {
            return TextAddResponseV2.EMPTY;
        }

        List<TextStructureV2> newParagraphs = new LinkedList<>();

        var selfCopy = copyStructure();
        newParagraphs.add(selfCopy);

        var childIterator = childListIterator();
        while (childIterator.hasNext()) {
            var currentChild = childIterator.next();
            if (currentChild == position) {
                while (dataIterator.hasNext()) {
                    var currentData = dataIterator.next();
                    if (!canContainChild(currentData)) {
                        return TextAddResponseV2.EMPTY;
                    }
                    selfCopy.children.add(currentData);
                    if (currentData.isEndOfLine()) {
                        selfCopy = copyStructure();
                        newParagraphs.add(selfCopy);
                    }
                }
            }
            selfCopy.children.add(currentChild);
        }

        return new TextAddResponseV2(//
                getParentStructure(), //
                position, //
                newParagraphs //
        );
    }

    @Override
    protected TextSplitResponse doSplit(TextStructureV2 position) {
        if (getChildWithElement(position) != position) {
            return TextSplitResponse.EMPTY;
        }

        var first = copyStructure();
        var childIterator = childListIterator();

        while (childIterator.hasNext()) {
            var child = childIterator.next();
            if (child == position) {
                var lastElement = (TextElementV2) children.getLast();
                first.children.add(//
                        TextElementFactoryV2.genNewLineChar(//
                                storage, //
                                null, //
                                lastElement.getFormat(), //
                                lastElement.getFormatEffect() //
                        ));

                var last = copyStructure();
                last.children.add(child);
                childIterator.forEachRemaining(last.children::add);
                return new TextSplitResponse(first, last);
            }
            first.children.add(child);
        }

        return TextSplitResponse.EMPTY;
    }

    @Override
    protected TextMergeResponseV2 doMerge(TextStructureV2 next) {
        var self = copyStructure();

        var childIterator = childListIterator();
        childIterator.forEachRemaining(self.children::add);

        LinkedList<TextStructureV2> newStructures = new LinkedList<>();

        if (checkMergeWith(next)) {
            if (self.isEndOfLine()) {
                self.removeEoL();
            }

            var nextChildIterator = childListIterator();
            nextChildIterator.forEachRemaining(self.children::add);

            newStructures.add(self);
        } else {
            newStructures.add(self);
            newStructures.add(next);
        }

        var last = newStructures.getLast().getLastChild();
        if (!(last instanceof TextElementV2)) {
            return new TextMergeResponseV2(//
                    null, //
                    List.of(self) //
            );
        }

        var lastElement = (TextElementV2) last;

        return new TextMergeResponseV2(//
                lastElement.getTextPosition(), //
                List.of(self) //
        );
    }

    @Override
    protected void removeEoS() {
        if (isEndOfSection()) {
            children.removeLast();
        }
    }

}
