package net.eugenpaul.jlexi.component.helper;

import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.design.listener.KeyEventAdapter;
import net.eugenpaul.jlexi.utils.event.KeyCode;

public class KeyEventAdapterToKeyPressable implements KeyEventAdapter {

    private KeyPressable target;

    public KeyEventAdapterToKeyPressable(KeyPressable target) {
        this.target = target;
    }

    @Override
    public void keyTyped(Character key) {
        target.onKeyTyped(key);
    }

    @Override
    public void keyPressed(KeyCode keyCode) {
        target.onKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(KeyCode keyCode) {
        target.onKeyReleased(keyCode);
    }
}
