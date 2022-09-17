package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPaneRow extends TextRepresentationOfRepresentation {

    private TreeMap<Integer, TextRepresentation> xPositionToColumn;

    @Getter
    @Setter
    private Color background;

    public TextPaneRow(Glyph parent) {
        super(parent);
        this.xPositionToColumn = new TreeMap<>();
        this.background = Color.INVISIBLE;
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
        this.xPositionToColumn.put(child.getRelativPosition().getX(), child);
    }

    @Override
    public TextPosition getCursorElementAt(Vector2d pos) {
        var row = this.xPositionToColumn.floorEntry(pos.getX());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAt(//
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

        this.cachedDrawable = new DrawableSketchImpl(this.background);

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
    protected TextPosition getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPosition getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

}
