package net.eugenpaul.jlexi.component.text.format.field;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;

public class TextSpan extends TextField {

    private LinkedList<TextElement> children;

    public TextSpan(TextStructure structureParent, FormatAttribute format) {
        super(structureParent, format);
        children = new LinkedList<>();
    }

    public TextSpan(TextStructure structureParent, FontStorage fontStorage, FormatAttribute format, String text) {
        super(structureParent, format);
        children = text.chars() //
                .mapToObj(v -> (char) v) //
                .map(v -> new TextChar(null, fontStorage, this, v)) //
                .collect(Collectors.toCollection(LinkedList::new));
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
    public void remove() {
        children.stream().forEach(TextElement::remove);
    }

    @Override
    public void addBefor(TextElement position, TextElement element) {
        ListIterator<TextElement> iterator = children.listIterator();
        while (iterator.hasNext()) {
            TextElement iteratorPosition = iterator.next();
            if (iteratorPosition == position) {
                iterator.add(element);
                break;
            }
        }
    }

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

}
