package net.eugenpaul.jlexi.component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.component.interfaces.MouseDraggable;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseDragAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;
import net.eugenpaul.jlexi.window.action.KeyBindingChildInputMap;
import net.eugenpaul.jlexi.window.action.KeyBindingChildInputMapImpl;

public abstract class GuiGlyph extends Glyph implements GuiEvents {

    @Setter
    protected MouseEventAdapter mouseEventAdapter;
    @Setter
    protected KeyEventAdapter keyEventAdapter;
    @Setter
    protected MouseDragAdapter mouseDragAdapter;

    private List<GuiGlyph> guiChilds;

    @Getter
    private KeyBindingChildInputMap keyBindingMap;

    protected GuiGlyph(Glyph parent) {
        super(parent);
        this.guiChilds = new LinkedList<>();
        this.keyBindingMap = new KeyBindingChildInputMapImpl( //
                () -> guiChilds.stream()//
                        .map(GuiGlyph::getKeyBindingMap)//
                        .collect(Collectors.toList()) //
        );

        this.mouseEventAdapter = new MouseEventAdapter() {

        };
        this.keyEventAdapter = new KeyEventAdapter() {

        };
        this.mouseDragAdapter = new MouseDragAdapter() {

        };
    }

    protected void addGuiChild(GuiGlyph child) {
        this.guiChilds.add(child);
    }

    protected boolean removeGuiChild(GuiGlyph child) {
        return this.guiChilds.remove(child);
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
