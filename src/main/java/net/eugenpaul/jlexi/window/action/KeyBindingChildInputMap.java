package net.eugenpaul.jlexi.window.action;

public interface KeyBindingChildInputMap {
    boolean addAction(String keys, KeyBindingRule rule, KeyBindingAction action);

    boolean removeAction(String keys, KeyBindingRule rule, KeyBindingAction action);
}
