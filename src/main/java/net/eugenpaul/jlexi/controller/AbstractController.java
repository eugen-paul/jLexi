package net.eugenpaul.jlexi.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.effect.EffectHandler;
import net.eugenpaul.jlexi.gui.AbstractPanel;
import net.eugenpaul.jlexi.model.InterfaceModel;
import net.eugenpaul.jlexi.utils.Area;
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
public abstract class AbstractController implements PropertyChangeListener, EffectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private List<AbstractPanel> registeredViews;
    private List<InterfaceModel> registeredModels;

    private Map<String, Glyph> glyphMap;

    private ThreadPoolExecutor pool;
    protected Scheduler modelScheduler;

    private Map<ModelPropertyChangeType, Disposable> modelPropChangeMap;

    /**
     * C*tor
     */
    protected AbstractController() {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();
        glyphMap = new HashMap<>();

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

    public void addGlyph(Glyph glyph, String name) {
        glyphMap.put(name, glyph);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        registeredViews.forEach(v -> v.modelPropertyChange(evt));
    }

    public void addModel(InterfaceModel model) {
        registeredModels.add(model);
    }

    public Mono<Drawable> getDrawable(String source) {
        return Mono.fromCallable(() -> {
            Glyph destinationGlyph = glyphMap.get(source);
            if (destinationGlyph != null) {
                return destinationGlyph.getPixels();
            }
            throw new IllegalArgumentException("cann't find glyph: " + source);
        }) //
                .publishOn(modelScheduler)//
        ;
    }

    public Mono<Drawable> getDrawableArea(String source, Area area) {
        return Mono.fromCallable(() -> {
            Glyph destinationGlyph = glyphMap.get(source);
            if (destinationGlyph != null) {
                return destinationGlyph.getPixels(area.getPosition(), area.getSize());
            }
            throw new IllegalArgumentException("cann't find glyph: " + source);
        }) //
                .publishOn(modelScheduler)//
        ;
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
                .publishOn(modelScheduler)//
                .delaySubscription(propertyType.getDelay())//
                .doOnSuccess(v -> LOGGER.trace("setModelProperty done"))//
                .subscribe();

        modelPropChangeMap.put(propertyType, disp);
    }
}
