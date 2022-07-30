package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketchImpl;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Slf4j
public class TextPaneColumn extends TextRepresentationOfRepresentation {

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
    public TextPositionV2 getCursorElementAtV2(Vector2d pos) {
        var row = this.yPositionToRow.floorEntry(pos.getY());
        if (null == row) {
            return null;
        }

        var clickedElement = row.getValue().getCursorElementAtV2(//
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
    public TextPositionV2 move(TextPositionV2 position, MovePosition moving) {
        switch (moving) {
        case UP:
            return getUp(position);
        case DOWN:
            return getDown(position);
        default:
            throw new NotYetImplementedException("Moving " + moving + " is not implemented.");
        }
    }

    private TextPositionV2 getUp(TextPositionV2 position) {
        // TODO
        var newPosition = position.move(MovePosition.UP);

        if (newPosition == null) {
            var childGlyph = position.getTextElement().getChild(this);

            if (childGlyph instanceof TextRepresentation) {
                var representation = (TextRepresentation) childGlyph;

                var pos = position.getTextElement().getRelativPositionTo(this);
                while (newPosition == null && representation != null && pos != null) {
                    representation = getPreviousRepresentation(representation);
                    if (representation != null) {
                        newPosition = representation.getLastTextV2(pos.getX());
                    }
                }
            }

        }

        return newPosition;
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

    private TextPositionV2 getDown(TextPositionV2 position) {
        // TODO
        return null;
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
    protected TextPositionV2 getFirstTextV2(int x) {
        if (yPositionToRow.isEmpty()) {
            return null;
        }

        var entry = yPositionToRow.firstEntry();
        var pos = entry.getValue().getRelativPositionTo(this);

        return entry.getValue().getFirstTextV2(x - pos.getX());
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

    @Override
    protected TextPositionV2 getLastTextV2(int x) {
        if (yPositionToRow.isEmpty()) {
            return null;
        }

        var entry = yPositionToRow.lastEntry();
        var pos = entry.getValue().getRelativPositionTo(this);

        return entry.getValue().getFirstTextV2(x - pos.getX());
    }

}
