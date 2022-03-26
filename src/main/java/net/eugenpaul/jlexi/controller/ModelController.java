package net.eugenpaul.jlexi.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.event.MouseButton;
import reactor.core.publisher.Mono;
import reactor.core.Disposable;

/**
 * Implementation of {@link AbstractController} for jLexi
 */
public class ModelController extends AbstractController {

    private Map<GlyphEffect, Disposable> effectMap;
    private Map<String, TextUpdateable> textUpdateableMap;

    public ModelController() {
        effectMap = Collections.synchronizedMap(new HashMap<>());
        textUpdateableMap = new HashMap<>();
    }

    /**
     * Window size was changed
     * 
     * @param size new size of window
     */
    public void resizeMainWindow(String name, Size size) {
        setModelProperty(ModelPropertyChangeType.FORM_RESIZE, name, size);
    }

    /**
     * Mouse was clicked
     * 
     * @param mouseX the horizontal x position of the event relative to the window component.
     * @param mouseY the horizontal y position of the event relative to the window component.
     * @param button which, if any, of the mouse buttons has changed state.
     */
    public void clickOnWindow(String name, int mouseX, int mouseY, MouseButton button) {
        setModelProperty(ModelPropertyChangeType.MOUSE_CLICK, name, mouseX, mouseY, button);
    }

    public void keyTyped(String name, Character key) {
        setModelProperty(ModelPropertyChangeType.KEY_TYPED, name, key);
    }

    public void keyPressed(String name, KeyCode code) {
        setModelProperty(ModelPropertyChangeType.KEY_PRESSED, name, code);
    }

    public void keyReleased(String name, KeyCode code) {
        setModelProperty(ModelPropertyChangeType.KEY_RELEASED, name, code);
    }

    public void addTextPane(String fieldName, TextUpdateable field) {
        textUpdateableMap.put(fieldName, field);
    }

    public void setText(String fieldName, List<TextElement> text) {
        Mono.empty()//
                .publishOn(modelScheduler)//
                .doOnSuccess(v -> {
                    TextUpdateable field = textUpdateableMap.get(fieldName);
                    if (field != null) {
                        field.setText(text);
                    }
                })//
                .subscribe() //
        ;
    }

    @Override
    public void addEffectToController(GlyphEffect effect) {
        // I don't use "repeate" function because each repetition can have its own delay.
        Disposable disp = effectToMono(effect)//
                .doOnSuccess(v -> addEffectToController(effect))//
                .subscribe();

        effectMap.put(effect, disp);
    }

    private Mono<Object> effectToMono(GlyphEffect effect) {
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
    public void removeEffectFromController(GlyphEffect effect) {
        Disposable disposeEffect = effectMap.remove(effect);
        if (disposeEffect != null) {
            disposeEffect.dispose();
        }
    }
}
