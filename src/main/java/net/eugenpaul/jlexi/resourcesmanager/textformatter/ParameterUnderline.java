package net.eugenpaul.jlexi.resourcesmanager.textformatter;

import lombok.Getter;

public enum ParameterUnderline implements FormatterTypeParameter<UnderlineType> {
    UNDERLINE(UnderlineType.class, "underline"), //
    ;

    @Getter
    private Class<UnderlineType> clazz;
    @Getter
    private String function;

    private ParameterUnderline(Class<UnderlineType> clazz, String function) {
        this.clazz = clazz;
        this.function = function;
    }
}
