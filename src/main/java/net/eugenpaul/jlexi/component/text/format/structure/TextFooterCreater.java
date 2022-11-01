package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextFooterCreater {

    private final TextStructure sharedElement;
    private final TextFooterConfiguration configuration;
    private EventManager changeManager;
    private int currentHeaderCount;

    public TextFooterCreater(TextStructure sharedElement, TextFooterConfiguration configuration) {
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

        return new TextFooter(this.sharedElement, changeManager, this.configuration);
    }

}
