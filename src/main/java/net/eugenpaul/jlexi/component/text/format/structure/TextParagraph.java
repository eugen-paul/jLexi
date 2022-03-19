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
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.resourcesmanager.textformatter.UnderlineType;
import net.eugenpaul.jlexi.utils.Size;

public class TextParagraph extends TextStructure implements GlyphIterable<TextStructureForm> {

    protected TextCompositor<TextElement> compositor;
    private LinkedList<TextElement> textElements;

    public TextParagraph(TextStructure parentStructure, TextFormat format, ResourceManager storage, String text) {
        super(parentStructure, format, storage);
        this.textElements = new LinkedList<>();
        initText(text);
        this.compositor = new TextElementToRowCompositor<>();
    }

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

    /**
     * !!JUST for Test!!<br>
     * test format: {XX:text} XX - format text - printed Text example: "Text can be: {BI:bold and italics}, {B :bold} or
     * { I:italics}."
     * 
     * @param text
     */
    private void initText(String text) {
        Pattern tagRegex = Pattern.compile("([^\\}]*)\\{(.+?)\\}([^\\{]*)");
        final Matcher matcher = tagRegex.matcher(text);
        int counter = 0;
        int underline = 0;
        while (matcher.find()) {
            counter++;
            underline = counter % 3;

            if (!matcher.group(1).isEmpty()) {
                textElements.addAll(stringToChars(matcher.group(1), format, TextFormatEffect.DEFAULT_FORMAT_EFFECT));
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
                        .build();

                var ef = TextFormatEffect.builder()//
                        .underline(uType)//
                        .build();

                textElements.addAll(stringToChars(matcher.group(2).substring(3), f, ef));
            }

            if (!matcher.group(3).isEmpty()) {
                textElements.addAll(stringToChars(matcher.group(3), format, TextFormatEffect.DEFAULT_FORMAT_EFFECT));
            }
        }
        if (counter == 0 && !text.isEmpty()) {
            textElements.addAll(stringToChars(text, format, TextFormatEffect.DEFAULT_FORMAT_EFFECT));
        }
        textElements.add(
                TextElementFactory.genNewLineChar(null, storage, this, format, TextFormatEffect.DEFAULT_FORMAT_EFFECT));
    }

    private LinkedList<TextElement> stringToChars(String data, TextFormat format, TextFormatEffect formatEffect) {
        return data.chars() //
                .mapToObj(v -> (char) v) //
                .map(v -> TextElementFactory.fromChar(null, storage, this, v, format, formatEffect)) //
                .filter(Objects::nonNull)//
                .collect(Collectors.toCollection(LinkedList::new));
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

    protected void add(TextElement element) {
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
