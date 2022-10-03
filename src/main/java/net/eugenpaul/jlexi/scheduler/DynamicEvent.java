package net.eugenpaul.jlexi.scheduler;

import java.time.Duration;

public interface DynamicEvent {
    void execute();

    Duration timeToNextExecute();

    void terminate();
}
