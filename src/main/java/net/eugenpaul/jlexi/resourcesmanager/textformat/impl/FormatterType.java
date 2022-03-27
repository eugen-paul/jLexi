package net.eugenpaul.jlexi.resourcesmanager.textformat.impl;

import lombok.Getter;
import net.eugenpaul.jlexi.resourcesmanager.textformat.FormatterCreator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderline;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;
import net.eugenpaul.jlexi.utils.Color;

public enum FormatterType {
    UNDERLINE_SINGLE(v -> new FormatUnderline(FormatUnderlineType.SINGLE, (Color) v)), //
    UNDERLINE_DOUBLE(v -> new FormatUnderline(FormatUnderlineType.DOUBLE, (Color) v)), //
    ;

    @Getter
    private FormatterCreator creator;

    private FormatterType(FormatterCreator creator) {
        this.creator = creator;
    }
}
