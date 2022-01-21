package net.eugenpaul.jlexi.data.formatting;

import java.util.List;

import net.eugenpaul.jlexi.data.iterator.GlyphIterator;

/**
 * Interface for Document formater
 * 
 * @param <T>
 */
public interface Compositor<T> {

    /**
     * Do format
     */
    public List<T> compose(List<T> data, final int width);

    /**
     * Do format
     */
    public List<T> compose(GlyphIterator iterator, final int width);
}
