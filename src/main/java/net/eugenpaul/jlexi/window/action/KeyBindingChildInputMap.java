package net.eugenpaul.jlexi.window.action;

import java.util.List;

public interface KeyBindingChildInputMap {
    void addAction(String name, String keys, KeyBindingRule rule, KeyBindingAction action);

    void removeAction(String name);

    List<KeyBindingAction> getCurrentByKeyAction(String keys);

    boolean isKeysSets(String keys);

    List<String> getAllKeys();

    List<KeyBindingAction> getAllByKeyActions(String keys);
}
