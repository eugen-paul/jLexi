package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableSketch;
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
    public Drawable getDrawable() {
        return cachedDrawable.draw();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

    public void setDrawable(DrawableSketch drawable) {
        setSize(drawable.getSize());
        cachedDrawable = drawable;
    }

}
