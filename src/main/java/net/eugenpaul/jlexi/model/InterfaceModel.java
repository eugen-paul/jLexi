package net.eugenpaul.jlexi.model;

import java.beans.PropertyChangeListener;

/**
 * Interface for Model-Objects of MVC. This objects that can be notified of the changes on the view.
 */
public interface InterfaceModel {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
