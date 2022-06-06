package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextRepresentation extends Glyph implements CursorControl, GlyphIterable<TextRepresentation> {

    @Getter
    @Setter
    private int marginLeft;
    @Getter
    @Setter
    private int marginRight;
    @Getter
    @Setter
    private int marginTop;
    @Getter
    @Setter
    private int marginBottom;

    protected TextRepresentation(Glyph parent) {
        super(parent);

        this.marginLeft = 0;
        this.marginRight = 0;
        this.marginTop = 0;
        this.marginBottom = 0;
    }

    public abstract boolean isEmpty();

    public abstract TextPosition getCursorElementAt(Vector2d pos);

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

    protected abstract TextPosition getLastText(int x);
    protected abstract TextPosition getFirstText(int x);

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
