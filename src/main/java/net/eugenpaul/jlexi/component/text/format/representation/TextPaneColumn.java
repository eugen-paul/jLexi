package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextPaneColumn extends TextRepresentationOfRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneColumn.class);

    private TreeMap<Integer, TextRepresentation> yPositionToRow;

    public TextPaneColumn(Glyph parent) {
        super(parent);
        this.yPositionToRow = new TreeMap<>();
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        Size currentSize = getSize();
        setSize(new Size(//
                Math.max(currentSize.getWidth(), child.getSize().getWidth()), //
                currentSize.getHeight() + child.getSize().getHeight()//
        ));
        this.cachedDrawable = null;
        this.yPositionToRow.put(child.getRelativPosition().getY(), child);
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = this.yPositionToRow.floorEntry(pos.getY());
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
    public Drawable getDrawable() {
        if (this.cachedDrawable != null) {
            return this.cachedDrawable.draw();
        }

        this.cachedDrawable = new DrawableSketchImpl(Color.WHITE);

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
