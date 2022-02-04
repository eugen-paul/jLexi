package net.eugenpaul.jlexi.component.text.formatting;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.TextPaneElementToGlyphIterator;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class RowContainer<T extends TextPaneElement> extends TextContainer<T> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(RowContainer.class);

    private LinkedList<T> children;

    private Size maxSize;

    public RowContainer(Glyph parent, Size maxSize) {
        super(parent, null);
        this.maxSize = maxSize;
        setSize(Size.ZERO_SIZE);
        children = new LinkedList<>();
    }

    @Override
    public Drawable getPixels() {
        List<Drawable> childDrawable = children.stream()//
                .map(Glyph::getPixels)//
                .collect(Collectors.toList());

        int width = getSize().getWidth();
        int height = getSize().getHeight();

        int[] pixels = new int[width * height];
        Size pixelsSize = new Size(width, height);

        int positionX = 0;
        for (Drawable drawable : childDrawable) {
            ImageArrayHelper.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    new Vector2d(0, 0), //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    new Vector2d(positionX, height - drawable.getPixelSize().getHeight()) //
            );
            positionX += drawable.getPixelSize().getWidth();
        }

        cachedDrawable = new DrawableImpl(pixels, pixelsSize);

        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return new TextPaneElementToGlyphIterator<>(children);
    }

    @Override
    public void visit(Visitor checker) {
        checker.visit(this);
    }

    @Override
    public NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY) {
        int x = 0;
        for (TextPaneElement glyph : children) {
            if (x + glyph.getSize().getWidth() > mouseX) {
                if (glyph.isCursorHoldable()) {
                    return glyph.getChildOn(mouseX - x, mouseY);
                }
                return glyph.getTextPaneListElement();
            }
            x += glyph.getSize().getWidth();
        }
        // if nothing found, return last element
        return children.getLast().getTextPaneListElement();
    }

    @Override
    public TextPaneElement getElementOnPosition(Vector2d position) {
        var child = getChildOn(position.getX(), position.getY());
        if (child == null) {
            return null;
        }
        return child.getData();
    }

    @Override
    public void add(T element) {
        int currentWidth = getSize().getWidth();
        children.add(element);
        element.setParent(this);
        element.getRelativPosition().setX(currentWidth);
        element.getRelativPosition().setY(0);

        setSize(new Size(//
                currentWidth + element.getSize().getWidth(), //
                Math.max(getSize().getHeight(), element.getSize().getHeight())//
        ) //
        );
    }

    @Override
    public boolean addIfPossible(T element) {
        int currentWidth = getSize().getWidth();
        if (element.getSize().getWidth() + currentWidth > maxSize.getWidth()) {
            return false;
        }
        children.add(element);
        element.setParent(this);
        element.getRelativPosition().setX(currentWidth);
        element.getRelativPosition().setY(0);

        setSize(new Size(//
                currentWidth + element.getSize().getWidth(), //
                Math.max(getSize().getHeight(), element.getSize().getHeight())//
        ) //
        );
        return true;
    }

    @Override
    public void updateSize(Size size) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {

        ListIterator<T> iterator = children.listIterator();
        T lastChild = null;
        while (iterator.hasNext()) {
            T child = iterator.next();
            if (child == cursor.getCurrentGlyph().getData()) {
                if (child.moveCursor(move, cursor)) {
                    return true;
                }
                return moveCursor(move, cursor, iterator, lastChild);
            }
            lastChild = child;
        }

        return false;
    }

    private boolean moveCursor(CursorMove move, Cursor cursor, ListIterator<T> iterator, T lastChild) {
        boolean moved = false;
        switch (move) {
        case LEFT:
            if (null != lastChild) {
                cursor.moveCursorTo(lastChild.getTextPaneListElement());
                moved = true;
            }
            break;
        case RIGHT:
            if (iterator.hasNext()) {
                cursor.moveCursorTo(iterator.next().getTextPaneListElement());
                moved = true;
            }
            break;
        default:
            break;
        }
        return moved;
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

}
