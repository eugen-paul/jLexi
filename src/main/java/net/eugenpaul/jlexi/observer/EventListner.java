package net.eugenpaul.jlexi.observer;

import java.beans.PropertyChangeEvent;

public interface EventListner {
    /**
     * callback function, that will be called by changes in model.
     * 
     * @param evt
     */
    public abstract void update(final PropertyChangeEvent evt);
}
