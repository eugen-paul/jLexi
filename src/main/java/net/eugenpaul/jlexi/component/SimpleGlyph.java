package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2Sketch;
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
    public DrawableV2 getDrawable() {
        return cachedDrawableV2.draw();
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

    public void setDrawable(DrawableV2Sketch drawable) {
        setSize(drawable.getSize());
        cachedDrawableV2 = drawable;
    }

}
