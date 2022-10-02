package net.eugenpaul.jlexi.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public abstract class EventManager implements PropertyChangeListener {

    private final List<EventListner> listeners;

    /**
     * C*tor
     */
    protected EventManager() {
        this.listeners = new ArrayList<>();
    }

    /**
     * add/register view to controller.
     * 
     * @param listener
     */
    public void addListner(EventListner listener) {
        this.listeners.add(listener);
    }

    /**
     * remove vier from controller.
     * 
     * @param listener
     */
    public void removeListner(EventListner listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.listeners.forEach(v -> v.update(evt));
    }
}
