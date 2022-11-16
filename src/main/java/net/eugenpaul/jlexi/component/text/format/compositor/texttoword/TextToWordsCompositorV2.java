package net.eugenpaul.jlexi.component.text.format.compositor.texttoword;

import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public interface TextToWordsCompositorV2 {
    <T extends TextStructureV2> List<TextWordV2> compose(ListIterator<T> iterator, ResourceManager storage);

    TextToWordsCompositorV2 copy();
}
