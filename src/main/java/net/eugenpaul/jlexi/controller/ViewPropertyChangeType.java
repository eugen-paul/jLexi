package net.eugenpaul.jlexi.controller;

import lombok.Getter;

public enum ViewPropertyChangeType {
    SET_TITLE("SET_TITLE"), //
    TRIGGER_FULL_DRAW("TRIGGER_FULL_DRAW"), //
    TRIGGER_AREA_DRAW("TRIGGER_AREA_DRAW"), //
    DRAW_AREA("DRAW_AREA");

    @Getter
    private String typeName;

    private ViewPropertyChangeType(String typeName) {
        this.typeName = typeName;
    }

}
