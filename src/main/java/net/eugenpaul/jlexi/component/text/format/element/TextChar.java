package net.eugenpaul.jlexi.component.text.format.element;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextChar extends TextElementAbstract{

    protected TextChar(Glyph parent) {
        super(parent);
    }

    @Override
    public boolean isEndOfLine() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPlaceHolder() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void reset() {
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
    
}
