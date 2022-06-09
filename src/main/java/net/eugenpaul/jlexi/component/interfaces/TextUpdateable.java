package net.eugenpaul.jlexi.component.interfaces;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.structure.TextSection;

public interface TextUpdateable {

    /**
     * Set text to element. Don't call this function directly.
     * 
     * @param text
     */
    public void setText(List<TextSection> text);
}
