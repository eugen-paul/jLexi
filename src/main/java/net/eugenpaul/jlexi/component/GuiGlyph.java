package net.eugenpaul.jlexi.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Setter;
import net.eugenpaul.jlexi.component.interfaces.GuiEvents;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.design.listener.MouseEventAdapter;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import net.eugenpaul.jlexi.utils.event.MouseWheelDirection;

public abstract class GuiGlyph extends Glyph implements GuiEvents {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiGlyph.class);

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
    public void redraw() {
        if (parent == null) {
            return;
        }

        cachedDrawable = null;
        parent.redraw();
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
    public void onMouseDragged(Integer mouseX, Integer mouseY, MouseButton button) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseDragged(mouseX, mouseY, button);
        }
    }

    @Override
    public void onMouseWhellMoved(Integer mouseX, Integer mouseY, MouseWheelDirection direction) {
        if (mouseEventAdapter != null) {
            mouseEventAdapter.mouseWhellMoved(mouseX, mouseY, direction);
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
