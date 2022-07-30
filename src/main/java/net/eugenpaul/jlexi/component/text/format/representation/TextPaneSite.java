package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPaneSite extends TextRepresentationOfRepresentation {

    private static final int DEFAULT_MARGIN_TOP = 5;
    private static final int DEFAULT_MARGIN_BOTTOM = 5;

    private TreeMap<Integer, TextRepresentation> xPositionToColumn;

    public TextPaneSite(Glyph parent, Size size) {
        super(parent);
        setSize(size);
        setMarginTop(DEFAULT_MARGIN_TOP);
        setMarginBottom(DEFAULT_MARGIN_BOTTOM);

        this.xPositionToColumn = new TreeMap<>();
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        child.setParent(this);

        this.cachedDrawable = null;
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.xPositionToColumn.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCursorElementAt(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Site Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Site Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    public TextPositionV2 getCursorElementAtV2(Vector2d pos) {
        var row = this.xPositionToColumn.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAtV2(//
                new Vector2d(//
                        pos.sub(row.getValue().getRelativPosition())//
                )//
        );
        if (clickedElement != null) {
            LOGGER.trace("Site Click on Element: {}.", clickedElement);
        } else {
            LOGGER.trace("Site Click on Element: NONE.");
        }
        return clickedElement;
    }

    @Override
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE, getSize());
        this.xPositionToColumn.clear();

        for (var el : children) {
            this.cachedDrawable.addDrawable(//
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY() //
            );

            this.xPositionToColumn.put(el.getRelativPosition().getX(), el);
        }

        return this.cachedDrawable.draw();
    }

    @Override
    protected TextPosition getLastText(int x) {
        var col = this.xPositionToColumn.floorEntry(x);
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getLastText(x - pos.getX());
    }

    @Override
    protected TextPositionV2 getLastTextV2(int x) {
        var col = this.xPositionToColumn.floorEntry(x);
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getLastTextV2(x - pos.getX());
    }

    @Override
    protected TextPosition getFirstText(int x) {
        var col = this.xPositionToColumn.floorEntry(x);
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getFirstText(x - pos.getX());
    }

    @Override
    protected TextPositionV2 getFirstTextV2(int x) {
        var col = this.xPositionToColumn.floorEntry(x);
        if (col == null) {
            return null;
        }

        var pos = col.getValue().getRelativPositionTo(this);

        return col.getValue().getFirstTextV2(x - pos.getX());
    }

}
