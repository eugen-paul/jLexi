package net.eugenpaul.jlexi.window;

import net.eugenpaul.jlexi.controller.WindowController;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.controller.ModelPropertyChangeListner;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;

/**
 * AbstractView to display Data.
 * <p>
 * This is a View of MVC
 */
public abstract class AbstractView implements ModelPropertyChangeListner {

    protected WindowController controller;

    /**
     * C'tor
     * 
     * @param controller Controller to send/recive actions to/from model.
     */
    protected AbstractView(WindowController controller) {
        this.controller = controller;
    }

    /**
     * Init the component.
     * 
     * @return true if the initialisation was successful.
     */
    public abstract boolean init();

    /**
     * Shows or hides this.
     * 
     * @param status - true show. false - hide.
     */
    public abstract void setVisible(boolean status);

    protected static boolean isType(PropertyChangeEvent evt, ViewPropertyChangeType type) {
        return evt.getPropertyName().equals(type.toString());
    }

}
