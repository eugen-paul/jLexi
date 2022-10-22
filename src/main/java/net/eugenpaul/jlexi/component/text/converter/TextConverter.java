package net.eugenpaul.jlexi.component.text.converter;

import net.eugenpaul.jlexi.exception.NotYetImplementedException;

/**
 * Interface to convert text.
 */
public interface TextConverter {

    /**
     * Convert raw text to formatted text.
     * 
     * @param rawList input
     * @return formatted text
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public TextData read(String data) throws NotYetImplementedException, IllegalArgumentException;

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public String write(TextData data) throws NotYetImplementedException, IllegalArgumentException;
}
