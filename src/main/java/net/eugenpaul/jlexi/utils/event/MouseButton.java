package net.eugenpaul.jlexi.utils.event;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * Mouse Mapping for internal use.
 */
public enum MouseButton {
    NOBUTTON(0), //
    BUTTON_LEFT(1), //
    BUTTON_MIDDLE(2), //
    BUTTON_RIGHT(3);

    @Getter
    private int mb;

    private static final Map<Integer, MouseButton> map = Stream.of(MouseButton.values())
            .collect(Collectors.toMap(MouseButton::getMb, v -> v));

    private MouseButton(int mb) {
        this.mb = mb;
    }

    /**
     * Get Mousebutton from Integer
     * 
     * @param button
     * @return Mousebutton
     */
    public static MouseButton ofButton(int button) {
        return map.get(button);
    }
}
