package net.eugenpaul.jlexi.component.text.converter;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
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
    public List<TextElement> read(String data) throws NotYetImplementedException, IllegalArgumentException;

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public String write(List<TextElement> data) throws NotYetImplementedException, IllegalArgumentException;

    /**
     * Convert formatted text to raw text.
     *
     * @param rawList input
     * @return data
     * @exception NotYetImplementedException
     * @exception IllegalArgumentException
     */
    public String write(Iterator<TextElement> data) throws NotYetImplementedException, IllegalArgumentException;
}
