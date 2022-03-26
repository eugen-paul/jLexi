package net.eugenpaul.jlexi.resourcesmanager.textformat.impl;

import lombok.Getter;
import net.eugenpaul.jlexi.resourcesmanager.textformat.FormatterCreator;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderline;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public enum FormatterType {
    UNDERLINE_SINGLE(() -> new FormatUnderline(FormatUnderlineType.SINGLE)), //
    UNDERLINE_DOUBLE(() -> new FormatUnderline(FormatUnderlineType.DOUBLE)), //
    ;

    @Getter
    private FormatterCreator creator;

    private FormatterType(FormatterCreator creator) {
        this.creator = creator;
    }
}
