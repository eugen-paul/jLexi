package net.eugenpaul.jlexi.component.text.converter;

import net.eugenpaul.jlexi.exception.NotYetImplementedException;

/**
 * Interface to convert text.
 */
public interface TextConverterV2 {

    /**
     * Convert raw text to formatted text.
     * 
     * @param rawList input
     * @return formatted text
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public TextDataV2 read(String data) throws NotYetImplementedException, IllegalArgumentException;

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public String write(TextDataV2 data) throws NotYetImplementedException, IllegalArgumentException;
}
