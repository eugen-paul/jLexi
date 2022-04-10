package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class TextPaneColumn extends TextStructureFormOfStructures {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneColumn.class);

    private TreeMap<Integer, TextStructureForm> yPositionToRow;

    public TextPaneColumn(Glyph parent) {
        super(parent);
        this.yPositionToRow = new TreeMap<>();
    }

    @Override
    public void add(TextStructureForm child) {
        super.add(child);
        Size currentSize = getSize();
        setSize(new Size(//
                Math.max(currentSize.getWidth(), child.getSize().getWidth()), //
                currentSize.getHeight() + child.getSize().getHeight()//
        ));
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
    public Drawable getPixels() {
        var sites = children;

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
    public TextElement getUp(TextElement element) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        var e = structure.getUp(element);

        if (null == e) {
            var pos = element.getRelativPositionTo(this);
            while (e == null && structure != null && pos != null) {
                structure = getPreviousStructureForm(structure);
                if (structure != null) {
                    pos.setY(0);
                    e = structure.getCorsorElementAt(pos);
                }
            }
        }

        return e;
    }

    @Override
    public TextElement getDown(TextElement element) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        var e = structure.getDown(element);

        if (null == e) {
            var pos = element.getRelativPositionTo(this);
            while (e == null && structure != null && pos != null) {
                structure = getNextStructureForm(structure);
                if (structure != null) {
                    pos.setY(0);
                    e = structure.getCorsorElementAt(pos);
                }
            }
        }

        return e;
    }

}
