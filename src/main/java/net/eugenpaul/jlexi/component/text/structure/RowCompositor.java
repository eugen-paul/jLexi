package net.eugenpaul.jlexi.component.text.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.TextPaneElementToGlyphIterator;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

/**
 * Compose elements to rows
 */
public class RowCompositor<T extends TextPaneElement> extends TextCompositor<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(RowCompositor.class);

    private ConcurrentSkipListMap<Integer, TextContainer<T>> yToRowMap;

    public RowCompositor(Glyph parent, Size maxSize) {
        super(parent);
        this.maxSize = maxSize;
        this.containerConstructor = RowContainer::new;
        this.yToRowMap = new ConcurrentSkipListMap<>();
    }

    @Override
    public void compose(Iterator<T> iterator) {
        int currentY = 0;
        int maxWidth = 0;
        yToRowMap.clear();

        TextContainer<T> row = containerConstructor.apply(parent, maxSize);
        row.setParent(this);
        while (iterator.hasNext()) {
            T glyph = iterator.next();

            if (!row.addIfPossible(glyph)) {
                // current row is full. Create new row.
                yToRowMap.putIfAbsent(currentY, row);
                row.getRelativPosition().setX(0);
                row.getRelativPosition().setY(currentY);

                currentY += row.getSize().getHeight();
                maxWidth = Math.max(maxWidth, row.getSize().getHeight());

                row = containerConstructor.apply(parent, maxSize);
                row.add(glyph);
                row.setParent(this);
            }

            if (glyph.isEndOfLine() || glyph.isPlaceHolder()) {
                // line break. Create new row.
                yToRowMap.putIfAbsent(currentY, row);
                row.getRelativPosition().setX(0);
                row.getRelativPosition().setY(currentY);

                currentY += row.getSize().getHeight();
                maxWidth = Math.max(maxWidth, row.getSize().getHeight());

                row = containerConstructor.apply(parent, maxSize);
                row.setParent(this);
            }
        }

        if (!row.isEmpty()) {
            // Add last row to map.
            yToRowMap.putIfAbsent(currentY, row);
            row.getRelativPosition().setX(0);
            row.getRelativPosition().setY(currentY);

            currentY += row.getSize().getHeight();
            maxWidth = Math.max(maxWidth, row.getSize().getHeight());
        }

        size = new Size(maxWidth, currentY);

    }

    @Override
    public Drawable getPixels() {
        List<Drawable> childDrawable = yToRowMap.values().stream()//
                .map(TextContainer::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int height = 0;

        for (Drawable drawable : childDrawable) {
            width = Math.max(width, drawable.getPixelSize().getWidth());
            height += drawable.getPixelSize().getHeight();
        }

        int[] pixels = new int[width * height];
        Size pixelsSize = new Size(width, height);

        int positionX = 0;
        int positionY = 0;
        for (Drawable drawable : childDrawable) {
            ImageArrayHelper.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    new Vector2d(0, 0), //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    new Vector2d(positionX, positionY)//
            );
            positionY += drawable.getPixelSize().getHeight();
        }

        cachedDrawable = new DrawableImpl(pixels, pixelsSize);

        return cachedDrawable;
    }

    @Override
    public TextPaneElement getElementOnPosition(Vector2d position) {
        var row = yToRowMap.floorEntry(position.getY());
        if (null == row) {
            return null;
        }

        return row.getValue().getElementOnPosition(position.subNew(row.getValue().getRelativPosition()));
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        if (null == cursor) {
            return false;
        }

        Entry<Integer, TextContainer<T>> childsRowEntry = getEntryOfGlyph(cursor);
        if (null == childsRowEntry) {
            return false;
        }

        if (childsRowEntry.getValue().moveCursor(move, cursor)) {
            return true;
        }

        boolean moved = false;
        switch (move) {
        case UP:
            moved = moveCursorPreviousRow(cursor, childsRowEntry, false);
            break;
        case DOWN:
            moved = moveCursorToNextRow(cursor, childsRowEntry, false);
            break;
        case LEFT:
            moved = moveCursorPreviousRow(cursor, childsRowEntry, true);
            break;
        case RIGHT:
            moved = moveCursorToNextRow(cursor, childsRowEntry, true);
            break;
        default:
            break;
        }

        return moved;
    }

    private boolean moveCursorToNextRow(Cursor cursor, Entry<Integer, TextContainer<T>> childsRowEntry,
            boolean setCursorToBegin) {
        boolean moved = false;
        TextContainer<T> child = yToRowMap.higherEntry(childsRowEntry.getKey()).getValue();
        if (null == child) {
            // it is the last row. Move cursor down is not possible.
            return moved;
        }

        Vector2d cursorPosition = cursor.getCurrentGlyph().getData().getRelativPositionTo(this);
        if (null != cursorPosition) {
            if (setCursorToBegin) {
                cursorPosition.setX(0);
            }
            cursorPosition.setY(0);
            var element = child.getElementOnPosition(cursorPosition);
            if (element != null) {
                cursor.moveCursorTo(element.getTextPaneListElement());
                moved = true;
            }
        }

        return moved;
    }

    private boolean moveCursorPreviousRow(Cursor cursor, Entry<Integer, TextContainer<T>> childsRowEntry,
            boolean setCursorToEnd) {
        boolean moved = false;
        TextContainer<T> child = yToRowMap.lowerEntry(childsRowEntry.getKey()).getValue();
        if (null == child) {
            // it is the first row. Move cursor up is not possible.
            return moved;
        }

        Vector2d cursorPosition = cursor.getCurrentGlyph().getData().getRelativPositionTo(this);
        if (null != cursorPosition) {
            if (setCursorToEnd) {
                cursorPosition.setX(child.getSize().getWidth() - 1);
            }
            cursorPosition.setY(child.getSize().getHeight() - 1);
            var element = child.getElementOnPosition(cursorPosition);
            if (element != null) {
                cursor.moveCursorTo(element.getTextPaneListElement());
                moved = true;
            }
        }

        return moved;
    }

    private Entry<Integer, TextContainer<T>> getEntryOfGlyph(Cursor cursor) {
        Glyph child = cursor.getCurrentGlyph().getData();
        while (null != child) {
            if (this == child.getParent()) {
                break;
            }
            child = child.getParent();
        }

        if (null == child) {
            return null;
        }

        Entry<Integer, TextContainer<T>> childsRowEntry = null;
        for (Entry<Integer, TextContainer<T>> rowEntry : yToRowMap.entrySet()) {
            if (rowEntry.getValue() == child) {
                childsRowEntry = rowEntry;
                break;
            }
        }

        return childsRowEntry;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return new TextPaneElementToGlyphIterator<>(yToRowMap.values());
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

}