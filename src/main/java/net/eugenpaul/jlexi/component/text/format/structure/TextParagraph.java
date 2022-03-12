package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextElementToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.textformatter.UnderlineType;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextStructureForm> {

    protected TextCompositor<TextElement> fieldCompositor;
    private LinkedList<TextElement> children;

    public TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage, String text) {
        super(parentStructure, format, storage);
        this.children = new LinkedList<>();
        initFields(text);
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    protected TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage,
            LinkedList<TextElement> children) {
        super(parentStructure, format, storage);
        this.children = children;
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    /**
     * !!JUST for Test!!<br>
     * test format: {XX:text} XX - format text - printed Text example: "Text can be: {BI:bold and italics}, {B :bold} or
     * { I:italics}."
     * 
     * @param text
     */
    private void initFields(String text) {
        Pattern tagRegex = Pattern.compile("([^\\}]*)\\{(.+?)\\}([^\\{]*)");
        final Matcher matcher = tagRegex.matcher(text);
        int counter = 0;
        int underline = 0;
        while (matcher.find()) {
            counter++;
            underline = counter % 3;

            if (!matcher.group(1).isEmpty()) {
                children.addAll(stringToChars(matcher.group(1), format));
            }

            if (!matcher.group(2).isEmpty()) {
                UnderlineType uType = UnderlineType.NONE;
                switch (underline) {
                case 1:
                    uType = UnderlineType.SINGLE;
                    break;
                case 2:
                    uType = UnderlineType.DOUBLE;
                    break;
                default:
                    break;
                }

                var f = TextFormat.builder()//
                        .fontName(format.getFontName())//
                        .fontsize(format.getFontsize())//
                        .bold(matcher.group(2).charAt(0) == 'B')//
                        .italic(matcher.group(2).charAt(1) == 'I')//
                        .underline(uType)//
                        .build();
                children.addAll(stringToChars(matcher.group(2).substring(3), f));
            }

            if (!matcher.group(3).isEmpty()) {
                children.addAll(stringToChars(matcher.group(3), format));
            }
        }
        if (counter == 0 && !text.isEmpty()) {
            children.addAll(stringToChars(text, format));
        }
        children.add(TextElementFactory.genNewLineChar(null, storage, this, format));
    }

    private LinkedList<TextElement> stringToChars(String data, TextFormat format) {
        return data.chars() //
                .mapToObj(v -> (char) v) //
                .map(v -> TextElementFactory.fromChar(null, storage, this, v, format)) //
                .filter(Objects::nonNull)//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
        this.children = new LinkedList<>();
        this.fieldCompositor = new TextElementToRowCompositor<>();
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {
        // TODO Auto-generated method stub
    }

    @Override
    public Iterator<TextStructureForm> printableChildIterator() {
        if (null == structureForm) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(structureForm);
    }

    @Override
    public List<TextStructureForm> getRows(Size size) {
        if (null == structureForm) {
            Iterator<TextElement> elIterator = getCompositorIterator();
            structureForm = fieldCompositor.compose(elIterator, size);
        }
        return structureForm;
    }

    private Iterator<TextElement> getCompositorIterator() {
        return children.iterator();
    }

    @Override
    public void resetStructure() {
        structureForm = null;
    }

    @Override
    protected void restructureChildren() {
        var iterator = children.listIterator();
        boolean doSplit = false;
        while (iterator.hasNext()) {
            var field = iterator.next();
            if (!field.isEndOfLine()) {
                doSplit = true;
                break;
            }
        }
        if (doSplit) {
            split();
        }

        if (children.isEmpty() //
                || !children.getLast().isEndOfLine() //
        ) {
            // Last element is not "endOfLine" => merge with this paragraph with the next paragraph.
            mergeWithNext();
        }
    }

    private void mergeWithNext() {
        if (parentStructure == null) {
            return;
        }

        var nextElement = parentStructure.getNextStructure(this);
        if (!(nextElement instanceof TextParagraph)) {
            return;
        }

        var nextParagraph = (TextParagraph) nextElement;
        nextParagraph.children.stream().forEach(v -> v.setStructureParent(this));
        children.addAll(nextParagraph.children);
        parentStructure.remove(nextParagraph);
    }

    private void split() {
        var iterator = children.listIterator();
        var splitter = new LinkedList<TextElement>();

        clearSplitter();

        boolean found = false;
        var newParagraph = new TextParagraph(parentStructure, format, storage, splitter);

        while (iterator.hasNext()) {
            var iteratorPosition = iterator.next();
            if (found) {
                splitter.add(iteratorPosition);
                iteratorPosition.setStructureParent(newParagraph);
                iterator.remove();
            }
            if (iteratorPosition.isEndOfLine()) {
                found = true;
                if (!splitter.isEmpty()) {
                    splits.add(newParagraph);
                    splitter = new LinkedList<>();
                }
            }
        }

        if (!splitter.isEmpty()) {
            splits.add(newParagraph);
        }
    }

    @Override
    public TextElement removeElement(TextElement element) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == element) {
                iterator.remove();
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    var nextParagraph = getNextParagraph();
                    if (nextParagraph == null) {
                        children.add(element);
                        return element;
                    } else {
                        return nextParagraph.getFirst();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void addBefore(TextElement position, TextElement element) {
        var iterator = children.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position) {
                iterator.previous();
                iterator.add(element);
                element.setStructureParent(this);
                break;
            }
        }
    }

    @Override
    protected ListIterator<TextStructure> childIterator() {
        return Collections.emptyListIterator();
    }

    public TextElement getFirst() {
        return children.getFirst();
    }

    public TextParagraph getNextParagraph() {
        if (parentStructure == null) {
            return null;
        }

        var nextParagraph = parentStructure.getNextStructure(this);
        if (!(nextParagraph instanceof TextParagraph)) {
            return null;
        }
        return (TextParagraph) nextParagraph;
    }

    public TextElement getNext(TextElement element) {
        var iterator = children.iterator();
        var found = false;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                found = true;
                continue;
            }
            if (found) {
                return currentElement;
            }
        }
        return null;
    }

    public TextElement getPrevious(TextElement element) {
        var iterator = children.listIterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == element) {
                iterator.previous();
                if (iterator.hasPrevious()) {
                    return iterator.previous();
                }
                break;
            }
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

}
