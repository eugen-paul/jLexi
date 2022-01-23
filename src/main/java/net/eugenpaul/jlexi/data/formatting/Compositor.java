package net.eugenpaul.jlexi.data.formatting;

import java.util.List;

import net.eugenpaul.jlexi.data.iterator.GlyphIteratorGen;

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
    public List<T> compose(GlyphIteratorGen<T> iterator, final int width);
}
