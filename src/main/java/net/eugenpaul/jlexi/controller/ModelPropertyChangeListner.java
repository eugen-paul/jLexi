package net.eugenpaul.jlexi.controller;

import java.beans.PropertyChangeEvent;

public interface ModelPropertyChangeListner {
    /**
     * callback function, that will be called by changes in model.
     * 
     * @param evt
     */
    public abstract void modelPropertyChange(final PropertyChangeEvent evt);
}
