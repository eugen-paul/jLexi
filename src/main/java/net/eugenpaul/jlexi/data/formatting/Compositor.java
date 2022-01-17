package net.eugenpaul.jlexi.data.formatting;

import java.util.List;

/**
 * Interface for Document formater
 * 
 * @param <T>
 */
public interface Compositor<T> {

    /**
     * set Objects that will be formatet
     * 
     * @param composition
     */
    public void setComposition(Composition<T> composition);

    /**
     * Do format
     */
    public List<T> compose(List<T> data, final int width);
}
