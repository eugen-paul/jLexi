package net.eugenpaul.jlexi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.eugenpaul.jlexi.component.interfaces.KeyBindingable;
import net.eugenpaul.jlexi.window.action.KeyBindingRule;

public class Configurator {
    private GlobalConfigurationDao config;
    private Map<String, KeyBindingable> nameToGui;

    public Configurator(String configPath) {
        this.config = new GlobalConfigurationDao(configPath);
        this.nameToGui = new HashMap<>();
    }

    public void init() throws IOException {
        this.config.load();
    }

    public boolean registerGui(String name, KeyBindingable guiElement) {
        if (nameToGui.containsKey(name)) {
            return false;
        }

        nameToGui.put(name, guiElement);

        return true;
    }

    public void setAllsKeyBindings(String name) {
        var gui = this.nameToGui.get(name);
        if (gui == null) {
            return;
        }

        var keys = this.config.getKeys(name);
        if (keys == null) {
            return;
        }

        for (var key : keys.getParameter()) {
            gui.registerDefaultKeyBindings(key.getName(), KeyBindingRule.FOCUS_WINDOW, key.getKeys());
        }
    }
}
