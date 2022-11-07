package net.eugenpaul.jlexi.component.text.format.structure;

import net.eugenpaul.jlexi.pubsub.EventManager;

public class TextHeaderV2 extends TextSharedElementV2 {

    private TextHeaderConfiguration configuration;

    protected TextHeaderV2(TextStructureV2 sharedElement, EventManager changeManager,
            TextHeaderConfiguration configuration) {
        super(sharedElement, changeManager);
        this.configuration = configuration;
    }

}
