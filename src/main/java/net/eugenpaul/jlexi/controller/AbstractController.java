package net.eugenpaul.jlexi.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.model.InterfaceModel;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * AbstractController to control data transfer between View and Model. It translates user's interactions with the view
 * into actions that the model will perform and sends model's changes to view.
 * <p>
 * This is a View of MVC
 */
@Slf4j
public abstract class AbstractController implements PropertyChangeListener {

    private List<ModelPropertyChangeListner> registeredViews;
    private List<InterfaceModel> registeredModels;

    protected Scheduler modelScheduler;

    private Map<ModelPropertyChangeType, Disposable> modelPropChangeMap;

    /**
     * C*tor
     */
    protected AbstractController(ExecutorService pool) {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();

        modelScheduler = Schedulers.fromExecutorService(pool);
        modelPropChangeMap = Collections.synchronizedMap(new EnumMap<>(ModelPropertyChangeType.class));
    }

    /**
     * add/register view to controller.
     * 
     * @param view
     */
    public void addViewChangeListner(ModelPropertyChangeListner view) {
        registeredViews.add(view);
    }

    /**
     * remove vier from controller.
     * 
     * @param view
     */
    public void removeViewChangeListner(ModelPropertyChangeListner view) {
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
     * @param propertyType = The name of the property.
     * @param newValue     = An object that represents the new value of the property.
     */
    protected void setModelProperty(ModelPropertyChangeType propertyType, Object... newValue) {
        Disposable oldChange = modelPropChangeMap.remove(propertyType);
        if (oldChange != null) {
            oldChange.dispose();
        }

        Disposable disp = Mono//
                .fromRunnable(() -> doModelPropertyChange(propertyType, newValue))//
                .publishOn(modelScheduler)//
                .delaySubscription(propertyType.getDelay())//
                .doOnSuccess(v -> LOGGER.trace("setModelProperty done"))//
                .subscribe();

        modelPropChangeMap.put(propertyType, disp);
    }

    protected void setModelPropertyWithoutDispose(ModelPropertyChangeType propertyType, Object... newValue) {
        Mono//
                .fromRunnable(() -> doModelPropertyChange(propertyType, newValue))//
                .publishOn(modelScheduler)//
                .delaySubscription(propertyType.getDelay())//
                .doOnSuccess(v -> LOGGER.trace("setModelPropertyWithoutDispose done"))//
                .subscribe();
    }

    protected synchronized void doOnModel(ModelPropertyChangeType propertyType, Runnable task, boolean disposeOld) {
        Disposable oldChange = modelPropChangeMap.remove(propertyType);
        if (disposeOld && oldChange != null) {
            oldChange.dispose();
        }

        Disposable disp = Mono//
                .fromRunnable(task)//
                .publishOn(modelScheduler)//
                .delaySubscription(propertyType.getDelay())//
                .doOnSubscribe(v -> LOGGER.trace("doOnModel start"))//
                .doOnSuccess(v -> LOGGER.trace("doOnModel done"))//
                .subscribe();

        modelPropChangeMap.put(propertyType, disp);
    }

    private void doModelPropertyChange(ModelPropertyChangeType propertyType, Object... newValue) {
        Class<?>[] methodParameter = Stream.of(newValue) //
                .map(Object::getClass) //
                .collect(Collectors.toList()) //
                .toArray(new Class[0]);

        registeredModels.stream()//
                .filter(v -> ((Class<?>) propertyType.getTargetClass()).isAssignableFrom(v.getClass()))//
                .forEach(v -> callRegistredModel(propertyType, methodParameter, v, newValue));
    }

    private void callRegistredModel(ModelPropertyChangeType propertyType, Class<?>[] methodParameter, InterfaceModel v,
            Object... newValue) {
        try {
            Method method = v.getClass().getMethod(propertyType.getMethode(), methodParameter);
            method.invoke(v, newValue);
        } catch (Exception ex) {
            // Handle exception.
        }
    }
}
