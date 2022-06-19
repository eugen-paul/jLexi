package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextPaneColumn extends TextRepresentationOfRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneColumn.class);

    private TreeMap<Integer, TextRepresentation> yPositionToRow;

    @Getter
    @Setter
    private Color background;

    public TextPaneColumn(Glyph parent) {
        super(parent);
        this.yPositionToRow = new TreeMap<>();
        this.background = Color.WHITE;
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        child.setParent(this);

        Size currentSize = getSize();
        setSize(new Size(//
                Math.max(currentSize.getWidth(),
                        child.getSize().getWidth() + child.getMarginLeft() + child.getMarginRight()), //
                currentSize.getHeight() + child.getSize().getHeight() + child.getMarginBottom() + child.getMarginTop()//
        ));

        this.cachedDrawable = null;
        this.yPositionToRow.put(child.getRelativPosition().getY(), child);
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.yPositionToRow.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCursorElementAt(//
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
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(background);

        for (var el : this.children) {
            this.cachedDrawable.addDrawable( //
                    el.getDrawable(), //
                    el.getRelativPosition().getX(), //
                    el.getRelativPosition().getY() //
            );
        }

        return this.cachedDrawable.draw();
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
                    e = representation.getLastText(pos.getX());
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
                    e = representation.getFirstText(pos.getX());
                }
            }
        }

        return e;
    }

    @Override
    protected TextPosition getFirstText(int x) {
        if (yPositionToRow.isEmpty()) {
            return null;
        }

        var entry = yPositionToRow.firstEntry();
        var pos = entry.getValue().getRelativPositionTo(this);

        return entry.getValue().getFirstText(x - pos.getX());
    }

    @Override
    protected TextPosition getLastText(int x) {
        if (yPositionToRow.isEmpty()) {
            return null;
        }

        var entry = yPositionToRow.lastEntry();
        var pos = entry.getValue().getRelativPositionTo(this);

        return entry.getValue().getFirstText(x - pos.getX());
    }

}
