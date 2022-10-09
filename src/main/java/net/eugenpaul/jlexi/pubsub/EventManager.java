package net.eugenpaul.jlexi.pubsub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class EventManager {

    private final List<EventSubscriber> subscribers;
    private final Executor pool;

    /**
     * C*tor
     */
    public EventManager(Executor pool) {
        this.subscribers = new ArrayList<>();
        this.pool = pool;
    }

    /**
     * C*tor
     */
    public EventManager() {
        this.subscribers = new ArrayList<>();
        this.pool = Runnable::run;
    }

    public void addSubscriber(EventSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void removeSubscriber(EventSubscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    public void fireEvent(Object source, Object type, Object data) {
        this.subscribers.forEach(v -> pool.execute(() -> v.update(source, type, data)));
    }
}
