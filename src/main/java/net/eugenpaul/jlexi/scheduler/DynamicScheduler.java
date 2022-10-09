package net.eugenpaul.jlexi.scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class DynamicScheduler {

    private final Map<DynamicEvent, Disposable> schedulerableMap;
    private final Scheduler modelScheduler;

    public DynamicScheduler(ExecutorService pool) {
        this.schedulerableMap = new HashMap<>();
        this.modelScheduler = Schedulers.fromExecutorService(pool);
    }

    public void addEffectToController(DynamicEvent effect) {
        // I don't use "repeate" function because each repetition can have its own delay.
        Disposable disp = effectToMono(effect)//
                .doOnSuccess(v -> addEffectToController(effect))//
                .subscribe();

        schedulerableMap.put(effect, disp);
    }

    private Mono<Object> effectToMono(DynamicEvent effect) {
        return Mono.empty()//
                .publishOn(modelScheduler)//
                .delaySubscription(effect.timeToNextExecute())//
                .doOnSubscribe(v -> effect.execute())//
                .doOnCancel(() -> {
                    effect.terminate();
                    schedulerableMap.remove(effect);
                })//
        ;
    }

    public void removeEffectFromController(DynamicEvent effect) {
        Disposable disposeEffect = schedulerableMap.remove(effect);
        if (disposeEffect != null) {
            disposeEffect.dispose();
        }
    }
}
