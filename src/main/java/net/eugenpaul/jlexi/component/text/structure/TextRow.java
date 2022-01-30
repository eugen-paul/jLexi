package net.eugenpaul.jlexi.component.text.structure;

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
import net.eugenpaul.jlexi.component.text.formatting.TextCompositor;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class TextRow<T extends TextPaneElement> extends TextPaneElement implements TextCompositor<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextRow.class);

    private LinkedList<T> children;

    private Size maxSize;

    public TextRow(Glyph parent, Size maxSize) {
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
        int hight = getSize().getHight();

        int[] pixels = new int[width * hight];
        Size pixelsSize = new Size(width, hight);

        int positionX = 0;
        for (Drawable drawable : childDrawable) {
            ImageArrayHelper.copyRectangle(//
                    drawable.getPixels(), //
                    drawable.getPixelSize(), //
                    new Vector2d(0, 0), //
                    drawable.getPixelSize(), //
                    pixels, //
                    pixelsSize, //
                    new Vector2d(positionX, hight - drawable.getPixelSize().getHight()) //
            );
            positionX += drawable.getPixelSize().getWidth();
        }

        return new DrawableImpl(pixels, pixelsSize);
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
        return null;
    }

    @Override
    public boolean isCursorHoldable() {
        return true;
    }

    @Override
    public void notifyUpdate(Glyph child) {
        LOGGER.trace("row notifyUpdate to parent");
        getParent().notifyUpdate(this);
    }

    @Override
    public TextPaneElement getElementOnPosition(Vector2d position) {
        return getChildOn(position.getX(), position.getY()).getData();
    }

    @Override
    public void compose(Iterator<T> iterator) {
        // TODO Auto-generated method stub

    }

    @Override
    public Drawable getPixels(Vector2d position, Size size) {
        // TODO Auto-generated method stub
        return null;
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
                Math.max(getSize().getHight(), element.getSize().getHight())//
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

}
