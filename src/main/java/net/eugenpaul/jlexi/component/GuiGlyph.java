package net.eugenpaul.jlexi.component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import net.eugenpaul.jlexi.window.action.KeyBindingAction;
import net.eugenpaul.jlexi.window.action.KeyBindingChildInputMap;
import net.eugenpaul.jlexi.window.action.KeyBindingChildInputMapImpl;
import net.eugenpaul.jlexi.window.action.KeyBindingRule;

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

    private Map<String, KeyBindingAction> defaultKeyBindings;

    @Getter
    protected String name;

    protected GuiGlyph(Glyph parent) {
        super(parent);
        this.name = null;
        this.guiChilds = new LinkedList<>();
        this.keyBindingMap = new KeyBindingChildInputMapImpl( //
                () -> guiChilds.stream()//
                        .map(GuiGlyph::getKeyBindingMap)//
                        .collect(Collectors.toList()) //
        );
        this.defaultKeyBindings = new HashMap<>();

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

    protected void addDefaultKeyBindings(String name, KeyBindingAction creator) {
        defaultKeyBindings.put(name, creator);
    }

    @Override
    public List<String> getDefaultKeyBindings() {
        return defaultKeyBindings.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public boolean registerDefaultKeyBindings(String name, KeyBindingRule rule, String keys) {
        var action = defaultKeyBindings.get(name);
        if (action == null) {
            return false;
        }
        getKeyBindingMap().addAction(name, keys, rule, action);

        return true;
    }

    @Override
    public void unregisterDefaultKeyBindings(String name) {
        getKeyBindingMap().removeAction(name);
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
