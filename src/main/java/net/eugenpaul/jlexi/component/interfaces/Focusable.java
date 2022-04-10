package net.eugenpaul.jlexi.component.interfaces;

public interface Focusable {
    default boolean isFocusable() {
        return false;
    }
}
