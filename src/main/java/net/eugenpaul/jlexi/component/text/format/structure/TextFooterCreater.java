package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextFooterCreater {

    private final TextStructure sharedElement;
    private final TextFooterConfiguration configuration;
    private EventManager changeManager;

    public TextFooterCreater(TextStructure sharedElement, TextFooterConfiguration configuration) {
        this.changeManager = new EventManager();
        this.sharedElement = sharedElement;
        this.configuration = configuration;
    }

    public void reset() {
        this.changeManager = new EventManager();
    }

    public TextFooter createNext() {
        return new TextFooter(this.sharedElement, changeManager, this.configuration);
    }

}
