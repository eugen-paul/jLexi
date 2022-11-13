package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.CursorMoving;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextWordBreak;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

@Slf4j
public class TextPaneElementRow extends TextRepresentation implements CursorMoving {

    private LinkedList<TextElement> children;
    private TreeMap<Integer, TextElement> xPositionToElement;

    public TextPaneElementRow(Glyph parent, List<TextElement> children) {
        super(parent);
        this.xPositionToElement = new TreeMap<>();
        this.children = new LinkedList<>(children);

        Size currentSize = Size.ZERO;
        for (var child : this.children) {
            child.setParent(this);
            setSize(new Size(//
                    currentSize.getWidth() + child.getSize().getWidth(), //
                    Math.max(currentSize.getHeight(), child.getSize().getHeight())//
            ));
            currentSize = getSize();
            this.xPositionToElement.put(child.getRelativPosition().getX(), child);
        }
    }

    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var elementEntry = this.xPositionToElement.floorEntry(pos.getX());
        if (null == elementEntry) {
            return null;
        }

        // If the element is a composite, then we need to go into the element.
        var clickedElement = elementEntry.getValue().getCursorElementAt(//
                new Vector2d(//
                        pos.sub(elementEntry.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Row Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Row Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    public TextPosition getFirstChild() {
        return this.children.getFirst().getTextPosition();
    }

    @Override
    public TextPosition getLastChild() {
        if (this.children.getLast() instanceof TextWordBreak) {
            return this.children.get(this.children.size() - 2).getTextPosition();
        }
        return this.children.getLast().getTextPosition();
    }

    @Override
    public TextPosition move(TextPosition position, MovePosition moving) {
        // TODO Add support for composite elements.
        // TODO Check fieldType of current and response element
        TextPosition responsePosition;
        switch (moving) {
        case NEXT:
            responsePosition = getNext(position);
            break;
        case PREVIOUS:
            responsePosition = getPrevious(position);
            break;
        case BENIG_OF_LINE:
            responsePosition = getFirstChild();
            break;
        case END_OF_LINE:
            responsePosition = getLastChild();
            break;
        default:
            // Set responsePosition to null by other movings (UP, DOWN, ...) to move curser outside.
            responsePosition = null;
            break;
        }

        if (responsePosition == null && getParent() instanceof TextRepresentation) {
            return ((TextRepresentation) getParent()).move(//
                    this, //
                    moving, //
                    getFieldType(), //
                    position.getTextElement().getRelativPositionTo(this).getX() //
            );
        }

        return responsePosition;
    }

    @Override
    protected TextPosition moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        if (!checkMove(fieldType, getFieldType())) {
            return null;
        }

        switch (moving) {
        case UP, DOWN:
            return getCursorElementAt(new Vector2d(xOffset, 0));
        case PREVIOUS:
            return children.getLast().getTextPosition();
        case NEXT:
            return children.getFirst().getTextPosition();
        default:
            break;
        }

        return null;
    }

    @Override
    protected TextPosition moveUp(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.UP, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveDown(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.DOWN, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveNext(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.NEXT, fieldType, xOffset);
    }

    @Override
    protected TextPosition movePrevious(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.PREVIOUS, fieldType, xOffset);
    }

    @Override
    protected TextPosition moveFirst(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return getFirstChild();
    }

    @Override
    protected TextPosition moveLast(TextRepresentation fromChild, TextFieldType fieldType, int xOffset) {
        return getLastChild();
    }

    @Override
    public boolean isChild(TextRepresentation representation) {
        return false;
    }

    private TextPosition getNext(TextPosition position) {
        var iterator = this.children.iterator();
        while (iterator.hasNext()) {
            var e = iterator.next();
            if (e == position.getTextElement()) {
                if (iterator.hasNext()) {
                    var nextEl = iterator.next();
                    if (nextEl instanceof TextWordBreak) {
                        return null;
                    }
                    return nextEl.getTextPosition();
                }
                return null;
            }
        }
        return null;
    }

    private TextPosition getPrevious(TextPosition position) {
        var iterator = this.children.iterator();
        TextElement prevElement = null;
        while (iterator.hasNext()) {
            var currentElement = iterator.next();
            if (currentElement == position.getTextElement()) {
                if (prevElement != null && prevElement.isCursorHoldable()) {
                    // TODO get first element from inside of prevElement.
                    throw new NotYetImplementedException("TODO get first element from inside of prevElement.");
                }
                if (prevElement == null) {
                    return null;
                }
                return prevElement.getTextPosition();
            }
            prevElement = currentElement;
        }
        return null;
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);

        for (var el : this.children) {
            this.cachedDrawable.addDrawable(//
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY()//
            );
        }

        return this.cachedDrawable.draw();
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
    public TextRepresentation getNextRepresentation(TextRepresentation structure) {
        return null;
    }

    @Override
    public TextRepresentation getPreviousRepresentation(TextRepresentation structure) {
        return null;
    }

    @Override
    protected TextPosition getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPosition getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }
}
