package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPaneRow extends TextStructureForm {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneRow.class);

    private TreeMap<Integer, TextElement> xPositionToRow;

    public TextPaneRow(Glyph parent) {
        super(parent);
        this.xPositionToRow = new TreeMap<>();
    }

    @Override
    public void notifyUpdate(TextElement element, Drawable draw, Area area) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyResize(TextElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        var row = xPositionToRow.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextElement clickedElement = row.getValue().getCorsorElementAt(//
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
    public void getFirstChild() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getLastChild() {
        // TODO Auto-generated method stub

    }

    @Override
    public Drawable getPixels() {
        var sites = children;

        int[] pixels = new int[getSize().getWidth() * getSize().getHeight()];

        cachedDrawable = new DrawableImpl(pixels, getSize());

        xPositionToRow.clear();

        Vector2d position = new Vector2d(0, 0);
        for (var el : sites) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);

            var xPosition = position.getX();

            xPositionToRow.put(xPosition, el);

            el.setRelativPosition(new Vector2d(xPosition, 0));

            position.setX(xPosition + el.getSize().getWidth());
        }

        return cachedDrawable;
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
    public Iterator<TextStructureForm> printableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

}
