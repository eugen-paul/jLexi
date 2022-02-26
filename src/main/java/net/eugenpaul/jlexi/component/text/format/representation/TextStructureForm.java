package net.eugenpaul.jlexi.component.text.format.representation;

import java.awt.geom.Area;
import java.util.List;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextStructureForm extends Glyph implements CursorControl {

    private List<TextField> children;

    protected TextStructureForm(Glyph parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public abstract void notifyUpdate(TextElement element, Drawable draw, Area area);

    public abstract void notifyResize(TextElement element);

    public abstract void getCorsorElementAt(Vector2d pos);

    public abstract void getFirstChild();

    public abstract void getLastChild();

    @Override
    public TextElement getNext(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getPrevious(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getUp(TextElement element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextElement getDown(TextElement element) {
        // TODO Auto-generated method stub
        return null;
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
