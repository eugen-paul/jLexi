package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextFooterCreaterV2 {

    private final TextStructureV2 sharedElement;
    private final TextFooterConfiguration configuration;
    private EventManager changeManager;
    private int currentHeaderCount;

    public TextFooterCreaterV2(TextStructureV2 sharedElement, TextFooterConfiguration configuration) {
        this.changeManager = new EventManager();
        this.sharedElement = sharedElement;
        this.configuration = configuration;
        this.currentHeaderCount = 0;
    }

    public void reset() {
        this.changeManager = new EventManager();
        this.currentHeaderCount = 0;
    }

    public TextStructureV2 createNext() {
        currentHeaderCount++;

        if (currentHeaderCount == 1 && !this.configuration.isOnFirstPage()) {
            return new TextEmptyV2(null);
        }

        return new TextFooterV2(this.sharedElement, changeManager, this.configuration);
    }

}
