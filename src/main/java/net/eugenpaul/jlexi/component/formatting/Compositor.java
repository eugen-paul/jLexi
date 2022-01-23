package net.eugenpaul.jlexi.component.formatting;

import java.util.List;

import net.eugenpaul.jlexi.component.iterator.GlyphIteratorGen;

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
