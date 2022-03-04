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

public class TextPaneColumn extends TextStructureForm {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneColumn.class);

    private TreeMap<Integer, TextStructureForm> yPositionToRow;

    public TextPaneColumn(Glyph parent) {
        super(parent);
        this.yPositionToRow = new TreeMap<>();
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
        var row = yPositionToRow.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        TextElement clickedElement = row.getValue().getCorsorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Column Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Column Click on Element: NONE.");
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
        var sites = childrenForm;

        int[] pixels = new int[getSize().getWidth() * getSize().getHeight()];

        cachedDrawable = new DrawableImpl(pixels, getSize());

        yPositionToRow.clear();

        Vector2d position = new Vector2d(0, 0);
        for (var el : sites) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);

            var yPosition = position.getY();

            yPositionToRow.put(yPosition, el);

            el.setRelativPosition(new Vector2d(0, yPosition));

            position.setY(yPosition + el.getSize().getHeight());
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
