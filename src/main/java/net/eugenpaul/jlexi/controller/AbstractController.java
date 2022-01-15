package net.eugenpaul.jlexi.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.model.InterfaceModel;

/**
 * AbstractController to control data transfer between View and Model. It translates user's interactions with the view
 * into actions that the model will perform and sends model's changes to view.
 * <p>
 * This is a View of MVC
 */
public abstract class AbstractController implements PropertyChangeListener {
    private List<AbstractPanel> registeredViews;
    private List<InterfaceModel> registeredModels;

    /**
     * C*tor
     */
    protected AbstractController() {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();
    }

    /**
     * add/register view to controller.
     * 
     * @param view
     */
    public void addView(AbstractPanel view) {
        registeredViews.add(view);
    }

    /**
     * remove vier from controller.
     * 
     * @param view
     */
    public void removeView(AbstractPanel view) {
        registeredViews.remove(view);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        registeredViews.forEach(v -> v.modelPropertyChange(evt));
    }

    public void addModel(InterfaceModel model) {
        registeredModels.add(model);
    }

    /**
     * This is a convenience method that subclasses can call upon to fire property changes back to the models. This
     * method uses reflection to inspect each of the model classes to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown, which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue     = An object that represents the new value of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {

        for (InterfaceModel model : registeredModels) {
            try {
                Method method = model.getClass().getMethod("set" + propertyName, newValue.getClass());
                method.invoke(model, newValue);

            } catch (Exception ex) {
                // Handle exception.
            }
        }
    }
}
