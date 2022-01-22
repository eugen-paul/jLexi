package net.eugenpaul.jlexi.data.effect;

import java.time.Duration;

public interface Effect {

    public boolean isDone();

    public Effect doEffect();

    public Duration timeToNextExecute();

    public void terminate();
}
