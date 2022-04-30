package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

public class TextPaneColumn extends TextRepresentationOfRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneColumn.class);

    private TreeMap<Integer, TextRepresentation> yPositionToRow;

    public TextPaneColumn(Glyph parent) {
        super(parent);
        this.yPositionToRow = new TreeMap<>();
    }

    @Override
    public void add(TextRepresentation child) {
        super.add(child);
        Size currentSize = getSize();
        setSize(new Size(//
                Math.max(currentSize.getWidth(), child.getSize().getWidth()), //
                currentSize.getHeight() + child.getSize().getHeight()//
        ));
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = yPositionToRow.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCorsorElementAt(//
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
        int[] pixels = new int[getSize().getWidth() * getSize().getHeight()];

        cachedDrawable = new DrawableImpl(pixels, getSize());

        yPositionToRow.clear();

        Vector2d position = new Vector2d(0, 0);
        for (var el : children) {
            ImageArrayHelper.copyRectangle(el.getPixels(), cachedDrawable, position);

            var yPosition = position.getY();

            yPositionToRow.put(yPosition, el);

            el.setRelativPosition(new Vector2d(0, yPosition));

            position.setY(yPosition + el.getSize().getHeight());
        }

        return cachedDrawable;
    }

    @Override
    public DrawableV2 getDrawable() {
        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE);

        yPositionToRow.clear();

        int currentY = 0;
        for (var el : children) {
            cachedDrawableV2.addDrawable(el.getDrawable(), 0, currentY);

            yPositionToRow.put(currentY, el);

            el.setRelativPosition(new Vector2d(0, currentY));

            currentY += el.getSize().getHeight();
        }

        return cachedDrawableV2.draw();
    }

    @Override
    public TextPosition getUp(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        var e = representation.getUp(position);

        if (null == e) {
            var pos = position.getTextElement().getRelativPositionTo(this);
            while (e == null && representation != null && pos != null) {
                representation = getPreviousRepresentation(representation);
                if (representation != null) {
                    pos.setY(0);
                    e = representation.getCorsorElementAt(pos);
                }
            }
        }

        return e;
    }

    @Override
    public TextPosition getDown(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        var e = representation.getDown(position);

        if (null == e) {
            var pos = position.getTextElement().getRelativPositionTo(this);
            while (e == null && representation != null && pos != null) {
                representation = getNextRepresentation(representation);
                if (representation != null) {
                    pos.setY(0);
                    e = representation.getCorsorElementAt(pos);
                }
            }
        }

        return e;
    }

}
