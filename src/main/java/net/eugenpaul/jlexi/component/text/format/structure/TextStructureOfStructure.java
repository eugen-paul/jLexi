package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public abstract class TextStructureOfStructure extends TextStructure {

    protected TextStructureOfStructure(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
    }

}
