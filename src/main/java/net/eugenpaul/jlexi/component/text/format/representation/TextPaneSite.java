package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPaneSite extends TextStructureForm {

    public TextPaneSite(Glyph parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void notifyUpdate(TextElement element, Drawable draw, Area area) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyResize(TextElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public TextElement getCorsorElementAt(Vector2d pos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void getFirstChild() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getLastChild() {
        // TODO Auto-generated method stub

    }

    @Override
    public Drawable getPixels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<Glyph> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<TextStructureForm> printableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

}
