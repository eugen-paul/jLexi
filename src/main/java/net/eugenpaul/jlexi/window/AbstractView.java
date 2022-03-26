package net.eugenpaul.jlexi.window;

import java.beans.PropertyChangeEvent;

import net.eugenpaul.jlexi.controller.AbstractController;

/**
 * AbstractView to display Data.
 * <p>
 * This is a View of MVC
 */
public abstract class AbstractView {

    protected AbstractController controller;

    /**
     * C'tor
     * 
     * @param controller Controller to send/recive actions to/from model.
     */
    protected AbstractView(AbstractController controller) {
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

    /**
     * callback function, that will be called by changes in model.
     * 
     * @param evt
     */
    public abstract void modelPropertyChange(final PropertyChangeEvent evt);
}
