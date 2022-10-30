package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TextFieldType {
    UNKNOWN(false), //
    HEADER(true), //
    BODY(false), //
    FOOTER(true), //
    ;

    @Getter
    private boolean locked;

    public boolean isSame(TextFieldType other) {
        switch (this) {
        case BODY:
            return other == BODY;
        case HEADER, FOOTER:
            return (other == HEADER) || (other == FOOTER);
        default:
            break;
        }
        return false;
    }
}
