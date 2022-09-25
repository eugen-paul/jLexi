package net.eugenpaul.jlexi.appl.impl.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ComponentInputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import net.eugenpaul.jlexi.component.GuiGlyph;

public class SwingKeyBindingMainInputMap extends ComponentInputMap {

    private final transient Map<GuiGlyph, SwingKeyBindingChildInputMap> childsMaps;

    public SwingKeyBindingMainInputMap(JComponent component) {
        super(component);
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
