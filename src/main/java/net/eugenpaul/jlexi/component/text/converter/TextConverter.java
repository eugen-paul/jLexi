package net.eugenpaul.jlexi.component.text.converter;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

/**
 * Interface to convert text.
 */
public interface TextConverter {

    /**
     * Convert raw text to formatted text.
     * 
     * @param rawList input
     * @return formatted text
     */
    public List<TextElement> read(String file);

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     */
    public String write(List<TextElement> data);

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     */
    public String write(Iterator<TextElement> data);
}
