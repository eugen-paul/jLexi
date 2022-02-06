package net.eugenpaul.jlexi.component.formatting;

import java.util.Iterator;
import java.util.List;

/**
 * Interface for Document formater
 * 
 * @param <T>
 */
public interface Compositor<T> {

    public default void compose(List<T> data){
        compose(data.iterator());
    }

    public void compose(Iterator<T> iterator);
}
