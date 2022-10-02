package net.eugenpaul.jlexi.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;
import net.eugenpaul.jlexi.effect.GlyphEffect;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link AbstractController} for all inner model changes that have no effect on the view
 * (initialization and registration of all internal elements).
 */
public class InnerModelController extends AbstractController {

    private Map<GlyphEffect, Disposable> effectMap;
    private Map<String, TextUpdateable> textUpdateableMap;

    public InnerModelController() {
        this.effectMap = Collections.synchronizedMap(new HashMap<>());
        this.textUpdateableMap = new HashMap<>();
    }

    public void undo(String name) {
        setModelPropertyWithoutDispose(ModelPropertyChangeType.TEXT_UNDO, name);
    }

    public void redo(String name) {
        setModelPropertyWithoutDispose(ModelPropertyChangeType.TEXT_REDO, name);
    }

    public void copy(String name) {
        setModelPropertyWithoutDispose(ModelPropertyChangeType.TEXT_COPY, name);
    }

    public void paste(String name) {
        setModelPropertyWithoutDispose(ModelPropertyChangeType.TEXT_PASTE, name);
    }

    public void addTextPane(String fieldName, TextUpdateable field) {
        textUpdateableMap.put(fieldName, field);
    }

    public void setText(String fieldName, List<TextSection> text) {
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
