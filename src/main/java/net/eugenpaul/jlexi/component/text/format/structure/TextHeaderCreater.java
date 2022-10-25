package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextHeaderCreater {

    private final TextStructure sharedElement;
    private final TextHeaderConfiguration configuration;
    private EventManager changeManager;

    public TextHeaderCreater(TextStructure sharedElement, TextHeaderConfiguration configuration) {
        this.changeManager = new EventManager();
        this.sharedElement = sharedElement;
        this.configuration = configuration;
    }

    public void reset() {
        this.changeManager = new EventManager();
    }

    public TextHeader createNext() {
        return new TextHeader(this.sharedElement, changeManager, this.configuration);
    }

}
