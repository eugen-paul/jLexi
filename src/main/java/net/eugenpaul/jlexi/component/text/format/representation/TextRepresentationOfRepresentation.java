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
        return this.children.isEmpty();
    }

    @Override
    public TextPosition getFirstChild() {
        return this.children.getFirst().getFirstChild();
    }

    @Override
    public TextPosition getLastChild() {
        return this.children.getLast().getLastChild();
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
        var iterator = this.children.iterator();
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
        var iterator = this.children.iterator();
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
    protected TextPosition moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        TextRepresentation target;
        switch (moving) {
        case UP, PREVIOUS:
            target = this.children.getLast();
            break;
        case DOWN, NEXT:
            target = this.children.getFirst();
            break;
        default:
            target = null;
            break;
        }

        if (target == null) {
            return null;
        }

        return target.moveIn(moving, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveUp(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        var next = getPreviousRepresentation(fromChild);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.moveUp(this, fieldType, xOffset);
            }
            return null;
        }

        return next.moveIn(MovePosition.UP, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveDown(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        var next = getNextRepresentation(fromChild);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.moveDown(this, fieldType, xOffset);
            }
            return null;
        }

        return next.moveIn(MovePosition.DOWN, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveNext(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        var next = getNextRepresentation(fromChild);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.moveNext(this, fieldType, xOffset);
            }
            return null;
        }

        var pos = next.getFirstChild();
        if (pos != null) {
            return pos.getTextElement().getTextPosition();
        }
        return null;
    }

    @Override
    protected TextPosition movePrevious(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        var next = getPreviousRepresentation(fromChild);

        if (next == null) {
            if (getParent() instanceof TextRepresentation) {
                var parentRepresentation = (TextRepresentation) getParent();
                return parentRepresentation.movePrevious(this, fieldType, xOffset);
            }
            return null;
        }

        var pos = next.getLastChild();
        if (pos != null) {
            return pos.getTextElement().getTextPosition();
        }
        return null;
    }

    @Override
    protected TextPosition moveFirst(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        // TODO
        return getFirstChild();
    }

    @Override
    protected TextPosition moveLast(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        // TODO
        return getLastChild();
    }

    @Override
    public boolean isChild(TextRepresentation representation) {
        return children.contains(representation);
    }

}
