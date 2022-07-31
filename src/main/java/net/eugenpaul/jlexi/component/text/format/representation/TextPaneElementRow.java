package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
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
public class TextPaneElementRow extends TextRepresentation {

    private LinkedList<TextElement> children;
    private TreeMap<Integer, TextElement> xPositionToElement;

    public TextPaneElementRow(Glyph parent, List<TextElement> children) {
        super(parent);
        this.xPositionToElement = new TreeMap<>();
        this.children = new LinkedList<>(children);

        Size currentSize = Size.ZERO_SIZE;
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
        TextPosition clickedElement = elementEntry.getValue().getCursorElementAt(//
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
    public TextPositionV2 getCursorElementAtV2(Vector2d pos) {
        var elementEntry = this.xPositionToElement.floorEntry(pos.getX());
        if (null == elementEntry) {
            return null;
        }

        // If the element is a composite, then we need to go into the element.
        var clickedElement = elementEntry.getValue().getCursorElementAtV2(//
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
        return children.getFirst().getTextPosition();
    }

    @Override
    public TextPosition getLastChild() {
        if (children.getLast() instanceof TextWordBreak) {
            return children.get(children.size() - 2).getTextPosition();
        }
        return children.getLast().getTextPosition();
    }

    @Override
    public TextPositionV2 move(TextPositionV2 position, MovePosition moving) {
        // TODO Add support for composite elements.
        TextPositionV2 responsePosition;
        switch (moving) {
        case NEXT:
            responsePosition = getNext(position);
            break;
        case PREVIOUS:
            responsePosition = getPrevious(position);
            break;
        default:
            responsePosition = null;
            break;
        }

        if (responsePosition == null && getParent() instanceof TextRepresentation) {
            return ((TextRepresentation) getParent()).move(position, moving);
        }

        return responsePosition;
    }

    private TextPositionV2 getNext(TextPositionV2 position) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            var e = iterator.next();
            if (e == position.getTextElement()) {
                if (iterator.hasNext()) {
                    var nextEl = iterator.next();
                    if (nextEl instanceof TextWordBreak) {
                        return null;
                    }
                    return nextEl.getTextPositionV2();
                }
                return null;
            }
        }
        return null;
    }

    private TextPositionV2 getPrevious(TextPositionV2 position) {
        var iterator = children.iterator();
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
                return prevElement.getTextPositionV2();
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

        for (var el : children) {
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
    protected TextPositionV2 getFirstTextV2(int x) {
        return getCursorElementAtV2(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 getLastTextV2(int x) {
        return getCursorElementAtV2(new Vector2d(x, 0));
    }

}
