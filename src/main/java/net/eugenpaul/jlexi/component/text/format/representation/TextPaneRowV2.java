package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPaneRowV2 extends TextRepresentationOfRepresentationV2 {

    private TreeMap<Integer, TextRepresentationV2> xPositionToColumn;

    private boolean isTextRow;

    @Getter
    @Setter
    private Color background;

    public TextPaneRowV2(Glyph parent, boolean isTextRow) {
        super(parent);
        this.xPositionToColumn = new TreeMap<>();
        this.background = Color.INVISIBLE;
        this.isTextRow = isTextRow;
    }

    public void add(TextRepresentationV2 child) {
        this.children.add(child);
        child.setParent(this);

        Size currentSize = getSize();
        setSize(new Size(//
                currentSize.getWidth() + child.getSize().getWidth(), //
                Math.max(currentSize.getHeight(), child.getSize().getHeight()) //
        ));

        this.cachedDrawable = null;
        this.xPositionToColumn.put(child.getRelativPosition().getX(), child);
    }

    @Override
    public TextPositionV2 getCursorElementAt(Vector2d pos) {
        var row = this.xPositionToColumn.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAt(//
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
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(this.background);

        for (var el : this.children) {
            this.cachedDrawable.addDrawable( //
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY() //
            );
        }

        return this.cachedDrawable.draw();
    }

    @Override
    protected TextPositionV2 getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        if (!isTextRow) {
            return super.moveIn(moving, fieldType, xOffset);
        }

        if (!checkMove(fieldType, getFieldType())) {
            return null;
        }

        Iterator<TextRepresentationV2> iterator = null;
        switch (moving) {
        case UP, DOWN:
            return getCursorElementAt(new Vector2d(xOffset, 0));
        case PREVIOUS:
            iterator = this.children.descendingIterator();
            break;
        case NEXT:
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

        if (!isTextRow) {
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
        } else {
            xOffset = fromChild.getRelativPositionTo(this).getX();
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

        if (!isTextRow) {
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
        } else {
            xOffset = fromChild.getRelativPositionTo(this).getX();
        }

        if (getParent() instanceof TextRepresentationV2) {
            var parentRepresentation = (TextRepresentationV2) getParent();
            return parentRepresentation.moveDown(this, fieldType, xOffset);
        }
        return null;
    }

}
