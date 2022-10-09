package net.eugenpaul.jlexi.appl.subscriber;

import net.eugenpaul.jlexi.pubsub.EventSubscriber;
import net.eugenpaul.jlexi.scheduler.DynamicEvent;
import net.eugenpaul.jlexi.scheduler.DynamicScheduler;

public class SchedulerSub implements EventSubscriber {
    public static final String ADD_EVENT = "SchedulerSub:ADD_EVENT";
    public static final String REMOVE_EVENT = "SchedulerSub:REMOVE_EVENT";

    private final DynamicScheduler dynamicScheduler;

    public SchedulerSub(DynamicScheduler dynamicScheduler) {
        this.dynamicScheduler = dynamicScheduler;
    }

    @Override
    public void update(Object source, Object type, Object data) {
        if (type == ADD_EVENT && data instanceof DynamicEvent) {
            this.dynamicScheduler.addEffectToController((DynamicEvent) data);
        } else if (type == REMOVE_EVENT && data instanceof DynamicEvent) {
            this.dynamicScheduler.removeEffectFromController((DynamicEvent) data);
        }
    }

}
