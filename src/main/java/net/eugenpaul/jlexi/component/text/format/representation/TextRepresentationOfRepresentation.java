package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
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
    public TextPosition move(TextPosition position, MovePosition moving) {
        switch (moving) {
        case UP:
            return getUp(position);
        case DOWN:
            return getDown(position);
        case PREVIOUS:
            return getPrevious(position);
        case NEXT:
            return getNext(position);
        default:
            throw new NotYetImplementedException("Moving " + moving + " is not implemented.");
        }
    }

    private TextPosition getNext(TextPosition position) {
        var childRepresentation = getChildRepresentation(position);
        if (childRepresentation == null) {
            return null;
        }

        var next = getNextRepresentation(childRepresentation);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.move(position, MovePosition.NEXT);
            }
            return null;
        }

        var pos = next.getFirstChild();
        if (pos != null) {
            return pos.getTextElement().getTextPosition();
        }
        return null;
    }

    private TextPosition getPrevious(TextPosition position) {
        var childGlyph = position.getTextElement().getChild(this);
        if (!(childGlyph instanceof TextRepresentation)) {
            return null;
        }

        var childRepresentation = (TextRepresentation) childGlyph;
        var previous = getPreviousRepresentation(childRepresentation);

        if (previous == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.move(position, MovePosition.PREVIOUS);
            }
            return null;
        }

        var pos = previous.getLastChild();
        if (pos != null) {
            return pos.getTextElement().getTextPosition();
        }
        return null;
    }

    private TextPosition getUp(TextPosition position) {
        var childRepresentation = getChildRepresentation(position);
        if (childRepresentation == null) {
            return null;
        }

        var next = getPreviousRepresentation(childRepresentation);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.move(position, MovePosition.UP);
            }
            return null;
        }

        var pos = position.getTextElement().getRelativPositionTo(this);
        return next.getLastText(pos.getX());
    }

    private TextPosition getDown(TextPosition position) {
        var childRepresentation = getChildRepresentation(position);
        if (childRepresentation == null) {
            return null;
        }

        var next = getNextRepresentation(childRepresentation);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.move(position, MovePosition.DOWN);
            }
            return null;
        }

        var pos = position.getTextElement().getRelativPositionTo(this);
        return next.getFirstText(pos.getX());
    }
}
