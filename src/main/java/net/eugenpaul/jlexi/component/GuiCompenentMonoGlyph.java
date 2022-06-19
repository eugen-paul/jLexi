package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.helper.KeyEventAdapterToKeyPressable;
import net.eugenpaul.jlexi.component.helper.MouseEventAdapterOnGuiGlyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Abstract Class for Gui-Glyph that cann have just one GuiComponent-Child
 * 
 */
public abstract class GuiCompenentMonoGlyph extends GuiGlyph {

    protected GuiGlyph component;

    protected GuiCompenentMonoGlyph(Glyph parent, GuiGlyph component) {
        super(parent);
        this.component = component;
        this.component.setParent(this);

        this.keyEventAdapter = new KeyEventAdapterToKeyPressable(component);

        this.mouseEventAdapter = new MouseEventAdapterOnGuiGlyph(//
                this.component, //
                this::isPositionOnComponent, //
                this::onMouseClickOutsideComponent, //
                this::onMousePressedOutsideComponent, //
                this::onMousePressedOutsideComponent, //
                this::onMouseWhellMoved //
        );
    }

    @Override
    public Drawable getDrawable() {
        if (component == null) {
            return DrawablePixelsImpl.EMPTY;
        }
        return component.getDrawable();
    }

    @Override
    public void visit(Visitor checker) {
        if (component == null) {
            return;
        }
        component.visit(checker);
    }

    @Override
    public Iterator<Glyph> iterator() {
        if (component == null) {
            return Collections.emptyIterator();
        }
        return component.iterator();
    }

    protected abstract boolean isPositionOnComponent(Integer mouseX, Integer mouseY);

    protected abstract void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);

    protected abstract void onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);

    protected abstract void onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);
}
