package net.eugenpaul.jlexi.component.text.format.representation;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextStructureForm extends Glyph implements CursorControl, GlyphIterable<TextStructureForm> {

    protected TextStructureForm(Glyph parent) {
        super(parent);
    }

    public abstract boolean isEmpty();

    public abstract void notifyUpdate(TextElement element, Drawable draw, Area area);

    public abstract void notifyResize(TextElement element);

    public abstract TextElement getCorsorElementAt(Vector2d pos);

    public abstract TextElement getFirstChild();

    public abstract TextElement getLastChild();

    @Override
    public TextElement getNext(TextElement element) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        var e = structure.getNext(element);

        while (e == null && structure != null) {
            structure = getNextStructureForm(structure);
            if (structure != null) {
                e = structure.getFirstChild();
            }
        }

        return e;
    }

    public abstract TextStructureForm getNextStructureForm(TextStructureForm structure);

    @Override
    public TextElement getPrevious(TextElement element, boolean moveForDelete) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        var e = structure.getPrevious(element, moveForDelete);
        while (e == null && structure != null) {
            structure = getPreviousStructureForm(structure);
            if (structure != null) {
                e = structure.getLastChild();
            }
        }

        return e;
    }

    public abstract TextStructureForm getPreviousStructureForm(TextStructureForm structure);

    protected TextStructureForm getChildStructureOfElement(TextElement element) {
        var parent = element.getParent();
        Glyph previousParent = null;
        while (parent != null && parent != this) {
            previousParent = parent;
            parent = parent.getParent();
        }

        if (previousParent instanceof TextStructureForm) {
            return (TextStructureForm) previousParent;
        }
        return null;
    }

    @Override
    public TextElement getUp(TextElement element) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        return structure.getUp(element);
    }

    @Override
    public TextElement getDown(TextElement element) {
        var structure = getChildStructureOfElement(element);
        if (null == structure) {
            return null;
        }

        return structure.getDown(element);
    }

    @Override
    public TextElement getEol(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getBol(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getStart(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getEnd(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPageUp(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPageDown(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }
}
