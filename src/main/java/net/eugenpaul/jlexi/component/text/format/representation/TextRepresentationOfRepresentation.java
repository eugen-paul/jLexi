package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextRepresentationOfRepresentation extends TextRepresentation {

    protected LinkedList<TextRepresentation> children;

    protected TextRepresentationOfRepresentation(Glyph parent) {
        super(parent);
        this.children = new LinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public TextPosition getFirstChild() {
        return children.getFirst().getFirstChild();
    }

    @Override
    public TextPosition getLastChild() {
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
    public Iterator<TextRepresentation> drawableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextRepresentation getNextRepresentation(TextRepresentation representation) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == representation) {
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
    public TextRepresentation getPreviousRepresentation(TextRepresentation representation) {
        var iterator = children.iterator();
        TextRepresentation lastElement = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == representation) {
                return lastElement;
            }
            lastElement = currentElement;
        }
        return null;
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
        children.forEach(v -> v.setParent(null));
        children.clear();
    }
}
