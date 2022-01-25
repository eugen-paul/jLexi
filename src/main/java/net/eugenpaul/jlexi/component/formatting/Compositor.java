package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.utils.container.NodeList;

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
    public List<T> compose(Iterator<T> iterator, final int width);

    /**
     * Do format
     */
    public NodeList<T> composeToNodeList(Iterator<T> iterator, final int width);
}
