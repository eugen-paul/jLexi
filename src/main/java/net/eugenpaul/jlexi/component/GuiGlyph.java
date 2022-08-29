package net.eugenpaul.jlexi.component;

import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseDragAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public abstract class GuiGlyph extends Glyph implements GuiEvents {

    @Setter
    protected MouseEventAdapter mouseEventAdapter;
    @Setter
    protected KeyEventAdapter keyEventAdapter;
    @Setter
    protected MouseDragAdapter mouseDragAdapter;

    protected GuiGlyph(Glyph parent) {
        super(parent);
        this.mouseEventAdapter = new MouseEventAdapter() {

        };
        this.keyEventAdapter = new KeyEventAdapter() {

        };
        this.mouseDragAdapter = new MouseDragAdapter() {

        };
    }

    @Override
    public void redraw() {
        this.cachedDrawable = null;

        if (this.parent == null) {
            return;
        }

        this.parent.redraw();
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public MouseDraggable onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            return mouseEventAdapter.mousePressed(mouseX, mouseY, button);
        }
        return null;
    }

    @Override
    public MouseDraggable onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            return mouseEventAdapter.mouseReleased(mouseX, mouseY, button);
        }
        return null;
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseWhellMoved(mouseX, mouseY, direction);
        }
    }

    @Override
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseDragAdapter != null) {
            mouseDragAdapter.mouseDragged(mouseX, mouseY, button);
        }
    }

    @Override
    public void onKeyTyped(Character key) {
        if (keyEventAdapter != null) {
            keyEventAdapter.keyTyped(key);
        }
    }

    @Override
    public void onKeyPressed(KeyCode keyCode) {
        if (keyEventAdapter != null) {
            keyEventAdapter.keyPressed(keyCode);
        }
    }

    @Override
    public void onKeyReleased(KeyCode keyCode) {
        if (keyEventAdapter != null) {
            keyEventAdapter.keyReleased(keyCode);
        }
    }

}
