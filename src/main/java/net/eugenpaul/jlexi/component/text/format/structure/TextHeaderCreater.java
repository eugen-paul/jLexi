package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextHeaderCreater {

    private final TextStructure sharedElement;
    private final TextHeaderConfiguration configuration;
    private EventManager changeManager;
    private int currentHeaderCount;

    public TextHeaderCreater(TextStructure sharedElement, TextHeaderConfiguration configuration) {
        this.changeManager = new EventManager();
        this.sharedElement = sharedElement;
        this.configuration = configuration;
        this.currentHeaderCount = 0;
    }

    public void reset() {
        this.changeManager = new EventManager();
        this.currentHeaderCount = 0;
    }

    public TextStructure createNext() {
        currentHeaderCount++;

        if (currentHeaderCount == 1 && !this.configuration.isOnFirstPage()) {
            return new TextEmpty(null);
        }

        return new TextHeader(this.sharedElement, changeManager, this.configuration);
    }

}
