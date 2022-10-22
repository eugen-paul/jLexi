package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextHeader extends TextSharedElement {

    private TextHeaderConfiguration configuration;

    protected TextHeader(TextStructure sharedElement, EventManager changeManager,
            TextHeaderConfiguration configuration) {
        super(sharedElement, changeManager);
        this.configuration = configuration;
    }

}
