package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Collections;
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
        if (!checkMove(fieldType, getFieldType())) {
            return null;
        }

        Iterator<TextRepresentation> iterator = null;
        switch (moving) {
        case UP, PREVIOUS:
            iterator = this.children.descendingIterator();
            break;
        case DOWN, NEXT:
            iterator = this.children.iterator();
            break;
        default:
            iterator = Collections.emptyIterator();
            break;
        }

        while (iterator.hasNext()) {
            var child = iterator.next();
            var responsePosition = child.moveIn(moving, fieldType, xOffset);
            if (responsePosition != null) {
                return responsePosition;
            }
        }

        return null;
    }

    @Override
    protected TextPosition moveUp(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentation next = fromChild;
        while (next != null) {
            next = getPreviousRepresentation(next);
            if (next == null) {
                break;
            }

            var responsePosition = next.moveIn(MovePosition.UP, fieldType, xOffset);
            if (responsePosition != null) {
                return responsePosition;
            }
        }

        if (getParent() instanceof TextRepresentation) {
            var parentRepresentation = (TextRepresentation) getParent();
            return parentRepresentation.moveUp(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPosition moveDown(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentation next = fromChild;
        while (next != null) {
            next = getNextRepresentation(next);
            if (next == null) {
                break;
            }

            var responsePosition = next.moveIn(MovePosition.DOWN, fieldType, xOffset);
            if (responsePosition != null) {
                return responsePosition;
            }
        }

        if (getParent() instanceof TextRepresentation) {
            var parentRepresentation = (TextRepresentation) getParent();
            return parentRepresentation.moveDown(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPosition moveNext(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentation next = fromChild;
        while (next != null) {
            next = getNextRepresentation(next);
            if (next == null) {
                break;
            }

            var responsePosition = next.moveIn(MovePosition.NEXT, fieldType, xOffset);
            if (responsePosition != null) {
                return responsePosition;
            }
        }

        if (getParent() instanceof TextRepresentation) {
            var parentRepresentation = (TextRepresentation) getParent();
            return parentRepresentation.moveNext(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPosition movePrevious(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentation next = fromChild;
        while (next != null) {
            next = getPreviousRepresentation(next);
            if (next == null) {
                break;
            }

            var responsePosition = next.moveIn(MovePosition.PREVIOUS, fieldType, xOffset);
            if (responsePosition != null) {
                return responsePosition;
            }
        }

        if (getParent() instanceof TextRepresentation) {
            var parentRepresentation = (TextRepresentation) getParent();
            return parentRepresentation.movePrevious(this, fieldType, xOffset);
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
