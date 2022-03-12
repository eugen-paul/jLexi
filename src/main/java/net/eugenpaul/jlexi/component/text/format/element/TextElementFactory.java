package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public final class TextElementFactory {
    private TextElementFactory() {

    }

    public static TextElement fromChar(Glyph parent, ResourceManager storage, TextStructure parentStructure,
            Character c, TextFormat format) {
        TextElement response = null;
        if (c == '\n') {
            response = new TextNewLine(parent, storage, parentStructure, format);
        } else if (CharacterHelper.isPrintable(c)) {
            response = new TextChar(parent, storage, parentStructure, c, format);
        }

        return response;
    }

    public static TextElement genNewLineChar(Glyph parent, ResourceManager storage, TextStructure parentStructure,
            TextFormat format) {
        return new TextNewLine(parent, storage, parentStructure, format);
    }
}
