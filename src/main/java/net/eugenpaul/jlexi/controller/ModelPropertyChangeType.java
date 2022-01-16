package net.eugenpaul.jlexi.controller;

import lombok.Getter;
import net.eugenpaul.jlexi.data.framing.Resizeable;

public enum ModelPropertyChangeType {
    FORM_RESIZE(Resizeable.class, "setSize");

    @Getter
    private final Object targetClass;
    @Getter
    private final String methode;

    private ModelPropertyChangeType(Object targetClass, String methode) {
        this.targetClass = targetClass;
        this.methode = methode;
    }
}
