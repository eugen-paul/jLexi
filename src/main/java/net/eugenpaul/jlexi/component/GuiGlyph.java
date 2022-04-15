package net.eugenpaul.jlexi.component;

import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;

public abstract class GuiGlyph extends Glyph implements GuiEvents {

    @Setter
    protected MouseEventAdapter mouseEventAdapter;
    @Setter
    protected KeyEventAdapter keyEventAdapter;

    protected GuiGlyph(Glyph parent) {
        super(parent);
        this.mouseEventAdapter = new MouseEventAdapter() {

        };
        this.keyEventAdapter = new KeyEventAdapter() {

        };
    }

    @Override
    public void onMouseClick(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMousePressed(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mousePressed(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseReleased(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseReleased(mouseX, mouseY, button);
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
