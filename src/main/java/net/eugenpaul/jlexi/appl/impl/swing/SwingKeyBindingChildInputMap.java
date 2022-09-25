package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import net.eugenpaul.jlexi.component.GuiGlyph;

public class SwingKeyBindingChildInputMap extends InputMap{

    private final transient Map<GuiGlyph, SwingKeyBindingChildInputMap> childsMaps;

    public SwingKeyBindingChildInputMap() {
        this.childsMaps = new HashMap<>();
    }

    @Override
    public Object get(KeyStroke keyStroke) {
        var response = super.get(keyStroke);
        if (response == null) {
            for (var child : this.childsMaps.entrySet()) {
                response = child.getValue().get(keyStroke);
                if (response != null) {
                    break;
                }
            }
        }

        return response;
    }

}
