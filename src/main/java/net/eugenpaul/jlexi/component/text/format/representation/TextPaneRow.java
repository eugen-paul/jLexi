package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import lombok.var;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPaneRow extends TextRepresentationOfRepresentation {

    private TreeMap<Integer, TextRepresentation> xPositionToRow;

    public TextPaneRow(Glyph parent) {
        super(parent);
        this.xPositionToRow = new TreeMap<>();
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        child.setParent(this);

        Size currentSize = getSize();
        setSize(new Size(//
                currentSize.getWidth() + child.getSize().getWidth(), //
                Math.max(currentSize.getHeight(), child.getSize().getHeight()) //
        ));

        this.cachedDrawable = null;
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.xPositionToRow.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCursorElementAt(//
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
    public TextPositionV2 getCursorElementAtV2(Vector2d pos) {
        var row = this.xPositionToRow.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAtV2(//
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

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);
        this.xPositionToRow.clear();

        int currentX = 0;
        for (var el : children) {
            int currentY = getSize().getHeight() - el.getSize().getHeight();

            this.cachedDrawable.addDrawable(el.getDrawable(), currentX, currentY);

            this.xPositionToRow.put(currentX, el);

            el.setRelativPosition(new Vector2d(currentX, currentY));

            currentX += el.getSize().getWidth();
        }

        return this.cachedDrawable.draw();
    }

    @Override
    protected TextPosition getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 getLastTextV2(int x) {
        return getCursorElementAtV2(new Vector2d(x, 0));
    }

    @Override
    protected TextPosition getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 getFirstTextV2(int x) {
        return getCursorElementAtV2(new Vector2d(x, 0));
    }

}
