package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextFooter extends TextSharedElement {

    private TextFooterConfiguration configuration;

    protected TextFooter(TextStructure sharedElement, EventManager changeManager,
            TextFooterConfiguration configuration) {
        super(sharedElement, changeManager);
        this.configuration = configuration;
    }

}
