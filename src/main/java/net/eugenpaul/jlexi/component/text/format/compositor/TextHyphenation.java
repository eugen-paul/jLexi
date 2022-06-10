package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TextHyphenation {
    LETTER("letter"), //
    SYLLABLE("syllable"), //
    WORD("word"), //
    ;

    private static final Map<String, TextHyphenation> stringToEnum = Stream.of(TextHyphenation.values())
            .collect(Collectors.toMap(v -> v.getValue(), v -> v));

    @Getter
    private final String value;

    public static TextHyphenation fromString(String data) {
        return stringToEnum.get(data.toLowerCase());
    }
}
