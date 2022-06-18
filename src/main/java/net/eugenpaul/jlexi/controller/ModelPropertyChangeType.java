package net.eugenpaul.jlexi.controller;

import java.time.Duration;

import lombok.Getter;
import net.eugenpaul.jlexi.window.interfaces.WindowsKeyPressable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseClickable;
import net.eugenpaul.jlexi.window.interfaces.WindowsMouseWheel;
import net.eugenpaul.jlexi.window.interfaces.WindowsResizeable;

public enum ModelPropertyChangeType {
    FORM_RESIZE(WindowsResizeable.class, "resizeTo", Duration.ofMillis(50)), //
    MOUSE_CLICK(WindowsMouseClickable.class, "onMouseClick", Duration.ZERO), //
    MOUSE_PRESSED(WindowsMouseClickable.class, "onMousePressed", Duration.ZERO), //
    MOUSE_RELEASED(WindowsMouseClickable.class, "onMouseReleased", Duration.ZERO), //
    MOUSE_WHEEL(WindowsMouseWheel.class, "onMouseWheelMooved", Duration.ZERO), //
    MOUSE_DRAGGED(WindowsMouseClickable.class, "onMouseDragged", Duration.ZERO), //
    KEY_TYPED(WindowsKeyPressable.class, "onKeyTyped", Duration.ZERO), //
    KEY_PRESSED(WindowsKeyPressable.class, "onKeyPressed", Duration.ZERO), //
    KEY_RELEASED(WindowsKeyPressable.class, "onKeyReleased", Duration.ZERO), //
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
