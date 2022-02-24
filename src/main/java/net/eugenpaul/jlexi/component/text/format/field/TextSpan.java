package net.eugenpaul.jlexi.component.text.format.field;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public class TextSpan extends TextField {

    private LinkedList<TextElement> children;

    protected TextSpan(TextStructure structureParent, FormatAttribute format) {
        super(structureParent, format);
        children = new LinkedList<>();
    }

    @Override
    public Iterator<TextElement> printableIterator() {
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
    public void notifyChange() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FormatAttribute mergeFormat(FormatAttribute format) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addBefor(TextElement position, TextElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> List<T> split() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void merge(TextField other) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createCopy(TextField fromTextElement) {
        // TODO Auto-generated method stub

    }

    @Override
    public TextElement getNext(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPrevious(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getUp(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getDown(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getEol(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getBol(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getStart(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getEnd(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPageUp(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPageDown(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

}
