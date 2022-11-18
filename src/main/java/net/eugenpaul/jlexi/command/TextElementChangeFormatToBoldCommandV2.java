package net.eugenpaul.jlexi.command;

import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructureV2;

public class TextElementChangeFormatToBoldCommandV2 extends TextElementChangeFormatCommandV2 {

    public TextElementChangeFormatToBoldCommandV2(TextStructureV2 selectedText) {
        super(selectedText);
    }

    @Override
    protected void setNewFormat(TextElementV2 element) {
        var format = element.getFormat();
        element.updateFormat(format.withBold(true));
    }
}
