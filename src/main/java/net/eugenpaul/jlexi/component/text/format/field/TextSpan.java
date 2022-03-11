package net.eugenpaul.jlexi.component.text.format.field;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextSpan extends TextField {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(TextSpan.class);

    private LinkedList<TextElement> children;
    private LinkedList<TextField> splits;
    private ResourceManager storage;

    public TextSpan(TextParagraph structureParent, FormatAttribute format, ResourceManager storage) {
        this(structureParent, format, storage, new LinkedList<>());
    }

    public TextSpan(TextParagraph structureParent, FormatAttribute format, ResourceManager storage, String text) {
        this(structureParent, format, storage, (LinkedList<TextElement>) null);

        this.children = text.chars() //
                .mapToObj(v -> (char) v) //
                .map(v -> TextElementFactory.fromChar(null, storage, this, v)) //
                .filter(Objects::nonNull)//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    protected TextSpan(TextParagraph structureParent, FormatAttribute format, ResourceManager storage,
            LinkedList<TextElement> data) {
        super(structureParent, format);
        this.storage = storage;
        this.children = data;
        this.splits = new LinkedList<>();
    }

    @Override
    public Iterator<TextElement> printableChildIterator() {
        return children.iterator();
    }

    @Override
    public TextElement getFirstChild() {
        return children.getFirst();
    }

    @Override
    public TextElement getLastChild() {
        return children.getLast();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public void addBefore(TextElement position, TextElement element) {
        if (element.isEndOfLine()) {
            split(position);
        } else {
            add(position, element);
        }
    }

    private void add(TextElement position, TextElement element) {
        ListIterator<TextElement> iterator = children.listIterator();
        while (iterator.hasNext()) {
            TextElement iteratorPosition = iterator.next();
            if (iteratorPosition == position) {
                iterator.previous();
                iterator.add(element);
                break;
            }
        }
    }

    private void split(TextElement position) {
        ListIterator<TextElement> iterator = children.listIterator();
        LinkedList<TextElement> splitter = new LinkedList<>();

        clearSplitter();

        boolean found = false;
        var newSpan = new TextSpan(getStructureParent(), //
                new FormatAttribute(getFormat()), //
                storage, //
                splitter);

        while (iterator.hasNext()) {
            TextElement iteratorPosition = iterator.next();
            if (iteratorPosition == position) {
                found = true;
            }
            if (found) {
                splitter.add(iteratorPosition);
                iteratorPosition.setParentTextField(newSpan);
                iterator.remove();
            }
        }

        children.add(TextElementFactory.genNewLineChar(null, storage, this));
        splits.add(newSpan);
        edit = true;
    }

    @Override
    public TextElement remove(TextElement element) {
        if (element.isEndOfLine()) {
            return mergeWithNext(element);
        } else {
            var response = removeElement(element);
            if (response == null) {
                response = getStructureParent().getNext(this, true).getFirstChild();
            }
            return response;
        }
    }

    private TextElement mergeWithNext(TextElement element) {
        if (getStructureParent() == null) {
            return element;
        }

        var nextElement = getStructureParent().getNext(this, true);
        if (nextElement != null) {
            var response = nextElement.getFirstChild();
            if (response != null) {
                children.removeLast();
                edit = true;
                return response;
            }
        }

        var nextParagraph = getStructureParent().getNextParagraph();
        if (nextParagraph == null) {
            return element;
        }

        nextElement = nextParagraph.getFirst();
        if (nextElement != null) {
            var response = nextElement.getFirstChild();
            if (response != null) {
                children.removeLast();
                edit = true;
                return response;
            }
        }

        return element;
    }

    private TextElement removeElement(TextElement element) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == element) {
                iterator.remove();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
        }
        return null;
    }

    // @Override
    // public TextElement removeBefore(TextElement element) {
    // var iterator = children.listIterator();
    // while (iterator.hasNext()) {
    // if (iterator.next() == element) {
    // iterator.previous();
    // if (iterator.hasPrevious()) {
    // iterator.previous();
    // iterator.remove();
    // break;
    // }
    // // TODO do merge with previous
    // LOGGER.trace("Merge TextSpan with previous element.");
    // var previous = getStructureParent().getPrevious(this);
    // if (previous != null) {
    // previous.remove(previous.getLastChild());
    // }
    // break;
    // }
    // }
    // return element;
    // }

    @Override
    public void reset() {
        children.stream().forEach(TextElement::reset);
    }

    @Override
    public void merge(TextField other) {
        Iterator<TextElement> iterator = other.printableChildIterator();
        while (iterator.hasNext()) {
            children.add(iterator.next());
        }
        reset();
    }

    @Override
    public void createCopy(TextField fromTextElement) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<TextField> getSplits() {
        return splits;
    }

    @Override
    public void clearSplitter() {
        splits.clear();
    }

}
