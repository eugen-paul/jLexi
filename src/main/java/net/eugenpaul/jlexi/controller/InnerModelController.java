package net.eugenpaul.jlexi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import net.eugenpaul.jlexi.component.interfaces.TextUpdateable;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link AbstractController} for all inner model changes that have no effect on the view
 * (initialization and registration of all internal elements).
 */
public class InnerModelController extends AbstractControllerV2 {

    private Map<String, TextUpdateable> textUpdateableMap;

    public InnerModelController(ExecutorService pool) {
        super(pool);
        this.textUpdateableMap = new HashMap<>();
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

}
