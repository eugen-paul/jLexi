package net.eugenpaul.jlexi.component.interfaces;

import java.util.List;

import net.eugenpaul.jlexi.window.action.KeyBindingAction;
import net.eugenpaul.jlexi.window.action.KeyBindingRule;

public interface KeyBindingable {

    List<String> getDefaultKeyBindings();

    KeyBindingAction getDefaultKeyBinding(String name);

    boolean registerDefaultKeyBindings(String name, KeyBindingRule rule, String keys);

    void unregisterDefaultKeyBindings(String name);
}
