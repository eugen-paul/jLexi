package net.eugenpaul.jlexi.component.interfaces;

import net.eugenpaul.jlexi.component.text.converter.TextData;

public interface TextUpdateable {

    /**
     * Set text to element. Don't call this function directly.
     * 
     * @param text
     */
    public void setText(TextData text);
}
