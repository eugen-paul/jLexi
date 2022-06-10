package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TextAlignment {
    LEFT("left"), //
    RIGHT("right"), //
    BLOCK("block"), //
    ;

    private static final Map<String, TextAlignment> stringToEnum = Stream.of(TextAlignment.values())
            .collect(Collectors.toMap(v -> v.getValue(), v -> v));

    @Getter
    private final String value;

    public static TextAlignment fromString(String data) {
        return stringToEnum.get(data.toLowerCase());
    }
}
