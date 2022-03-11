package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.field.TextField;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public final class TextElementFactory {
    private TextElementFactory() {

    }

    public static TextElement fromChar(Glyph parent, ResourceManager storage, TextField parentTextField, Character c) {
        TextElement response = null;
        if (c == '\n') {
            response = new TextNewLine(parent, storage, parentTextField);
        } else if (CharacterHelper.isPrintable(c)) {
            response = new TextChar(parent, storage, parentTextField, c);
        }

        return response;
    }

    public static TextElement genNewLineChar(Glyph parent, ResourceManager storage, TextField parentTextField) {
        return new TextNewLine(parent, storage, parentTextField);
    }
}
