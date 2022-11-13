package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.component.text.converter.TextDataV2;

public interface TextUpdateableV2 {

    /**
     * Set text to element. Don't call this function directly.
     * 
     * @param text
     */
    public void setText(TextDataV2 text);
}
