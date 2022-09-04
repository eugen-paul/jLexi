package net.eugenpaul.jlexi.component.text.converter;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.exception.UnsupportedException;

/**
 * Interface to read/write text from/to Clipboard.
 */
public interface ClipboardConverter {

    /**
     * Read data from Clipboard. If HTML data is avaible, then HTML data will be read. Otherwise plain text data will be
     * read.
     * 
     * @return formatted text
     * @exception NotYetImplementedException
     */
    public List<TextElement> read() throws NotYetImplementedException;

    /**
     * Read HTML data from Clipboard.
     * 
     * @return formatted text
     * @exception NotYetImplementedException
     * @exception UnsupportedException
     */
    public List<TextElement> readHtml() throws NotYetImplementedException, UnsupportedException;

    /**
     * Read Plain-Text data from Clipboard.
     * 
     * @return formatted text
     * @exception NotYetImplementedException
     * @exception UnsupportedException
     */
    public List<TextElement> readPlain() throws NotYetImplementedException, UnsupportedException;

    public void write(List<TextElement> data) throws NotYetImplementedException;
}
