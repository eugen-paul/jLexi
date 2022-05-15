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

public class TextPaneSite extends TextRepresentationOfRepresentation {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPaneSite.class);

    private TreeMap<Integer, TextRepresentation> xPositionToColumn;

    public TextPaneSite(Glyph parent, Size size) {
        super(parent);
        setSize(size);
        this.xPositionToColumn = new TreeMap<>();
    }

    public void add(TextRepresentation child) {
        this.children.add(child);
        this.cachedDrawable = null;
    }

    @Override
    public TextPosition getCorsorElementAt(Vector2d pos) {
        var row = this.xPositionToColumn.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        TextPosition clickedElement = row.getValue().getCorsorElementAt(//
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

}
