package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextFooterV2 extends TextSharedElementV2 {

    private TextFooterConfiguration configuration;

    protected TextFooterV2(TextStructureV2 sharedElement, EventManager changeManager,
            TextFooterConfiguration configuration) {
        super(sharedElement, changeManager);
        this.configuration = configuration;
    }

}
