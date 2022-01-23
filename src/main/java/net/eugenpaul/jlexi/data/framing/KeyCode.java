package net.eugenpaul.jlexi.data.framing;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum KeyCode {
    SHIFT(1), //
    ALT(2), //
    CTRL(3), //
    UP(4), //
    DOWN(5), //
    LEFT(6), //
    RIGHT(7), //
    DELETE(8), //
    BACK_SPACE(9), //
    ENTER(10), //
    ;

    @Getter
    private final int code;

    private static final Map<Integer, KeyCode> codeToKey = Stream.of(KeyCode.values())
            .collect(Collectors.toMap(KeyCode::getCode, v -> v));

    private KeyCode(int code) {
        this.code = code;
    }

    public static KeyCode ofCode(int code) {
        return codeToKey.get(code);
    }
}
