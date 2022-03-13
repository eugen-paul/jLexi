package net.eugenpaul.jlexi.resourcesmanager.textformatter;

import lombok.Getter;

public enum FormatterType {
    UNDERLINE_SINGLE(Integer.class, "underline"), //
    UNDERLINE_DOUBLE(Integer.class, "underline"),//
    ;

    @Getter
    private Class<?> clazz;
    @Getter
    private String function;

    private FormatterType(Class<?> clazz, String function) {
        this.clazz = clazz;
        this.function = function;
    }
}
