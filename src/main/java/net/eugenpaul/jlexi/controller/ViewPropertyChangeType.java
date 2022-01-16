package net.eugenpaul.jlexi.controller;

import lombok.Getter;

public enum ViewPropertyChangeType {
    UPDATE("UPDATE");

    @Getter
    private String typeName;

    private ViewPropertyChangeType(String typeName) {
        this.typeName = typeName;
    }

}
