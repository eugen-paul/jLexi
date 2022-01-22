package net.eugenpaul.jlexi.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.eugenpaul.jlexi.gui.AbstractPanel;
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
public abstract class AbstractController implements PropertyChangeListener {
    private List<AbstractPanel> registeredViews;
    private List<InterfaceModel> registeredModels;

    private ThreadPoolExecutor pool;
    private Scheduler modelScheduler;

    private Map<ModelPropertyChangeType, Disposable> modelPropChangeMap;

    /**
     * C*tor
     */
    protected AbstractController() {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();

        pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        modelScheduler = Schedulers.fromExecutorService(pool);
        modelPropChangeMap = Collections.synchronizedMap(new EnumMap<>(ModelPropertyChangeType.class));
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
     * @param propertyType = The name of the property.
     * @param newValue     = An object that represents the new value of the property.
     */
    protected void setModelProperty(ModelPropertyChangeType propertyType, Object... newValue) {
        Disposable oldChange = modelPropChangeMap.remove(propertyType);
        if (oldChange != null) {
            oldChange.dispose();
        }

        Disposable disp = Mono.fromRunnable(() -> {
            Class<?>[] methodParameter = Stream.of(newValue) //
                    .map(Object::getClass) //
                    .collect(Collectors.toList()) //
                    .toArray(new Class[0]);

            registeredModels.stream()//
                    .filter(v -> ((Class<?>) propertyType.getTargetClass()).isAssignableFrom(v.getClass()))//
                    .forEach(v -> {
                        try {
                            Method method = v.getClass().getDeclaredMethod(propertyType.getMethode(), methodParameter);
                            method.invoke(v, newValue);
                        } catch (Exception ex) {
                            // Handle exception.
                        }
                    });
        })//
                .delaySubscription(Duration.ofMillis(50))//
                .publishOn(modelScheduler)//
                .subscribe();

        modelPropChangeMap.put(propertyType, disp);
    }
}
