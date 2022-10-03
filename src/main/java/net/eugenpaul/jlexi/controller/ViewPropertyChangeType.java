package net.eugenpaul.jlexi.controller;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum ViewPropertyChangeType {
    SET_TITLE("SET_TITLE"), //
    TRIGGER_FULL_DRAW("TRIGGER_FULL_DRAW"), //
    // TRIGGER_AREA_DRAW("TRIGGER_AREA_DRAW"), //
    // DRAW_AREA("DRAW_AREA"), //
    CURSOR_MOVE("CURSOR_MOVE"), //
    CURSOR_SET_FORMAT_BOLD("CURSOR_SET_FORMAT_BOLD"), //
    CURSOR_SET_FORMAT_ITALIC("CURSOR_SET_FORMAT_ITALIC"), //
    ;

    private static final Map<String, ViewPropertyChangeType> nameToEnum = Stream.of(ViewPropertyChangeType.values())
            .collect(Collectors.toMap(ViewPropertyChangeType::getTypeName, v -> v));

    @Getter
    private final String typeName;

    private ViewPropertyChangeType(String typeName) {
        this.typeName = typeName;
    }

    public static ViewPropertyChangeType fromValue(String name) {
        return nameToEnum.get(name);
    }

    @Override
    public String toString() {
        return this.typeName;
    }

}
