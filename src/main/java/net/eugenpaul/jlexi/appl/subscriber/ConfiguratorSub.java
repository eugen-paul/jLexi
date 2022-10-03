package net.eugenpaul.jlexi.appl.subscriber;

import net.eugenpaul.jlexi.component.GuiGlyph;
import net.eugenpaul.jlexi.config.Configurator;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;

public class ConfiguratorSub implements EventSubscriber {

    private final Configurator configurator;

    public ConfiguratorSub(Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    public void update(Object source, Object type, Object data) {
        if (type == GlobalSubscribeTypes.REGISTER_GUI_ELEMENT && data instanceof GuiGlyph) {
            String name = ((GuiGlyph) data).getName();
            this.configurator.registerGui(name, (GuiGlyph) data);
            this.configurator.setAllsKeyBindings(name);
        }
    }
}
