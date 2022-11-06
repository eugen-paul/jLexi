package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextRepresentationOfRepresentationV2 extends TextRepresentationV2 {

    protected LinkedList<TextRepresentationV2> children;

    protected TextRepresentationOfRepresentationV2(Glyph parent) {
        super(parent);
        this.children = new LinkedList<>();
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public TextPositionV2 getFirstChild() {
        return this.children.getFirst().getFirstChild();
    }

    @Override
    public TextPositionV2 getLastChild() {
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
    public Iterator<TextRepresentationV2> drawableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextRepresentationV2 getNextRepresentation(TextRepresentationV2 representation) {
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
    public TextRepresentationV2 getPreviousRepresentation(TextRepresentationV2 representation) {
        var iterator = this.children.iterator();
        TextRepresentationV2 lastElement = null;
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
    protected TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        if (!checkMove(fieldType, getFieldType())) {
            return null;
        }

        Iterator<TextRepresentationV2> iterator = null;
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
    protected TextPositionV2 moveUp(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentationV2 next = fromChild;
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

        if (getParent() instanceof TextRepresentationV2) {
            var parentRepresentation = (TextRepresentationV2) getParent();
            return parentRepresentation.moveUp(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPositionV2 moveDown(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentationV2 next = fromChild;
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

        if (getParent() instanceof TextRepresentationV2) {
            var parentRepresentation = (TextRepresentationV2) getParent();
            return parentRepresentation.moveDown(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPositionV2 moveNext(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentationV2 next = fromChild;
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

        if (getParent() instanceof TextRepresentationV2) {
            var parentRepresentation = (TextRepresentationV2) getParent();
            return parentRepresentation.moveNext(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPositionV2 movePrevious(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        if (fieldType.isLocked()) {
            return null;
        }

        if (fieldType == TextFieldType.UNKNOWN) {
            fieldType = getFieldType();
        }

        TextRepresentationV2 next = fromChild;
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

        if (getParent() instanceof TextRepresentationV2) {
            var parentRepresentation = (TextRepresentationV2) getParent();
            return parentRepresentation.movePrevious(this, fieldType, xOffset);
        }
        return null;
    }

    @Override
    protected TextPositionV2 moveFirst(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        // TODO
        return getFirstChild();
    }

    @Override
    protected TextPositionV2 moveLast(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        // TODO
        return getLastChild();
    }

    @Override
    public boolean isChild(TextRepresentationV2 representation) {
        return children.contains(representation);
    }

}
