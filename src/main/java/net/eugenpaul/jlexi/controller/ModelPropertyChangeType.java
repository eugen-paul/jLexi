package net.eugenpaul.jlexi.controller;

import java.time.Duration;

import lombok.Getter;
import net.eugenpaul.jlexi.data.framing.KeyPressable;
import net.eugenpaul.jlexi.data.framing.MouseClickable;
import net.eugenpaul.jlexi.data.framing.Resizeable;

public enum ModelPropertyChangeType {
    FORM_RESIZE(Resizeable.class, "resizeTo", Duration.ofMillis(50)), //
    MOUSE_CLICK(MouseClickable.class, "onMouseClick", Duration.ZERO), //
    KEY_PRESSED(KeyPressable.class, "onKeyPressed", Duration.ZERO), //
    ;

    @Getter
    private final Object targetClass;
    @Getter
    private final String methode;
    @Getter
    private Duration delay;

    private ModelPropertyChangeType(Object targetClass, String methode, Duration delay) {
        this.targetClass = targetClass;
        this.methode = methode;
        this.delay = delay;
    }
}
