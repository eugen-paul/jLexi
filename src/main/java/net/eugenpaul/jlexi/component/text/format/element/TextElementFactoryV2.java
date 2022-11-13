package net.eugenpaul.jlexi.component.text.format.element;

import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextCharV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextNewLineV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextPageBreakV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public final class TextElementFactoryV2 {
    private TextElementFactoryV2() {

    }

    public static TextElementV2 fromChar(ResourceManager storage, TextStructureV2 parentStructure, Character c,
            TextFormat format, TextFormatEffect formatEffect) {
        TextElementV2 response = null;
        if (c == '\n') {
            response = new TextNewLineV2(storage, parentStructure, format, formatEffect);
        } else if (CharacterHelper.isPrintable(c)) {
            response = new TextCharV2(storage, parentStructure, c, format, formatEffect);
        }

        return response;
    }

    public static TextElementV2 fromChar(ResourceManager storage, Character c, TextFormat format,
            TextFormatEffect formatEffect) {
        return fromChar(storage, null, c, format, formatEffect);
    }

    public static TextElementV2 genNewLineChar(ResourceManager storage, TextStructureV2 parentStructure,
            TextFormat format, TextFormatEffect formatEffect) {
        return new TextNewLineV2(storage, parentStructure, format, formatEffect);
    }

    public static TextElementV2 genNewLineChar(ResourceManager storage, TextFormat format,
            TextFormatEffect formatEffect) {
        return genNewLineChar(storage, null, format, formatEffect);
    }

    public static TextElementV2 genNewSectionChar(ResourceManager storage, TextStructureV2 parentStructure,
            TextFormat format, TextFormatEffect formatEffect) {
        return new TextPageBreakV2(storage, parentStructure, format, formatEffect);
    }

    public static TextElementV2 genNewSectionChar(ResourceManager storage, TextFormat format,
            TextFormatEffect formatEffect) {
        return genNewSectionChar(storage, null, format, formatEffect);
    }
}
