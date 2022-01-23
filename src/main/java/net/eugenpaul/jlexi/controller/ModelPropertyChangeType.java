package net.eugenpaul.jlexi.controller;

import java.time.Duration;

import lombok.Getter;
import net.eugenpaul.jlexi.component.interfaces.KeyPressable;
import net.eugenpaul.jlexi.component.interfaces.MouseClickable;
import net.eugenpaul.jlexi.component.interfaces.Resizeable;

public enum ModelPropertyChangeType {
    FORM_RESIZE(Resizeable.class, "resizeTo", Duration.ofMillis(50)), //
    MOUSE_CLICK(MouseClickable.class, "onMouseClick", Duration.ZERO), //
    KEY_TYPED(KeyPressable.class, "onKeyTyped", Duration.ZERO), //
    KEY_PRESSED(KeyPressable.class, "onKeyPressed", Duration.ZERO), //
    KEY_RELEASED(KeyPressable.class, "onKeyReleased", Duration.ZERO), //
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
