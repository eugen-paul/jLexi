package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.component.interfaces.GuiComponent;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;

/**
 * Abstract Class for Gui-Glyph that cann have just one GuiComponent-Child
 * 
 */
public abstract class GuiCompenentMonoGlyph extends GuiGlyph {

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

    protected abstract boolean isPositionOnComponent(Integer mouseX, Integer mouseY);

    protected abstract void onMouseClickOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);

    protected abstract void onMousePressedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);

    protected abstract void onMouseReleasedOutsideComponent(Integer mouseX, Integer mouseY, MouseButton button);

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (isPositionOnComponent(mouseX, mouseY)) {
            component.onMouseClick(//
                    mouseX - component.getGlyph().getRelativPosition().getX(), //
                    mouseY - component.getGlyph().getRelativPosition().getY(), //
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
                    mouseX - component.getGlyph().getRelativPosition().getX(), //
                    mouseY - component.getGlyph().getRelativPosition().getY(), //
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
                    mouseX - component.getGlyph().getRelativPosition().getX(), //
                    mouseY - component.getGlyph().getRelativPosition().getY(), //
                    button//
            );
        } else {
            onMouseReleasedOutsideComponent(mouseX, mouseY, button);
        }
    }

    @Override
    public void onKeyTyped(Character key) {
        component.onKeyTyped(key);
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        component.onKeyPressed(keyCode);
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        component.onKeyReleased(keyCode);
    }
}
