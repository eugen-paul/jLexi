package net.eugenpaul.jlexi.component.text.formatting;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import net.eugenpaul.jlexi.component.Glyph;
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
public class RowCompositor<T extends TextPaneElement> extends Glyph implements TextCompositor<T> {
    private ConcurrentSkipListMap<Integer, TextCompositor<T>> yToRowMap;

    private BiFunction<Glyph, Size, TextCompositor<T>> rowConstructor;

    private Size maxSize;

    public RowCompositor(Glyph parent, Size maxSize, BiFunction<Glyph, Size, TextCompositor<T>> rowConstructor) {
        super(parent);
        this.maxSize = maxSize;
        this.rowConstructor = rowConstructor;
        this.yToRowMap = new ConcurrentSkipListMap<>();
    }

    @Override
    public void compose(Iterator<T> iterator) {
        int currentY = 0;
        int maxWidth = 0;
        yToRowMap.clear();

        TextCompositor<T> row = rowConstructor.apply(parent, maxSize);
        row.setParent(this);
        while (iterator.hasNext()) {
            T glyph = iterator.next();

            if (!row.addIfPossible(glyph) || glyph.isEndOfLine() || glyph.isPlaceHolder()) {
                // current row is full. Create new row.
                yToRowMap.putIfAbsent(currentY, row);
                row.getRelativPosition().setX(0);
                row.getRelativPosition().setY(currentY);

                currentY += row.getSize().getHight();
                maxWidth = Math.max(maxWidth, row.getSize().getHight());

                row = rowConstructor.apply(parent, maxSize);
                row.setParent(this);
            }
        }

        size = new Size(maxWidth, currentY);
    }

    @Override
    public Drawable getPixels() {
        List<Drawable> childDrawable = yToRowMap.values().stream()//
                .map(TextCompositor::getPixels)//
                .collect(Collectors.toList());

        int width = 0;
        int hight = 0;

        for (Drawable drawable : childDrawable) {
            width = Math.max(width, drawable.getPixelSize().getWidth());
            hight += drawable.getPixelSize().getHight();
        }

        int[] pixels = new int[width * hight];
        Size pixelsSize = new Size(width, hight);

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
            positionY += drawable.getPixelSize().getHight();
        }

        return new DrawableImpl(pixels, pixelsSize);
    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        // TODO Auto-generated method stub
        return null;
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
    public boolean addIfPossible(T element) {
        return false;
    }

    @Override
    public void updateSize(Size size) {
        this.maxSize = size;
    }

    @Override
    public Vector2d getRelativPosition() {
        return relativPosition;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        if (null == cursor) {
            return false;
        }

        Entry<Integer, TextCompositor<T>> childsRowEntry = getEntryOfGlyph(cursor);
        if (null == childsRowEntry) {
            return false;
        }

        if (childsRowEntry.getValue().moveCursor(move, cursor)) {
            return true;
        }

        boolean moved = false;
        switch (move) {
        case UP:
            moved = moveCursorUp(cursor, childsRowEntry);
            break;
        case DOWN:
            moved = moveCursorDown(cursor, childsRowEntry);
            break;
        default:
            break;
        }

        return moved;
    }

    private boolean moveCursorDown(Cursor cursor, Entry<Integer, TextCompositor<T>> childsRowEntry) {
        boolean moved = false;
        TextCompositor<T> child = yToRowMap.higherEntry(childsRowEntry.getKey()).getValue();
        if (null != child) {
            Vector2d cursorPosition = cursor.getCurrentGlyph().getData().getRelativPositionTo(this);
            if (null != cursorPosition) {
                cursorPosition.setY(cursorPosition.getY() + childsRowEntry.getKey());
                var element = child.getElementOnPosition(cursorPosition);
                cursor.moveCursorTo(element.getTextPaneListElement());
                moved = true;
            }
        }
        return moved;
    }

    private boolean moveCursorUp(Cursor cursor, Entry<Integer, TextCompositor<T>> childsRowEntry) {
        boolean moved = false;
        TextCompositor<T> child = yToRowMap.lowerEntry(childsRowEntry.getKey()).getValue();
        if (null != child) {
            Vector2d cursorPosition = cursor.getCurrentGlyph().getData().getRelativPositionTo(this);
            if (null != cursorPosition) {
                cursorPosition.setY(cursorPosition.getY() - childsRowEntry.getKey());
                var element = child.getElementOnPosition(cursorPosition);
                cursor.moveCursorTo(element.getTextPaneListElement());
                moved = true;
            }
        }
        return moved;
    }

    private Entry<Integer, TextCompositor<T>> getEntryOfGlyph(Cursor cursor) {
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

        Entry<Integer, TextCompositor<T>> childsRowEntry = null;
        for (Entry<Integer, TextCompositor<T>> rowEntry : yToRowMap.entrySet()) {
            if (rowEntry.getValue() == child) {
                childsRowEntry = rowEntry;
                break;
            }
        }

        return childsRowEntry;
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
    public void notifyUpdate(Glyph child) {
        if (null != parent) {
            parent.notifyUpdate(this);
        }
    }

}
