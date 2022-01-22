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
     * @param mouseX the horizontal x position of the event relative to the window component.
     * @param mouseY the horizontal y position of the event relative to the window component.
     * @param button which, if any, of the mouse buttons has changed state.
     */
    public void clickOnWindow(int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_CLICK, mouseX, mouseY, button);
    }

    public void keyPressed(Character key) {
        setModelProperty(ModelPropertyChangeType.KEY_PRESSED, key);
    }

    @Override
    public void addEffect(Effect effect) {
        // I don't use "repeate" function because each repetition can have its own delay.
        Disposable disp = effectToMono(effect)//
                .doOnSuccess(v -> addEffect(effect))//
                .subscribe();

        effectMap.put(effect, disp);
    }

    private Mono<Object> effectToMono(Effect effect) {
        return Mono.empty()//
                .publishOn(modelScheduler)//
                .delaySubscription(effect.timeToNextExecute())//
                .doOnSubscribe(v -> effect.doEffect())//
                .doOnCancel(() -> {
                    effect.terminate();
                    effectMap.remove(effect);
                })//
        ;
    }

    @Override
    public void removeEffect(Effect effect) {
        Disposable disposeEffect = effectMap.remove(effect);
        if (disposeEffect != null) {
            disposeEffect.dispose();
        }
    }
}
