package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public final class TextElementFactory {
    private TextElementFactory() {

    }

    public static TextElement fromChar(Glyph parent, ResourceManager storage, TextStructure parentStructure,
            Character c, TextFormat format, TextFormatEffect formatEffect) {
        TextElement response = null;
        if (c == '\n') {
            response = new TextNewLine(parent, storage, parentStructure, format, formatEffect);
        } else if (CharacterHelper.isPrintable(c)) {
            response = new TextChar(parent, storage, parentStructure, c, format, formatEffect);
        }

        return response;
    }

    public static TextElement fromChar(ResourceManager storage, Character c, TextFormat format,
            TextFormatEffect formatEffect) {
        return fromChar(null, storage, null, c, format, formatEffect);
    }

    public static TextElement genNewLineChar(Glyph parent, ResourceManager storage, TextStructure parentStructure,
            TextFormat format, TextFormatEffect formatEffect) {
        return new TextNewLine(parent, storage, parentStructure, format, formatEffect);
    }

    public static TextElement genNewLineChar(ResourceManager storage, TextFormat format,
            TextFormatEffect formatEffect) {
        return genNewLineChar(null, storage, null, format, formatEffect);
    }

    public static TextElement genNewSectionChar(Glyph parent, ResourceManager storage, TextStructure parentStructure,
            TextFormat format, TextFormatEffect formatEffect) {
        return new TextNewSection(parent, storage, parentStructure, format, formatEffect);
    }
}
