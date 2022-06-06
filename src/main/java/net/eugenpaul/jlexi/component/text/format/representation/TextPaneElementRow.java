package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.var;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPaneElementRow extends TextRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneElementRow.class);

    private LinkedList<TextElement> children;
    private TreeMap<Integer, TextElement> xPositionToRow;

    public TextPaneElementRow(Glyph parent, List<TextElement> children) {
        super(parent);
        this.xPositionToRow = new TreeMap<>();
        this.children = new LinkedList<>(children);

        Size currentSize = Size.ZERO_SIZE;
        for (var child : this.children) {
            child.setParent(this);
            setSize(new Size(//
                    currentSize.getWidth() + child.getSize().getWidth(), //
                    Math.max(currentSize.getHeight(), child.getSize().getHeight())//
            ));
            currentSize = getSize();
            this.xPositionToRow.put(child.getRelativPosition().getX(), child);
        }
    }

    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.xPositionToRow.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCursorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
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
        return children.getLast().getTextPosition();
    }

    @Override
    public TextPosition getNext(TextPosition position) {
        var iterator = children.iterator();
        while (iterator.hasNext()) {
            var e = iterator.next();
            if (e == position.getTextElement()) {
                if (iterator.hasNext()) {
                    return iterator.next().getTextPosition();
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public TextPosition getPrevious(TextPosition position) {
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
    protected TextPosition getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPosition getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

}
