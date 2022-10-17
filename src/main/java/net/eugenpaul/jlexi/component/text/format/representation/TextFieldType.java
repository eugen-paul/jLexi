package net.eugenpaul.jlexi.component.text.format.representation;

public enum TextFieldType {
    UNKNOWN, //
    HEADER, //
    BODY, //
    FOOTER, //
    ;

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
