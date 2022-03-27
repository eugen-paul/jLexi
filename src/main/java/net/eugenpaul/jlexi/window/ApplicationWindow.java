package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.controller.ModelController;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;

public abstract class ApplicationWindow extends Window {

    protected ApplicationWindow(String name, ModelController controller, Size size) {
        super(//
                name, //
                size, //
                factory.createApplicationWindow().apply(controller), //
                controller //
        );

        this.focusOn = null;
    }

    @Override
    public void onKeyTyped(String name, Character key) {
        if (focusOn != null) {
            focusOn.onKeyTyped(name, key);
        }
    }

    @Override
    public void onKeyPressed(String name, KeyCode keyCode) {
        if (focusOn != null) {
            focusOn.onKeyPressed(name, keyCode);
        }
    }

    @Override
    public void onKeyReleased(String name, KeyCode keyCode) {
        if (focusOn != null) {
            focusOn.onKeyReleased(name, keyCode);
        }

    }

    @Override
    public void onMouseClick(String name, Integer mouseX, Integer mouseY, MouseButton button) {
        if (mainGlyph != null) {
            mainGlyph.onMouseClick(name, mouseX, mouseY, button);
        }
    }

    @Override
    public void resizeTo(String name, Size size) {
        if (mainGlyph != null) {
            mainGlyph.resizeTo(name, size);
        }
    }

}
