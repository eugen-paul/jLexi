package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.helper.KeyEventAdapterToKeyPressable;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawablePixelsImpl;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

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

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent(mouseX, mouseY)) {
            component.onMouseClick(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMouseClickOutsideComponent(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent(mouseX, mouseY)) {
            component.onMousePressed(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMousePressedOutsideComponent(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent(mouseX, mouseY)) {
            component.onMouseReleased(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMouseReleasedOutsideComponent(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        // TODO
        component.onMouseDragged(//
                mouseX - component.getRelativPosition().getX(), //
                mouseY - component.getRelativPosition().getY(), //
                button//
        );
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (isPositionOnComponent(mouseX, mouseY)) {
            component.onMouseWhellMoved(//
                    mouseX - component.getRelativPosition().getX(), //
                    mouseY - component.getRelativPosition().getY(), //
                    direction//
            );
        }
    }
}
