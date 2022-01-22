package net.eugenpaul.jlexi.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.data.Size;
import net.eugenpaul.jlexi.data.effect.Effect;
import net.eugenpaul.jlexi.data.framing.MouseButton;
import reactor.core.publisher.Mono;
import reactor.core.Disposable;

/**
 * Implementation of {@link AbstractController} for jLexi
 */
public class DefaultController extends AbstractController {

    private Map<Effect, Disposable> effectMap;

    public DefaultController() {
        effectMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Window size was changed
     * 
     * @param size new size of window
     */
    public void resizeMainWindow(Size size) {
        setModelProperty(ModelPropertyChangeType.FORM_RESIZE, size);
    }

    /**
     * Mouse was clicked
     * 
     * @param mouseX Returns the horizontal x position of the event relative to the window component.
     * @param mouseY Returns the horizontal y position of the event relative to the window component.
     * @param button Returns which, if any, of the mouse buttons has changed state.
     */
    public void clickOnWindow(int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_CLICK, mouseX, mouseY, button);
    }

    @Override
    public void addEffect(Effect effect) {
        Disposable disp = Mono.fromRunnable(effect::doEffect)//
                .delaySubscription(effect.timeToNextExecute())//
                .publishOn(modelScheduler)//
                .repeatWhen(v -> v)//
                .doOnSubscribe(v -> effect.doEffect())//
                .doOnCancel(effect::terminate)//
                .subscribe();

        effectMap.put(effect, disp);
    }

    @Override
    public void removeEffect(Effect effect) {
        Disposable disposeEffect = effectMap.remove(effect);
        if (disposeEffect != null) {
            disposeEffect.dispose();
        }
    }
}
