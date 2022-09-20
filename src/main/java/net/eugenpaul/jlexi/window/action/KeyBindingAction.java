package net.eugenpaul.jlexi.window.action;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import lombok.Getter;

public abstract class KeyBindingAction {
    @Getter
    protected Object action;
    private PropertyChangeSupport propertyChangeSupport;

    public synchronized void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public synchronized void removeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            return;
        }
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public abstract void doAction();

}
