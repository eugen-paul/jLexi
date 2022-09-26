package net.eugenpaul.jlexi.window.action;

public interface KeyBindingChildInputMap {
    void addAction(String keys, KeyBindingRule rule, KeyBindingAction action);

    void removeAction(String keys, KeyBindingRule rule, KeyBindingAction action);

    KeyBindingAction getAction(String keys);

    boolean isKeysSets(String keys);
}
