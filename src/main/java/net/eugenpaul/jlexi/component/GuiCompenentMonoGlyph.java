package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;

/**
 * Abstract Class for Gui-Glyph that cann have just one GuiComponent-Child
 * 
 */
public abstract class GuiCompenentMonoGlyph extends Glyph implements GuiComponent {

    protected GuiComponent component;

    protected GuiCompenentMonoGlyph(Glyph parent, GuiComponent component) {
        super(parent);
        this.component = component;
    }

    @Override
    public Drawable getPixels() {
        if (component == null) {
            return DrawableImpl.EMPTY_DRAWABLE;
        }
        return component.getGlyph().getPixels();
    }

    @Override
    public void visit(Visitor checker) {
        if (component == null) {
            return;
        }
        component.getGlyph().visit(checker);
    }

    @Override
    public Iterator<Glyph> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }
        return component.getGlyph().iterator();
    }

    @Override
    public Glyph getGlyph() {
        return this;
    }
}
