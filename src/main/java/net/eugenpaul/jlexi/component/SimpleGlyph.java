package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.visitor.Visitor;

@Getter
@Setter
public class SimpleGlyph extends Glyph {

    /**
     * C'tor
     * 
     * @param parent
     */
    public SimpleGlyph() {
        super(null);
    }

    @Override
    public Drawable getPixels() {
        return cachedDrawable;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

    public void setDrawable(Drawable drawable) {
        size = drawable.getPixelSize();
        cachedDrawable = drawable;
    }

}
