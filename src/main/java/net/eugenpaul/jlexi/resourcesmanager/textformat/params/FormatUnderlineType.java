package net.eugenpaul.jlexi.resourcesmanager.textformat.params;

import lombok.Getter;

public enum FormatUnderlineType {
    NONE("NONE"), //
    SINGLE("SINGLE"), //
    DOUBLE("DOUBLE"),//
    ;

    @Getter
    private String value;

    private FormatUnderlineType(String value) {
        this.value = value;
    }
}
