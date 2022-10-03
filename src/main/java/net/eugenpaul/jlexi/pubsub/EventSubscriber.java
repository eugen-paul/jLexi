package net.eugenpaul.jlexi.pubsub;

public interface EventSubscriber {

    void update(Object source, Object type, Object data);
}
