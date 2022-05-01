package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.var;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2SketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextPaneRow extends TextRepresentationOfRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneRow.class);

    private TreeMap<Integer, TextRepresentation> xPositionToRow;

    public TextPaneRow(Glyph parent) {
        super(parent);
        this.xPositionToRow = new TreeMap<>();
    }

    @Override
    public void add(TextRepresentation child) {
        super.add(child);
        Size currentSize = getSize();
        setSize(new Size(//
                currentSize.getWidth() + child.getSize().getWidth(), //
                Math.max(currentSize.getHeight(), child.getSize().getHeight()) //
        ));
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = xPositionToRow.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCorsorElementAt(//
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
    public DrawableV2 getDrawable() {
        cachedDrawableV2 = new DrawableV2SketchImpl(Color.WHITE);
        xPositionToRow.clear();

        int currentX = 0;
        for (var el : children) {
            int currentY = getSize().getHeight() - el.getSize().getHeight();

            cachedDrawableV2.addDrawable(el.getDrawable(), currentX, currentY);

            xPositionToRow.put(currentX, el);

            el.setRelativPosition(new Vector2d(currentX, currentY));

            currentX += el.getSize().getWidth();
        }

        return cachedDrawableV2.draw();
    }

}
