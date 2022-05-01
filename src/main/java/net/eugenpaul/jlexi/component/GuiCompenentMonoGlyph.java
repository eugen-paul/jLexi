package net.eugenpaul.jlexi.component;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.visitor.Visitor;
import net.eugenpaul.jlexi.draw.DrawableV2;
import net.eugenpaul.jlexi.draw.DrawableV2PixelsImpl;
import net.eugenpaul.jlexi.utils.event.KeyCode;
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
    }

    @Override
    public DrawableV2 getDrawable() {
        if (component == null) {
            return DrawableV2PixelsImpl.EMPTY;
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
