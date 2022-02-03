package net.eugenpaul.jlexi.controller;

import lombok.Getter;

public enum ViewPropertyChangeType {
    UPDATE("UPDATE"), //
    REDRAW("REDRAW"), //
    DO_REDRAW("DO_REDRAW");

    @Getter
    private String typeName;

    private ViewPropertyChangeType(String typeName) {
        this.typeName = typeName;
    }

}
