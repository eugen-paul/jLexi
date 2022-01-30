package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import net.eugenpaul.jlexi.utils.event.KeyCode;

public enum CursorMove {
    UP(KeyCode.UP), //
    DOWN(KeyCode.DOWN), //
    LEFT(KeyCode.LEFT), //
    RIGHT(KeyCode.RIGHT), //
    ;

    @Getter
    private KeyCode keyCode;

    private static final Map<KeyCode, CursorMove> keyCodeToCursorMove = Stream.of(CursorMove.values())
            .collect(Collectors.toMap(v -> v.getKeyCode(), v -> v));

    private CursorMove(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public static CursorMove fromKeyCode(KeyCode keyCode) {
        return keyCodeToCursorMove.get(keyCode);
    }

}
