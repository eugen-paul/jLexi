package net.eugenpaul.jlexi.controller;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum ViewPropertyChangeType {
    SET_MAIN_TITLE("SET_MAIN_TITLE"), //
    TRIGGER_FULL_DRAW("TRIGGER_FULL_DRAW"), //
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
