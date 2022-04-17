package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextRepresentation extends Glyph implements CursorControl, GlyphIterable<TextRepresentation> {

    protected TextRepresentation(Glyph parent) {
        super(parent);
    }

    public abstract boolean isEmpty();

    public abstract void notifyUpdate(TextElement element, Drawable draw, Area area);

    public abstract void notifyResize(TextElement element);

    public abstract TextPosition getCorsorElementAt(Vector2d pos);

    public abstract TextPosition getFirstChild();

    public abstract TextPosition getLastChild();

    @Override
    public TextPosition getNext(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        var e = representation.getNext(position);

        while (e == null && representation != null) {
            representation = getNextRepresentation(representation);
            if (representation != null) {
                e = representation.getFirstChild();
            }
        }

        return e;
    }

    public abstract TextRepresentation getNextRepresentation(TextRepresentation representation);

    @Override
    public TextPosition getPrevious(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        var e = representation.getPrevious(position);
        while (e == null && representation != null) {
            representation = getPreviousRepresentation(representation);
            if (representation != null) {
                e = representation.getLastChild();
            }
        }

        return e;
    }

    public abstract TextRepresentation getPreviousRepresentation(TextRepresentation structure);

    @Override
    public TextPosition getUp(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        return representation.getUp(position);
    }

    @Override
    public TextPosition getDown(TextPosition position) {
        var representation = position.getRepresentationChild(this);
        if (null == representation) {
            return null;
        }

        return representation.getDown(position);
    }

    @Override
    public TextPosition getEol(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextPosition getBol(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextPosition getStart(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextPosition getEnd(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextPosition getPageUp(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextPosition getPageDown(TextPosition element) {
        // TODO Auto-generated method stub
        return null;
    }
}
