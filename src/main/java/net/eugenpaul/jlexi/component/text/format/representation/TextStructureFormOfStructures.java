package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextStructureFormOfStructures extends TextStructureForm {

    protected LinkedList<TextStructureForm> children;

    protected TextStructureFormOfStructures(Glyph parent) {
        super(parent);
        this.children = new LinkedList<>();
    }

    public void add(TextStructureForm child) {
        children.add(child);
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public void notifyUpdate(TextElement element, Drawable draw, Area area) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyResize(TextElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public TextElement getFirstChild() {
        return children.getFirst().getFirstChild();
    }

    @Override
    public TextElement getLastChild() {
        return children.getLast().getLastChild();
    }

    @Override
    public Iterator<Glyph> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<TextStructureForm> drawableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextStructureForm getNextStructureForm(TextStructureForm structure) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == structure) {
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public TextStructureForm getPreviousStructureForm(TextStructureForm structure) {
        var iterator = children.iterator();
        TextStructureForm lastElement = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == structure) {
                return lastElement;
            }
            lastElement = currentElement;
        }
        return null;
    }
}
