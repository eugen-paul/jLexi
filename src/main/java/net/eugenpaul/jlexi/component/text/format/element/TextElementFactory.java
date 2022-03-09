package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public final class TextElementFactory {
    private TextElementFactory() {

    }

    public static TextElement fromChar(Glyph parent, FontStorage fontStorage, TextField parentTextField, Character c) {
        TextElement response = null;
        if (c == '\n') {
            response = new TextNewLine(parent, fontStorage, parentTextField);
        } else if (CharacterHelper.isPrintable(c)) {
            response = new TextChar(parent, fontStorage, parentTextField, c);
        }

        return response;
    }

    public static TextElement genNewLineChar(Glyph parent, FontStorage fontStorage, TextField parentTextField) {
        return new TextNewLine(parent, fontStorage, parentTextField);
    }
}
