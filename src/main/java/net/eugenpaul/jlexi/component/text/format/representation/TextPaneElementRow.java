package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPaneElementRow extends TextRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneElementRow.class);

    private LinkedList<TextElement> children;
    private TreeMap<Integer, TextElement> xPositionToRow;

    public TextPaneElementRow(Glyph parent) {
        super(parent);
        this.xPositionToRow = new TreeMap<>();
        this.children = new LinkedList<>();
    }

    public void add(TextElement child) {
        children.add(child);
        Size currentSize = getSize();
        setSize(new Size(//
                currentSize.getWidth() + child.getSize().getWidth(), //
                Math.max(currentSize.getHeight(), child.getSize().getHeight())//
        ));
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = xPositionToRow.floorEntry(pos.getX());
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
    public Drawable getPixels() {
        int[] pixels = new int[getSize().getWidth() * getSize().getHeight()];

        cachedDrawable = new DrawableImpl(pixels, getSize());

        xPositionToRow.clear();

        Vector2d position = new Vector2d(0, 0);
        for (var el : children) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);

            var xPosition = position.getX();

            xPositionToRow.put(xPosition, el);

            el.setRelativPosition(new Vector2d(xPosition, 0));

            position.setX(xPosition + el.getSize().getWidth());
        }

        return cachedDrawable;
    }

    @Override
    public DrawableV2 getDrawable() {
        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE);
        xPositionToRow.clear();

        int maxHeight = 0;
        for (TextElement textElement : children) {
            maxHeight = Math.max(maxHeight, textElement.getSize().getHeight());
        }

        int currentX = 0;
        for (var el : children) {
            int currentY = maxHeight - el.getSize().getHeight();

            cachedDrawableV2.addDrawable(el.getDrawable(), currentX, currentY);

            xPositionToRow.put(currentX, el);

            el.setRelativPosition(new Vector2d(currentX, currentY));

            currentX += el.getSize().getWidth();
        }

        return cachedDrawableV2.draw();
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

}
